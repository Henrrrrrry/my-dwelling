package helper_classes_and_methods;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/**
 * Author: Xinfei Li
 * ID: u7785177
 * Create: 09/05/2024   7:00 pm
 * Last Edit: 10/05/2024   02:20 am
 */
public class LoginStorageHandler implements StorageHandler {
    private SharedPreferences sharedPreferences;

    public LoginStorageHandler(Context context) {
        this.sharedPreferences = context.getSharedPreferences("LoginHistory", Context.MODE_PRIVATE);
    }

    @Override
    public void saveData(String username, String message) {
        String timestamp = TimeUtil.getCurrentTimestamp();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(timestamp, "Username: "+username + " - Activity: " + message);
        editor.apply();
    }

////     Method to retrieve all log entries
//    public List<String> loadAllLogs() {
//        Map<String, ?> allEntries = sharedPreferences.getAll();
//        List<String> logs = new ArrayList<>();
//
//
//
//        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue().toString();
//            // Split the value by "-"
//            String[] parts = value.split(" - ");
//
//            logs.add(key + "\n- " + parts[1] +"\n" + parts[2]);
//        }
////        Collections.sort(logs);
//        return logs;
//    }
//     Method to retrieve all log entries
    public List<String> loadAllLogs() {
        Map<String, ?> allEntries = sharedPreferences.getAll();
        List<String> logs = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key=entry.getKey();
            String value=entry.getValue().toString();
            // Split the value by "-"
            String[] parts = value.split(" - ");
            logs.add(key + " \n- " + parts[0] + " \n- " + parts[1]);
        }
        Collections.sort(logs);
        return logs;
    }
}



