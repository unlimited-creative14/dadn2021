package com.hk203.dadn;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.hk203.dadn.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    String username;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    public void onBackPressed()
    {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getIntent().getStringExtra("username");
        Toast.makeText(this, "Hello " + username, Toast.LENGTH_LONG).show();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preCreate();
        addGroupNameDrawer(binding.navView);

<<<<<<< HEAD
        mqttService = new MQTTService(this);
        mqttService.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                SendRequestTimer();
            }

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println(cause.toString());
            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage
                    message) throws Exception {
                Log.d("mqtt",message.toString());
            }
        });
    }

    private void sendDataMQTT(String data) {
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);
        byte[] b = data.getBytes(StandardCharsets.UTF_8);
        msg.setPayload(b);
        Log.d("ABC", "Publish:" + msg);
        try {
            mqttService.mqttAndroidClient.publish("malongnhan/feeds/server", msg);
        } catch (MqttException e) {

        }
    }

    void SendRequestTimer() {
        Timer aTimer = new Timer();
        TimerTask aTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
                sendDataMQTT("this will be json");
            }
        };

        aTimer.schedule(aTask, 1000, 10000);
=======
>>>>>>> b3588b8738dd27812b279c6db4875c0eae71602e
    }

    void preCreate() {
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_add_account,
                R.id.nav_home,
                R.id.nav_updateHealthRule,
                R.id.nav_adafruit_demo
        ).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    void addGroupNameDrawer(NavigationView nav) {
        // Add item to drawer
        Menu drawerMenu = nav.getMenu();

        MenuItem adminLabel = drawerMenu.findItem(R.id.lbAdmin);
        TextView tv = new TextView(getApplicationContext());
        tv.setText(getString(R.string.menu_lbadmin));
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(20);
        tv.setTypeface(null, Typeface.BOLD);
        adminLabel.setActionView(tv);

        MenuItem accountLabel = drawerMenu.findItem(R.id.lbAccount);
        TextView tv1 = new TextView(getApplicationContext());
        tv1.setText(getString(R.string.menu_lbaccount));
        tv1.setTextColor(Color.BLACK);
        tv1.setTextSize(20);
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setGravity(Gravity.START);
        accountLabel.setActionView(tv1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}