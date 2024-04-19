package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_page);

        Button followButton = null;
        Button fireAlarmNoti=null;
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// link fire alarm noti to subcribers
            }
        });

        fireAlarmNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               publish noti function
            }
        });
    }
}