package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import helper_classes_and_methods.RepairState;
import helper_classes_and_methods.User;
import helper_classes_and_methods.StorageFactory;
import helper_classes_and_methods.StorageHandler;
//import android.widget.ImageView;

public class ProfPageActivity extends BaseActivity {
    //                test case create channel ID
    public static final String CHANNEL_ID = "uniqueChannelId";
//     ArrayList<Observer> observers1= new ArrayList<>();
    private StorageHandler storageHandler;

    //private ImageView buildingImage;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        createNotificationChannel();
        // Initialize StorageHandler
        storageHandler = StorageFactory.getStorageHandler(this, StorageFactory.HandlerType.FIRE_ALARM);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_page);

        ImageView buildingImage = findViewById(R.id.buildingImage);
//        buildingImage.setImageResource(R.drawable.img_default_building);

        Button followButton = findViewById(R.id.followButton);
        Button fireAlarmNoti = findViewById(R.id.fireAlarmButton);


        User user = (User) getIntent().getExtras().getSerializable("User");
        if (!user.isMaintainer()) fireAlarmNoti.setEnabled(false);
        Button backButton = findViewById(R.id.backButton);
        TextView buildingInfo = findViewById(R.id.buildingInfo);
        TextView buildingTitle= findViewById(R.id.address);
        //address

        Dwelling searchDwelling = (Dwelling) getIntent().getExtras().getSerializable("Dwelling");

        Button repairButton = findViewById(R.id.repairButton);
        if (!(searchDwelling.needsRepair())){
            repairButton.setEnabled(false);
            repairButton.setText("Don't need Repair");
        }


        String infoText = //"Address: " + searchDwelling.getAddress() + "\n" +
                "Seismic Rating: "+searchDwelling.getSeismicRating() + "\n" +
                "Year of construction: "+searchDwelling.getConstructionDate() + "\n" +
                "Materials: "+searchDwelling.getBuildingMaterial();
        buildingInfo.setText(infoText);
        buildingTitle.setText("Addr:"+searchDwelling.getAddress());

        if(searchDwelling.getBuildingMaterial()==BuildingMaterial.BRICK){
            buildingImage.setImageResource(R.drawable.brick);
        } else if (searchDwelling.getBuildingMaterial()==BuildingMaterial.STEEL) {
            buildingImage.setImageResource(R.drawable.steel);
        }else if (searchDwelling.getBuildingMaterial()==BuildingMaterial.CONCRETE) {
            buildingImage.setImageResource(R.drawable.concrete);
        }else if (searchDwelling.getBuildingMaterial()==BuildingMaterial.WOOD) {
            buildingImage.setImageResource(R.drawable.wood);
        }else{
            buildingImage.setImageResource(R.drawable.img_default_building);
        }

            if (searchDwelling.getObservers().contains(user)) {
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
                if (searchDwelling.getObservers().contains(user)){
//                    remove user from observers
                    searchDwelling.detach(user);
//                    tell users they have unfollowed and next time click on the button will follow again
                    followButton.setBackgroundColor(Color.rgb(0,0,128));
                    followButton.setText("Follow");
//                    System.out.println(searchDwelling.getObservers());
                    Toast.makeText(getApplicationContext(),"you have unfollowed",Toast.LENGTH_SHORT).show();

                }
//                the user hasn't followed the building
                else{
//                    add user to the observers list
                    searchDwelling.attach(user);
//                    tell users they have followed and next time click on the button will unfollow
                    followButton.setBackgroundColor(Color.rgb(128,128,128));
                    followButton.setText("Unfollow");
//                    System.out.println(searchDwelling.getObservers());
                    Toast.makeText(getApplicationContext(),"you have followed",Toast.LENGTH_SHORT).show();
                }






            }
        });

        fireAlarmNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log fire alarm event
                storageHandler.saveData(searchDwelling.getAddress(), "fire alarm triggered");
//               publish notification to all observers
                searchDwelling.notifyAllObservers(ProfPageActivity.this);

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        repairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Thank you for your maintenance",Toast.LENGTH_SHORT).show();
                repairButton.setEnabled(false);

                Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                // start animation
                repairButton.startAnimation(fadeOut);

                // set animation
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        repairButton.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

    }
//    create a new channel
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

