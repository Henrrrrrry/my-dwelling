package helper_classes_and_methods;

import android.content.Context;

public interface Observer {
    public void update(String msg, Context context);
    public void maintainUpdate(String msg, Context context);
}
