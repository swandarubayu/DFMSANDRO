package com.ramadhany.vodjo.latihan1.Menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.ramadhany.vodjo.latihan1.R;
import com.ramadhany.vodjo.latihan1.helper.SQLiteHandler;
import com.ramadhany.vodjo.latihan1.helper.SessionManager;

import java.util.HashMap;

/**
 * Created by user on 7/3/2018.
 */

public class UserProfActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private TextView txtUsername, txtDomisili;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprof);
        setTitle("User Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext().getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String username = user.get("username");
        String domisili = user.get("domisili");

        txtUsername = (TextView) findViewById(R.id.username);
        txtDomisili = (TextView) findViewById(R.id.user_dom);

        txtUsername.setText(username);
        txtDomisili.setText(domisili);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

