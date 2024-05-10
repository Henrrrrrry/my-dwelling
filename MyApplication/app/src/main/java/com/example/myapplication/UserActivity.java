package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import helper_classes_and_methods.*;

public class UserActivity extends AppCompatActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user = (User) getIntent().getExtras().getSerializable("User");



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