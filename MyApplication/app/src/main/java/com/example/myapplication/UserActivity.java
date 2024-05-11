package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helper_classes_and_methods.*;

public class UserActivity extends AppCompatActivity {
    User user;
    private StorageHandler fireAlarmStorageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user = (User) getIntent().getExtras().getSerializable("User");
        ListView logList = findViewById(R.id.logList);
        TextView userName = findViewById(R.id.userName);
        userName.setText("Welcome!  "+user.getUserID());

        fireAlarmStorageHandler = new FireAlarmStorageHandler(this);
        List<String> fireAlarmData = fireAlarmStorageHandler.loadAllLogs();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fireAlarmData);
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
