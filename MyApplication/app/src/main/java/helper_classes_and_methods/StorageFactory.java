package helper_classes_and_methods;

import android.content.Context;

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

