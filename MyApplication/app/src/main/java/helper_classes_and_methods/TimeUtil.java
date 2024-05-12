package helper_classes_and_methods;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * For save the current time in the log
 * Author: Xinfei Li
 * ID: u7785177
 * Create: 09/05/2024   7:00 pm
 * Last Edit: 10/05/2024   02:20 am
 */
public class TimeUtil {
    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}

