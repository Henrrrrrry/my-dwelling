package helper_classes_and_methods;

import android.content.Context;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;


public class Dwelling implements  Subject {

//    record who followed this address
    private ArrayList<Observer> observers;
//    use state design pattern to define if this building need repair
    private DwellingState dwellingState;


    // Dwelling address
    private String address;


    private BuildingMaterial buildingMaterial; // Material used in building construction
    private LocalDate constructionDate; // Date the building was constructed
    private int seismicRating; // Seismic rating of the building  Seismic rating 1-10, the higher the better,should be calculated
    private LocalDate lastRepairDate; // Date of the last repair


    // Default as false, true means there's a fire, need to notify all users who followed to this building.
    private boolean fireAlarm;
//    define a user as maintainer for each building
    private User maintainer;
//    TODO: maybe need to modify this constructor when the building materials and state functions are determined
    public Dwelling(String address, LocalDate constructionDate, BuildingMaterial buildingMaterial,ArrayList<Observer> observers,User maintainer) {
        this.address = address;

        this.buildingMaterial = buildingMaterial;
        this.constructionDate = constructionDate;

        this.lastRepairDate = constructionDate; // Initialize last repair date to construction date

        this.fireAlarm=false;
        this.observers=observers ;
        this.dwellingState=new NormalState();
        this.maintainer =maintainer;
    }

    public Dwelling() {
    }




    /**
     * Method to determine if the building needs repairs
     * @return true for need repair and false for no need
     */
    public boolean needsRepair() {
        LocalDate currentDate = LocalDate.now(); // Get the current date
        int daysSinceLastRepair = (int) java.time.temporal.ChronoUnit.DAYS.between(this.lastRepairDate, currentDate); // Calculate days since last repair

        // If it's been more than 1 day since last repair, consider repair needed
        if (daysSinceLastRepair >= 1) {
            // Extract material properties
            double initialStrength = this.buildingMaterial.getInitialStrength(); // Using double for more accurate calculations
            double corrosionFactor = this.buildingMaterial.getCorrosionFactor();

            // Calculate the updated material strength based on degradation and repairs
            double strengthAfterCorrosion = initialStrength - corrosionFactor * daysSinceLastRepair;

            // Check if the updated seismic rating is below the repair threshold
            return strengthAfterCorrosion < this.buildingMaterial.getRepairThreshold();
        }
        return false; // No repair needed if less than 1 day since last repair
    }

    /**
     * Method to perform repairs on the building
     * Updating the lastRepairDate, set the strength to the initial strength
     * divide it by 10 to update the seismicRating
     */
    //
    public void repair() {
        LocalDate currentDate = LocalDate.now(); // Get the current date

        this.lastRepairDate = currentDate; // Update last repair date

        double strength = this.buildingMaterial.getInitialStrength();
        this.seismicRating = (int) (strength / 10); // Seismic rating is strength divided by 10
    }


    // Getter and Setter
    public  String getAddress() {
        return address;
    }

    /**
     * getter of SeismicRating
     * use strength = initialStrength - corrosionFactor * daysSinceLastRepair to get the strength
     * divide it by 10 to update the seismicRating
     * @return seismicRating
     */
    public int getSeismicRating() {
        LocalDate currentDate = LocalDate.now(); // Get the current date
        int daysSinceLastRepair = (int) java.time.temporal.ChronoUnit.DAYS.between(this.lastRepairDate, currentDate); // Calculate days since last repair

        double initialStrength = this.buildingMaterial.getInitialStrength(); // Using double for more accurate calculations
        double corrosionFactor = this.buildingMaterial.getCorrosionFactor();

        // Calculate the updated material strength based on degradation and repairs
        double strength = initialStrength - corrosionFactor * daysSinceLastRepair;

        return this.seismicRating = (int) (strength / 10); // Seismic rating is strength divided by 10
    }



    public LocalDate getLastRepairDate() {
        return lastRepairDate;
    }

    public void setLastRepairDate(LocalDate lastRepairDate) {
        this.lastRepairDate = lastRepairDate;
    }

    public LocalDate getConstructionDate() {
        return constructionDate;
    }


    public BuildingMaterial getBuildingMaterials() {
        return buildingMaterial;
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
