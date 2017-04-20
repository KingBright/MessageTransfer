package name.kingbright.messagetransfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

import name.kingbright.messagetransfer.core.InboxSmsReader;
import name.kingbright.messagetransfer.core.Intents;
import name.kingbright.messagetransfer.core.MessageTransferService;
import name.kingbright.messagetransfer.core.models.SmsMessage;
import name.kingbright.messagetransfer.ui.InfoListFragment;
import name.kingbright.messagetransfer.util.JsonUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initHomeFragment();
    }

    private void initHomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        InfoListFragment infoListFragment = new InfoListFragment();
        fragmentTransaction.replace(R.id.fragment_container, infoListFragment, infoListFragment.getTag());
        fragmentTransaction.commit();
    }

    private void startBind(String weiXinId) {
        Intent intent = new Intent(getApplicationContext(), MessageTransferService.class);
        intent.setAction(Intents.ACTION_BIND);
        intent.putExtra(Intents.EXTRA_WEIXIN_ID, weiXinId);
        startService(intent);
    }

    private void readSms() {
        InboxSmsReader reader = new InboxSmsReader(getApplicationContext());
        List<SmsMessage> list = reader.getSmsInboxWithLimit(1);
        if (list.size() > 0) {
            SmsMessage message = list.get(0);
            Log.d(TAG, JsonUtil.toJson(message));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d(TAG, "onNavigationItemSelected : " + item.getTitle());
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
