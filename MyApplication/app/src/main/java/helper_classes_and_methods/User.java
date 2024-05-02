package helper_classes_and_methods;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.ProfPageActivity;
import com.example.myapplication.R;


public class User implements Observer {
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

    @Override
    public void update(String location, Context context) {
        String msg = "Hi,Dear " + userID + ", there's an emergency at your followed address: " + location + ".";
        String title = "Fire Alarm";
        notifyUsers(msg, title, context);
//        System.out.println("helloworld1");
    }

    @Override
    public void maintainUpdate(String location, Context context) {
        String msg = "Hi,Dear " + userID + ", the building you are responsible for: " + location + " need repairs.";
        String title = "Repair Request";
        notifyUsers(msg, title, context);
//        System.out.println("helloworld2");
    }

    // implemented android notification api, followed the tut: https://developer.android.com/develop/ui/views/notifications/build-notification
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
}


