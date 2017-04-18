package name.kingbright.messagetransfer.core;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.content.ContentResolverCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import name.kingbright.messagetransfer.core.models.SmsMessage;

/**
 * @author Jin Liang
 * @since 2017/4/16
 */

public class InboxSmsReader {

    private static final String TAG = "InboxSmsReader";
    private Context mContext;

    private ContentResolver mContentResolver;

    private MessageFactory mMessageFactory;

    public InboxSmsReader(Context context) {
        mContext = context;
        mContentResolver = mContext.getContentResolver();
        mMessageFactory = MessageFactory.getInstance(mContext);
    }

    private Uri getInboxUri() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Telephony.Sms.Inbox.CONTENT_URI;
        } else {
            return Uri.parse("content://sms/inbox");
        }
    }

    public static String getSenderNameByNumber(ContentResolver contentResolver, String number) {
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

        Cursor cursor = ContentResolverCompat.query(contentResolver, ContactsContract
                .CommonDataKinds.Phone.CONTENT_URI, projection, ContactsContract.CommonDataKinds.Phone.NUMBER
                + "=?", new String[]{number}, null, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if (TextUtils.isEmpty(name)) {
                return number;
            }
            return name;
        }
        return number;
    }

    public List<SmsMessage> getSmsInboxWithLimit(int limit) {
        return getSmsInbox(null, null, limit);
    }

    public List<SmsMessage> getSmsInbox(String selection, String[] args, int limit) {
        List<SmsMessage> list = new ArrayList<>();
        try {
            Uri uri = getInboxUri();
            String[] projection = new String[]{Telephony.Sms.Inbox.ADDRESS, Telephony.Sms.Inbox.BODY, Telephony.Sms.Inbox.DATE};

            Cursor cursor = ContentResolverCompat.query(mContentResolver, uri, projection,
                    selection, args, "date desc" + (limit > 0 ? " limit " + limit : ""), null);
            if (cursor.moveToFirst()) {
                int addressIndex = cursor.getColumnIndex(Telephony.Sms.Inbox.ADDRESS);
                int bodyIndex = cursor.getColumnIndex(Telephony.Sms.Inbox.BODY);
                int dateIndex = cursor.getColumnIndex(Telephony.Sms.Inbox.DATE);
                do {
                    String address = cursor.getString(addressIndex);
                    String body = cursor.getString(bodyIndex);
                    long date = cursor.getLong(dateIndex);
                    String name = getSenderNameByNumber(mContentResolver, address);
                    Log.d(TAG, "address : " + address);
                    list.add(mMessageFactory.buildSmsMessage(name, body, date));
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return list;
    }
}
