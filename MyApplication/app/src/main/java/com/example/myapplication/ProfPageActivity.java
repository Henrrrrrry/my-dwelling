package com.example.myapplication;

import helper_classes_and_methods.*;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;//import for display in TextView
//import android.widget.ImageView;

public class ProfPageActivity extends AppCompatActivity {
    private TextView buildingInfo;

    //private ImageView buildingImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_page);

        Button followButton = null;
        Button fireAlarmNoti = null;
        //--yujing--
        //buildingImage = findViewById(R.id.buildingImage);
//        buildingInfo = findViewById(R.id.buildingInfo);
        String[] building = {"1234 Main St","7","True", "1990", "30", "Concrete"};//string[] sample for display test

        String infoText = "Addr: " + building[0] + "\n" +
                building[1] + "\n" +
                building[2] + "\n" +
                building[3] + "\n" +
                building[4] + "\n" +
                building[5];
        buildingInfo.setText(infoText);

        //----yujing--end-----
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

