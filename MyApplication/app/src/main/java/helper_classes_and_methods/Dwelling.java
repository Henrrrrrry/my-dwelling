package helper_classes_and_methods;

import java.util.ArrayList;


public class Dwelling implements  Subject {

//    record who followed this address
    private ArrayList<Observer> observers;
//    use state design pattern to define if this building need repair
    private DwellingState dwellingState;


    // Dwelling address
    private static String address;
    // Seismic rating 1-10, the higher the better,should be calculated
    private int seismicRating;

//    this part of code is commented cuz we are using state to represent
    // Boolean value representing if a dwelling needs repair
//    private boolean needRepair;

    // The year of building construction
    private static int yearOfConstruction;
    // Life of building
    private int buildingLife;
    // Materials of building
    private static String buildingMaterials;
    // Default as false, if true means there's a fire, need to notify all users who followed to this building.
    private boolean fireAlarm;
    private User maintainer;

    //  figure out a function to calculate Seismic rating & needRepair
    public Dwelling(String address, int yearOfConstruction, int buildingLife, String buildingMaterials,ArrayList<Observer> observers,User maintainer) {
        Dwelling.address = address;
        Dwelling.yearOfConstruction = yearOfConstruction;
        this.buildingLife = buildingLife;
        Dwelling.buildingMaterials = buildingMaterials;
//        this.needRepair=false;
        this.fireAlarm=false;
        this.observers=observers ;
        this.dwellingState=new NormalState();
        this.maintainer =maintainer;
    }

    public Dwelling() {
    }



    // Accessors and mutators
    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        Dwelling.address = address;
    }

    public int getSeismicRating() {
        return seismicRating;
    }

    public void setSeismicRating(int seismicRating) {
        this.seismicRating = seismicRating;
    }

//    public boolean isNeedRepair() {
//        return needRepair;
//    }
//
//    public void setNeedRepair(boolean needRepair) {
//        this.needRepair = needRepair;
//    }

    public static int getYearOfConstruction() {
        return yearOfConstruction;
    }

    public static void setYearOfConstruction(int yearOfConstruction) {
        Dwelling.yearOfConstruction = yearOfConstruction;
    }

    public int getBuildingLife() {
        return buildingLife;
    }

    public void setBuildingLife(int buildingLife) {
        this.buildingLife = buildingLife;
    }

    public static String getBuildingMaterials() {
        return buildingMaterials;
    }

    public static void setBuildingMaterials(String buildingMaterials) {
        Dwelling.buildingMaterials = buildingMaterials;
    }

    public boolean isFireAlarm() {
        return fireAlarm;
    }

    public void setFireAlarm(boolean fireAlarm) {
        this.fireAlarm = fireAlarm;
        if (fireAlarm)  notifyAllObservers();
    }



    public ArrayList<Observer> getObservers() {
        return observers;
    }

//    for now it's used for create test cases
    public void setObservers(ArrayList<Observer> observers) {
        this.observers = observers;
    }

//    TODO: seems like not correct
    public void setDwellingState(DwellingState newDwellingState) {
        this.dwellingState = newDwellingState;
        this.dwellingState.handle(this);
    }
//    add current user to the observers list when current user click follow
    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers() {
        for (Observer observer:observers) observer.update(getAddress());
    }

    @Override
    public void notifyMaintainer() {
        maintainer.maintainUpdate(getAddress());
    }
}
