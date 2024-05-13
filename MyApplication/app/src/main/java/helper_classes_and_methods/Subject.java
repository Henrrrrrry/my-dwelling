package helper_classes_and_methods;

import android.content.Context;

/**
 * Author: Hongyu Li u7776180
 * Description: interface for observer design pattern, dwelling will implement this one , so it could be the subject
 */

public interface Subject {
    public void attach(Observer observer);
    public void detach(Observer observer);
    public void notifyAllObservers(Context context);
    public  void notifyMaintainer(Context context);

}
