package helper_classes_and_methods;

import android.content.Context;

/**
 * Author: Hongyu Li u7776180
 * Description: observer design pattern
 */
public interface Observer {
    /**
     * Description: update fire alarm notification for followed users
     * @param msg the msg users will get
     * @param context  the current context
     */
    public void update(String msg, Context context);

    /**
     * Description: update if they need repair notification for maintainers, designed for service provider (server)
     * @param msg the msg maintainers will get
     * @param context the current context
     */
    public void maintainUpdate(String msg, Context context);
}
