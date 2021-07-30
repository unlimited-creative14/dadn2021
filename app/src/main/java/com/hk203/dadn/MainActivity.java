package com.hk203.dadn;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.hk203.dadn.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private String authToken;
    private UserRole role;

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

        authToken = getIntent().getStringExtra("authToken");
        role =(UserRole)getIntent().getSerializableExtra("role");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preCreate(role);
        addGroupNameDrawer(binding.navView);

    }

    void preCreate(UserRole role) {
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_add_account,
                R.id.nav_home,
                R.id.nav_updateHealthRule,
                R.id.nav_adafruit_demo,
                R.id.nav_patient_list,
                R.id.nav_notifications,
                R.id.profileFragment
        ).setDrawerLayout(drawer).build();
        if (role == UserRole.Admin) {
            navigationView.getMenu().setGroupVisible(R.id.medical_staffNavGroup, false);
        }
        else{
            navigationView.getMenu().setGroupVisible(R.id.adminNavGroup, false);
        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.tv_user_email)).setText(
                "User name"
        );
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

        MenuItem msLabel = drawerMenu.findItem(R.id.lbMedicalStaff);
        TextView tv2 = new TextView(getApplicationContext());
        tv2.setText(getString(R.string.menu_lbmediccal_staff));
        tv2.setTextColor(Color.BLACK);
        tv2.setTextSize(20);
        tv2.setTypeface(null, Typeface.BOLD);
        msLabel.setActionView(tv2);

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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public String getAuthToken(){
        return authToken;
    }
}