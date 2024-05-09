package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import helper_classes_and_methods.BuildingMaterial;
import helper_classes_and_methods.Dwelling;
import helper_classes_and_methods.Observer;
import helper_classes_and_methods.User;
//import android.widget.ImageView;

public class ProfPageActivity extends BaseActivity {
    //                test case create channel ID
    public static final String CHANNEL_ID = "uniqueChannelId";
     ArrayList<Observer> observers1= new ArrayList<>();

    //private ImageView buildingImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        test case create fake info&channel
        User maintainer = new User("mok3@163.com","666666",true,"Xinfei");
        Dwelling.Location locationTest = new Dwelling.Location(-35.255290,149.145020);
        Dwelling dwelling= new Dwelling("41 davenport St", LocalDate.of(1948, Month.OCTOBER, 1),BuildingMaterial.WOOD,observers1,maintainer,locationTest);
        User user1 = new User("a2546556102@gmail.com", "123456",false,"Henry");
        observers1.add(user1);
        createNotificationChannel();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_page);

        ImageView buildingImage = findViewById(R.id.buildingImage);
        buildingImage.setImageResource(R.drawable.img_default_building);

        Button followButton = findViewById(R.id.followButton);
        Button fireAlarmNoti = findViewById(R.id.fireAlarmButton);
        Button backButton = findViewById(R.id.backButton);
        TextView buildingInfo = findViewById(R.id.buildingInfo);
        //TODO: this is test data reply with real data
        Dwelling searchDwelling = (Dwelling) getIntent().getExtras().getSerializable("Dwelling");
//        String[] building = {
//                searchDwelling.getAddress(),
//                String.valueOf(searchDwelling.getSeismicRating()),
//                String.valueOf(searchDwelling.isFireAlarm()),
//                String.valueOf(searchDwelling.getConstructionDate()),
//                String.valueOf(searchDwelling.getBuildingMaterial())
//        };
        //String[] building = {"1234 Main St","7","True", "1990", "30", "Concrete"};//string[] sample for display test

        String infoText = "Address: " + searchDwelling.getAddress() + "\n" +
                "Seismic Rating: "+searchDwelling.getSeismicRating() + "\n" +
                "Year of construction: "+searchDwelling.getConstructionDate() + "\n" +
                "Materials: "+searchDwelling.getBuildingMaterial();
        buildingInfo.setText(infoText);



        if (dwelling.getObservers().contains(user1)) {
            followButton.setBackgroundColor(Color.rgb(128,128,128));
            followButton.setText("Unfollow");
        } else {
            followButton.setBackgroundColor(Color.rgb(0,0,128));
            followButton.setText("Follow");
        }

        //----yujing--end-----
        followButton.setOnClickListener(new View.OnClickListener() { //follow button
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
//                the user has already followed
                if (dwelling.getObservers().contains(user1)){
//                    remove user from observers
                    dwelling.detach(user1);
//                    tell users they have unfollowed and next time click on the button will follow again
                    followButton.setBackgroundColor(Color.rgb(0,0,128));
                    followButton.setText("Follow");
                    System.out.println(dwelling.getObservers());
                    Toast.makeText(getApplicationContext(),"you have unfollowed",Toast.LENGTH_SHORT).show();

                }
//                the user hasn't followed the building
                else{
//                    add user to the observers list
                    dwelling.attach(user1);
//                    tell users they have followed and next time click on the button will unfollow
                    followButton.setBackgroundColor(Color.rgb(128,128,128));
                    followButton.setText("Unfollow");
                    System.out.println(dwelling.getObservers());
                    Toast.makeText(getApplicationContext(),"you have followed",Toast.LENGTH_SHORT).show();
                }






            }
        });

        fireAlarmNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               publish notification to all observers
                dwelling.notifyAllObservers(ProfPageActivity.this);

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(ProfPageActivity.this,MapActivity.class);
                startActivity(backIntent);
            }
        });

    }
//    create a new channel, not sure should it be created in login activity or here
    private void createNotificationChannel() {
        CharSequence name = "NotificationChannel";
        String description = "This is a channel for notification, with this the notification can post in noti bar";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(ProfPageActivity.CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


}

