package helper_classes_and_methods;

import android.content.Context;

import java.util.ArrayList;


public class Dwelling implements  Subject {

//    record who followed this address
    private ArrayList<Observer> observers;
//    use state design pattern to define if this building need repair
    private DwellingState dwellingState;


    // Dwelling address
    private String address;
    // Seismic rating 1-10, the higher the better,should be calculated
    private int seismicRating;
    // The year of building construction
    private int yearOfConstruction;
    // Life of building
    private int buildingLife;
//   Materials of building by using Enum
    private Enum<BuildingMaterial> buildingMaterials;
    // Default as false, true means there's a fire, need to notify all users who followed to this building.
    private boolean fireAlarm;
//    define a user as maintainer for each building
    private User maintainer;
//    TODO: maybe need to modify this constructor when the building materials and state functions are determined
    public Dwelling(String address, int yearOfConstruction, int buildingLife, Enum<BuildingMaterial> buildingMaterials,ArrayList<Observer> observers,User maintainer) {
        this.address = address;
        this.yearOfConstruction = yearOfConstruction;
        this.buildingLife = buildingLife;
        this.buildingMaterials = buildingMaterials;
        this.fireAlarm=false;
        this.observers=observers ;
        this.dwellingState=new NormalState();
        this.maintainer =maintainer;
    }

    public Dwelling() {
    }



    // Getter and Setter
    public  String getAddress() {
        return address;
    }


    public int getSeismicRating() {
        return seismicRating;
    }

    public void setSeismicRating(int seismicRating) {
        this.seismicRating = seismicRating;
    }

    public int getYearOfConstruction() {
        return yearOfConstruction;
    }


    public int getBuildingLife() {
        return buildingLife;
    }

    public void setBuildingLife(int buildingLife) {
        this.buildingLife = buildingLife;
    }

    public Enum<BuildingMaterial> getBuildingMaterials() {
        return buildingMaterials;
    }


    public boolean isFireAlarm() {
        return fireAlarm;
    }

    public void setFireAlarm(boolean fireAlarm) {
        this.fireAlarm = fireAlarm;
    }



    public ArrayList<Observer> getObservers() {
        return observers;
    }
    public void setObservers(ArrayList<Observer> observers) {
        this.observers = observers;
    }

//    TODO: seems like not correct
    public void setDwellingState(DwellingState newDwellingState,Context context) {
        this.dwellingState = newDwellingState;
        this.dwellingState.handle(this,context);
    }
//    add current user to the observers list when current user click follow
    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }
//    remove user from observers if the user click unfollow(the button twice)
    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }
//    notify all users there's a fire alarm
    @Override
    public void notifyAllObservers(Context context) {
        for (Observer observer:observers) observer.update(getAddress(),context);
    }
//    notify maintainer of the building it need repairs
    @Override
    public void notifyMaintainer(Context context) {
        maintainer.maintainUpdate(getAddress(), context);
    }
}
