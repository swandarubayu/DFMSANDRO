package com.ramadhany.vodjo.latihan1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;

import com.ramadhany.vodjo.latihan1.Controller.Setting;
import com.ramadhany.vodjo.latihan1.Menu.MapsActivity;
import com.ramadhany.vodjo.latihan1.Menu.RiskAreaActivity;
import com.ramadhany.vodjo.latihan1.Menu.UserProfActivity;
import com.ramadhany.vodjo.latihan1.Menu.AboutActivity;
import com.ramadhany.vodjo.latihan1.View.LoginActivity;
import com.ramadhany.vodjo.latihan1.helper.SQLiteHandler;
import com.ramadhany.vodjo.latihan1.helper.SessionManager;


public class MainActivity extends AppCompatActivity {

    private View btnMaps, btnRiskArea, btnUserProf, btnAbout;

     private SessionManager session;
     private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext().getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext().getApplicationContext());

        btnMaps = findViewById(R.id.btnMaps);
        btnRiskArea = findViewById(R.id.btnRiskArea);
        btnUserProf = findViewById(R.id.btnUserProf);
        btnAbout = findViewById(R.id.btnAbout);

        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        btnRiskArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RiskAreaActivity.class);
                startActivity(intent);
            }
        });

        btnUserProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserProfActivity.class);
                startActivity(intent);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        //starting service
        //startService(new Intent(MainActivity.this, MyService.class));

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);

                        Log.d("LatLang", latitude + " " + longitude);

                        if (latitude != null && longitude != null) {
                            Log.d("LatLang", latitude + "" + longitude);
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );
    }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.main_menu, menu);

         return super.onCreateOptionsMenu(menu);
     }

     /**
      * On selecting action bar icons
      * */
     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         // Take appropriate action for each action item click
         switch (item.getItemId()) {
             case R.id.action_logout:
                 // logout action
                 logoutUser();
                 return true;
             default:
                 return super.onOptionsItemSelected(item);
         }
     }

     /**
      * Logging out the user. Will set isLoggedIn flag to false in shared
      * preferences Clears the user data from sqlite users table
      * */
     private void logoutUser() {
         session.setLogin(false);

         db.deleteUsers();

         // Launching the login activity
         Intent intent = new Intent(MainActivity.this, LoginActivity.class);
         startActivity(intent);
         finish();
     }

    @Override
    protected void onResume() {
        super.onResume();
        new Setting(this).checkEnableGPS();
        new Setting(this).forceEnableGPSAcess();
        new Setting(this).getLocation();
    }


}