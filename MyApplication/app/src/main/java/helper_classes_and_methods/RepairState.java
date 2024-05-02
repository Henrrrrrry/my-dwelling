package helper_classes_and_methods;

import android.content.Context;

public class RepairState extends DwellingState{

    @Override
    void handle(Dwelling dwelling, Context context) {
        dwelling.notifyMaintainer(context);
    }
}
