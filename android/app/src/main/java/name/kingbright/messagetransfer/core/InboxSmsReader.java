//package name.kingbright.messagetransfer.core;
//
//import android.content.ContentResolver;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteException;
//import android.net.Uri;
//import android.support.v4.content.ContentResolverCompat;
//import android.util.Log;
//
///**
// * @author Jin Liang
// * @since 2017/4/16
// */
//
//public class InboxSmsReader {
//    private final String SMS_URI_INBOX = "content://sms/inbox";
//
//    public String getSmsInPhone(ContentResolver contentResolver) {
//        StringBuilder smsBuilder = new StringBuilder();
//        try {
//            Uri uri = Uri.parse(SMS_URI_INBOX);
//            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
//            Cursor cur = ContentResolverCompat.query(contentResolver, uri, projection, null, null, "date desc", null);
//            if (cur.moveToFirst()) {
//                int index_Address = cur.getColumnIndex("address");
//                int index_Person = cur.getColumnIndex("person");
//                int index_Body = cur.getColumnIndex("body");
//                int index_Date = cur.getColumnIndex("date");
//                int index_Type = cur.getColumnIndex("type");
//
//                do {
//                    String strAddress = cur.getString(index_Address);
//                    int intPerson = cur.getInt(index_Person);
//                    String strbody = cur.getString(index_Body);
//                    long longDate = cur.getLong(index_Date);
//                    int intType = cur.getInt(index_Type);
//
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                    Date d = new Date(longDate);
//                    String strDate = dateFormat.format(d);
//
//                    String strType = "";
//                    if (intType == 1) {
//                        strType = "接收";
//                    } else if (intType == 2) {
//                        strType = "发送";
//                    } else {
//                        strType = "null";
//                    }
//
//                    smsBuilder.append("[ ");
//                    smsBuilder.append(strAddress + ", ");
//                    smsBuilder.append(intPerson + ", ");
//                    smsBuilder.append(strbody + ", ");
//                    smsBuilder.append(strDate + ", ");
//                    smsBuilder.append(strType);
//                    smsBuilder.append(" ]\n\n");
//                } while (cur.moveToNext());
//
//                if (!cur.isClosed()) {
//                    cur.close();
//                    cur = null;
//                }
//            } else {
//                smsBuilder.append("no result!");
//            } // end if
//
//            smsBuilder.append("getSmsInPhone has executed!");
//
//        } catch (SQLiteException ex) {
//            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
//        }
//
//        return smsBuilder.toString();
//    }
//}
