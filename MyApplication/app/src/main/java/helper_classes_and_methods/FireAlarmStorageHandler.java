package helper_classes_and_methods;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Map;

/**
 * Author: Xinfei Li
 * ID: u7785177
 * Create: 09/05/2024   7:00 pm
 * Last Edit: 10/05/2024   02:20 am
 */
public class FireAlarmStorageHandler implements StorageHandler {
    private SharedPreferences sharedPreferences;

    public FireAlarmStorageHandler(Context context) {
        this.sharedPreferences = context.getSharedPreferences("FireAlarmHistory", Context.MODE_PRIVATE);
    }

    @Override
    public void saveData(String dwelling, String message) {
        String timestamp = TimeUtil.getCurrentTimestamp();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(timestamp, "Dwelling: "+dwelling + " - Activity: " + message);
        editor.apply();
    }

    @Override
    public String loadData(String partialTime) {
        Map<String, ?> allEntries = sharedPreferences.getAll();
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith(partialTime)) { // Checks if the key (timestamp) starts with the provided partialTime
                result.append(entry.getKey()).append(" - ").append(entry.getValue().toString()).append("\n");
            }
        }
        return result.length() > 0 ? result.toString() : null;
    }
}


