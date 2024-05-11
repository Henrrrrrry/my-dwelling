package helper_classes_and_methods;

import android.content.Context;

import java.io.Serializable;

/**
 * Author: Hongyu Li: create an abstract class, used for State pattern
 *         Xinrui Zhang: implements serializable
 */

public abstract class DwellingState implements Serializable {

    abstract void handle(Dwelling dwelling, Context context);




}
