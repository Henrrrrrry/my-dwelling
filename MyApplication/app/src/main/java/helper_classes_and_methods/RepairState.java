package helper_classes_and_methods;

public class RepairState extends DwellingState{

    @Override
    void handle(Dwelling dwelling) {
        dwelling.notifyMaintainer();
    }
}
