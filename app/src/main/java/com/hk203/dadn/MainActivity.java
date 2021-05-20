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

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.hk203.dadn.databinding.ActivityMainBinding;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    String username;
    MQTTService mqttService;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getIntent().getStringExtra("username");
        Toast.makeText(this, "Hello " + username, Toast.LENGTH_LONG).show();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preCreate();
        addGroupNameDrawer(binding.navView);

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
    }


//    GraphView graph = (GraphView) findViewById(R.id.graph);
//    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//            new DataPoint(0, 1),
//            new DataPoint(1, 5),
//            new DataPoint(2, 3),
//            new DataPoint(3, 2),
//            new DataPoint(4, 6)
//    });
//        graph.addSeries(series);

    void preCreate() {
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery,
                R.id.nav_slideshow, R.id.nav_updateHealthRule
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