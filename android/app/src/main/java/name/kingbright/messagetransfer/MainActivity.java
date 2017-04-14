package name.kingbright.messagetransfer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import name.kingbright.messagetransfer.core.WebSocketManager;

public class MainActivity extends AppCompatActivity {
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

    }
}
