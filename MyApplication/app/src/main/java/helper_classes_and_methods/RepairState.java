package helper_classes_and_methods;

import android.content.Context;

import java.io.Serializable;

/**
 * Author: Hongyu Li u7776180
 * Description: state that the building need to be repaired
 */
public class RepairState extends DwellingState implements Serializable {

    @Override
    void handle(Dwelling dwelling, Context context) {
        dwelling.notifyMaintainer(context);
    }
}
