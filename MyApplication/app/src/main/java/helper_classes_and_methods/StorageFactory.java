package helper_classes_and_methods;

import android.content.Context;
/**
 * Author: Xinfei Li
 * ID: u7785177
 * Create: 09/05/2024   7:00 pm
 * Last Edit: 10/05/2024   02:20 am
 */
public class StorageFactory {
    public enum HandlerType {
        LOGIN, FIRE_ALARM
    }

    public static StorageHandler getStorageHandler(Context context, HandlerType type) {
        switch (type) {
            case LOGIN:
                return new LoginStorageHandler(context);
            case FIRE_ALARM:
                return new FireAlarmStorageHandler(context);
            default:
                throw new IllegalArgumentException("Unknown Handler Type");
        }
    }
}

