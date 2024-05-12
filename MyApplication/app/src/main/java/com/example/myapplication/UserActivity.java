package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import helper_classes_and_methods.*;

public class UserActivity extends AppCompatActivity {
    User user;
    private StorageHandler fireAlarmStorageHandler;
    private StorageHandler loginStorageHandler;

    /**
     * Author: Yujing Zhang u7671098:set display parameters
     * Author: Hongyu Li: implemented bottom navi bar
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user = (User) getIntent().getExtras().getSerializable("User");

        TextView userName = findViewById(R.id.userName);
        userName.setText("Welcome!  "+user.getUserID());
        ListView logList = findViewById(R.id.logList);

        // Fire Alarm Logs
        FireAlarmStorageHandler fireAlarmStorageHandler = new FireAlarmStorageHandler(this);
        List<String> fireAlarmData = fireAlarmStorageHandler.loadAllLogs();

        // Login Logs
        LoginStorageHandler loginStorageHandler = new LoginStorageHandler(this);
        List<String> loginData = loginStorageHandler.loadAllLogs();

        // Combine the two lists
        List<String> combinedData = new ArrayList<>();
        combinedData.addAll(fireAlarmData);
        combinedData.addAll(loginData);

        // Sort the combined list in descending order
        Collections.sort(combinedData, Comparator.reverseOrder());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, combinedData);
        logList.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_user);
        bottomNavigationView.setOnItemSelectedListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_map) {

                        if (!(getApplicationContext() instanceof MapActivity)) {
                            Intent intent = new Intent(this, MapActivity.class);
                            intent.putExtra("USER",user);
                            startActivity(intent);
                        }

                        return true;
                    }
                    return false;
                }

        );

    }

}
