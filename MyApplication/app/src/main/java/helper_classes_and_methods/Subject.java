package helper_classes_and_methods;

import android.content.Context;

public interface Subject {
    public void attach(Observer observer);
    public void detach(Observer observer);
    public void notifyAllObservers(Context context);
    public  void notifyMaintainer(Context context);

}
