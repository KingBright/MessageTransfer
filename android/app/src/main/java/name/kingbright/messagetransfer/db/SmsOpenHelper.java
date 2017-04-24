package name.kingbright.messagetransfer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import name.kingbright.messagetransfer.core.models.SmsMessage;
import name.kingbright.messagetransfer.util.L;

public class SmsOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "SmsOpenHelper";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "message_transfer";
    private static final String SMS_TABLE_NAME = "sms";

    private static final String ID = "_id";
    private static final String BODY = "body";
    private static final String SENDER = "sender";
    private static final String SIGN = "sign";
    private static final String TIME = "time";
    private static final String STATE = "state";

    private static final String TABLE_CREATE = "CREATE TABLE " + SMS_TABLE_NAME + " ("
            + ID + " LONG, "
            + BODY + " TEXT, "
            + SENDER + " TEXT, "
            + SIGN + " TEXT, "
            + TIME + " LONG, "
            + STATE + " INTEGER);";


    private static final int UN_SYNCED = 0;
    private static final int SYNCED = 1;

    private static SmsOpenHelper instance;

    public static SmsOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SmsOpenHelper(context);
        }
        return instance;
    }

    private SmsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertOrUpdate(SmsMessage message) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = convertToContentValues(message, UN_SYNCED);

        if (checkRowExistence(db, values)) {
            int result = update(db, values);
            if (result > 0) {
                L.d(TAG, "successfully updated sms");
            } else {
                L.d(TAG, "failed to update");
            }
        } else {
            long id = insert(db, values);
            if (id == -1) {
                L.d(TAG, "failed to insert");
            } else {
                L.d(TAG, "successfully inserted sms");
            }
        }
    }

    public void updateToSynced(String sign) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATE, SYNCED);
        values.put(SIGN, sign);
        if (checkRowExistence(db, values)) {
            int result = update(db, values);
            if (result > 0) {
                L.d(TAG, "set sms synced");
            } else {
                L.d(TAG, "failed to set synced");
            }
        } else {
            L.d(TAG, "no local record to update");
        }
    }

    private int update(SQLiteDatabase db, ContentValues values) {
        return db.update(SMS_TABLE_NAME, values, SIGN + "=?", new String[]{values.getAsString
                (SIGN)});
    }

    private long insert(SQLiteDatabase db, ContentValues values) {
        return db.insert(SMS_TABLE_NAME, null, values);
    }

    private boolean checkRowExistence(SQLiteDatabase db, ContentValues values) {
        Cursor cursor = null;
        try {
            cursor = db.query(SMS_TABLE_NAME, new String[]{SIGN}, SIGN + "=?", new String[]{values.getAsString(SIGN)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private ContentValues convertToContentValues(SmsMessage message, int synced) {
        ContentValues values = new ContentValues();
        values.put(BODY, message.body);
        values.put(SENDER, message.sender);
        values.put(SIGN, message.sign);
        values.put(TIME, message.time);
        values.put(STATE, UN_SYNCED);
        return values;
    }
}