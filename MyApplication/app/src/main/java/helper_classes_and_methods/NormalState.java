package helper_classes_and_methods;

import android.content.Context;

import java.io.Serializable;

/**
 * Author: Hongyu Li u7776180: normal state, do nothing when the dwelling state is normal
 *         Xinrui Zhang: implements serializable
 */

public class NormalState extends DwellingState implements Serializable {


    @Override
    void handle(Dwelling dwelling, Context context) {


    }
}
