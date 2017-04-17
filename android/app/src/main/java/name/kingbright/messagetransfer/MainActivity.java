package name.kingbright.messagetransfer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import name.kingbright.messagetransfer.core.AbsWebSocketManager;
import name.kingbright.messagetransfer.core.InboxSmsReader;
import name.kingbright.messagetransfer.core.MessageFactory;
import name.kingbright.messagetransfer.core.WebSocketManagerImpl;
import name.kingbright.messagetransfer.core.models.SmsMessage;
import name.kingbright.messagetransfer.util.SystemUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private AbsWebSocketManager mWebSocketManager;

    private InboxSmsReader mInboxSmsReader;
    private MessageFactory mMessageFactory;

    private List<String> mPermissions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInboxSmsReader = new InboxSmsReader(getContext());
        mMessageFactory = MessageFactory.getInstance(getContext());

        mWebSocketManager = new WebSocketManagerImpl();
        mWebSocketManager.setUrl(Constants.WSS_SERVER);
        mWebSocketManager.start();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<SmsMessage> list = mInboxSmsReader.getSmsInboxWithLimit(1);
                if (list.size() > 0) {
                    String msg = mMessageFactory.wrapToString(list.get(0));
                    Log.d(TAG, "latest sms is : " + msg);
                    mWebSocketManager.sendMessage(msg);
                }
            }
        });

        checkPermissions();
    }

    private void checkPermissions() {
        checkPermission(Manifest.permission.RECEIVE_SMS);
        checkPermission(Manifest.permission.READ_SMS);
        checkPermission(Manifest.permission.READ_CONTACTS);
        checkPermission(Manifest.permission.READ_PHONE_STATE);
        if (mPermissions.size() > 0) {
            String[] array = new String[mPermissions.size()];
            requestPermission(MainActivity.this, mPermissions.toArray(array));
        }
    }

    private void checkPermission(String permission) {
        if (!SystemUtil.checkPermission(getContext(), permission)) {
            mPermissions.add(permission);
            addHintCard(permission);
        }
    }

    /**
     * Add a card to explain why we need the permission.
     *
     * @param permission
     */
    private void addHintCard(String permission) {

    }

    private Context getContext() {
        return getApplicationContext();
    }

    public void requestPermission(Activity activity, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions,
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
