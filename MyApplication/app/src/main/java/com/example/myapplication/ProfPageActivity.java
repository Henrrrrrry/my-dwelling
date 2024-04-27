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

        Button followButton = findViewById(R.id.followButton);
        Button fireAlarmNoti = findViewById(R.id.fireAlarmButton);
        TextView buildingInfo = findViewById(R.id.buildingInfo);
        //--yujing--
        String[] building = {"1234 Main St","7","True", "1990", "30", "Concrete"};//string[] sample for display test

        String infoText = "Address: " + building[0] + "\n" +
                "Seismic Rating: "+building[1] + "\n" +
                "Need Repair: "+building[2] + "\n" +
                "Year of construction: "+building[3] + "\n" +
                "Life(years): "+building[4] + "\n" +
                "Materials: "+building[5];
        buildingInfo.setText(infoText);

        //----yujing--end-----
        followButton.setOnClickListener(new View.OnClickListener() { //follow button
            @Override
            public void onClick(View v) {
// link fire alarm noti to subcribers
                if (followButton.getText().toString().equals("Follow") ){//if click, changed color and show 'unfollow'
                    followButton.setText("Unfollow");

                }
                else{
                    followButton.setText("Follow");//if click, changed to 'follow'
                }

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

