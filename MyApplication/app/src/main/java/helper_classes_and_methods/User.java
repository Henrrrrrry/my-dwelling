package helper_classes_and_methods;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.ProfPageActivity;
import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * Author: Hongyu Li: created the skeleton
 */

public class User implements Observer, Serializable {
    private String username;
    private String password;
    private boolean isMaintainer;
    private String userID;

    // Constructor


    public User(String username, String password, boolean isMaintainer, String userID) {
        this.username = username;
        this.password = password;
        this.isMaintainer = isMaintainer;
        this.userID = userID;
    }
    public User(){}
    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isMaintainer() {
        return isMaintainer;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMaintainer(boolean isMaintainer) {
        this.isMaintainer = isMaintainer;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", isMaintainer=" + isMaintainer +
                '}';
    }

    /**
     * Auhtor: Hongyu Li
     * Description: override the update method
     * @param location the location msg users will get
     * @param context  the current context
     */
    @Override
    public void update(String location, Context context) {
        String msg = "Hi,Dear " + userID + ", there's an emergency at your followed address: " + location + ".";
        String title = "Fire Alarm";
        notifyUsers(msg, title, context);
    }

    /**
     * Auhtor: Hongyu Li
     * Description: override method
     * @param location the location msg maintainers will get
     * @param context the current context
     */
    @Override
    public void maintainUpdate(String location, Context context) {
        String msg = "Hi,Dear " + userID + ", the building you are responsible for: " + location + " need repairs.";
        String title = "Repair Request";
        notifyUsers(msg, title, context);
    }

    /**
     * Author: Hongyu Li
     * Descripotion: implemented android notification api, followed the tut: <a href="https://developer.android.com/develop/ui/views/notifications/build-notification">...</a>
     * @param msg the msg maintainers will get
     * @param title firealarm or need repair
     * @param context the current context
     */

    public void notifyUsers(String msg, String title, Context context) {
//        This intent leads to profilePageActivity, before starting clear the previous activity
        Intent tapNotificationIntent = new Intent(context, ProfPageActivity.class);
//        give permission to other apps, to make sure when clicking on notification, can lead to this page
        tapNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, tapNotificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ProfPageActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.redalarm)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        Notification notification = builder.build();
        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationCompat.notify(0, notification);
    }
    /**
     * Author: Xinfei Li
     * ID: u7785177
     * Create: 29/04/2024   6:00 pm
     * Last Edit: 30/04/2024   12:00 pm
     */
    // Method to read user data from a CSV file and validate credentials
    public boolean validateUserCredentials(String inputUsername, String inputPassword, AssetManager assetManager) {
        BufferedReader bufferedReader = null;
        boolean isValidUser = false;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open("loginDetails.csv"), StandardCharsets.UTF_8));

            String line = bufferedReader.readLine(); // Read the header line and ignore it
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 4 && tokens[0].equals(inputUsername) && tokens[1].equals(inputPassword)) {
                    this.username = tokens[0];
                    this.password = tokens[1];
                    this.isMaintainer = Boolean.parseBoolean(tokens[2]);
                    this.userID = tokens[3];
                    isValidUser = true;
                    break;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isValidUser;
    }




}


