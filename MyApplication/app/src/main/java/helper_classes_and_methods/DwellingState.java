package helper_classes_and_methods;

import android.content.Context;

import java.io.Serializable;

public abstract class DwellingState implements Serializable {

    abstract void handle(Dwelling dwelling, Context context);




}
