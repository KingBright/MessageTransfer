package name.kingbright.messagetransfer.core;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.content.ContentResolverCompat;

import java.util.ArrayList;
import java.util.List;

import name.kingbright.messagetransfer.core.models.SmsMessage;

/**
 * @author Jin Liang
 * @since 2017/4/16
 */

public class InboxSmsReader {

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

    private String getSenderNameById(ContentResolver contentResolver, int person) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        return "";
    }

    public List<SmsMessage> getSmsInboxWithLimit(int limit) {
        return getSmsInbox(null, null, limit);
    }

    public List<SmsMessage> getSmsInbox(String selection, String[] args, int limit) {
        List<SmsMessage> list = new ArrayList<>();
        try {
            Uri uri = getInboxUri();
            String[] projection = new String[]{Telephony.Sms.Inbox.ADDRESS, Telephony.Sms
                    .Inbox.PERSON, Telephony.Sms.Inbox.BODY, Telephony.Sms.Inbox.DATE};

            Cursor cursor = ContentResolverCompat.query(mContentResolver, uri, projection,
                    selection, args, "date desc" + (limit > 0 ? " limit " + limit : ""), null);
            if (cursor.moveToFirst()) {
                int addressIndex = cursor.getColumnIndex(Telephony.Sms.Inbox.ADDRESS);
                int personIndex = cursor.getColumnIndex(Telephony.Sms.Inbox.PERSON);
                int bodyIndex = cursor.getColumnIndex(Telephony.Sms.Inbox.BODY);
                int dateIndex = cursor.getColumnIndex(Telephony.Sms.Inbox.DATE);
                do {
                    String address = cursor.getString(addressIndex);
                    String body = cursor.getString(bodyIndex);
                    long date = cursor.getLong(dateIndex);

                    list.add(mMessageFactory.buildSmsMessage(address, body, date));
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return list;
    }
}
