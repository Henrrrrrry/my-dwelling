package helper_classes_and_methods;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginStorageHandler implements StorageHandler {
    private SharedPreferences sharedPreferences;

    public LoginStorageHandler(Context context) {
        this.sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public void saveData(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public String loadData(String key) {
        return sharedPreferences.getString(key, null);
    }
}

