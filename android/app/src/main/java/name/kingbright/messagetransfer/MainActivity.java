package name.kingbright.messagetransfer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import name.kingbright.messagetransfer.core.WebSocketManager;

public class MainActivity extends AppCompatActivity {
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    
    private WebSocketManager websocketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        websocketManager = new WebSocketManager();
        websocketManager.start();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                websocketManager.sendMessage("Hello there!");
            }
        });

        if (!checkPermission(getContext(), Manifest.permission.RECEIVE_SMS)) {
            requestPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS);
        }
    }

    private Context getContext() {
        return getApplicationContext();
    }

    public boolean checkPermission(Context context, String permission) {
        int result = PermissionChecker.checkCallingOrSelfPermission(context, permission);
        if (result == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermission(Activity activity, String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                permission)) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }
}
