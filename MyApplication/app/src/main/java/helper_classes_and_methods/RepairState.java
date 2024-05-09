package helper_classes_and_methods;

import android.content.Context;

import java.io.Serializable;

public class RepairState extends DwellingState implements Serializable {

    @Override
    void handle(Dwelling dwelling, Context context) {
        dwelling.notifyMaintainer(context);
    }
}
