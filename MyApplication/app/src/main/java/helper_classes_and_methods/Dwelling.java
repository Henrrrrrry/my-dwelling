package helper_classes_and_methods;

import android.content.Context;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;


public class Dwelling implements  Subject, Serializable {

    public static class Location implements Serializable{
        double lat;
        double lng;

        public Location() {
        }

        public Location(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }

//    record who followed this address
    private ArrayList<Observer> observers;
//    use state design pattern to define if this building need repair
    private DwellingState dwellingState;
//    location of the dwelling ,represented by[latitude, longitude]
    private Location location;


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
    public Dwelling(String address, LocalDate constructionDate, BuildingMaterial buildingMaterial,ArrayList<Observer> observers,User maintainer,Location location) {
        this.address = address;

        this.buildingMaterial = buildingMaterial;
        this.constructionDate = constructionDate;

        this.lastRepairDate = constructionDate; // Initialize last repair date to construction date

        this.fireAlarm=false;
        this.observers=observers ;
        this.dwellingState=new NormalState();
        this.maintainer =maintainer;
        this.location=location;
    }

    public Dwelling() {
    }




    /**
     * Method to determine if the building needs repairs
     * @return true for need repair and false for no need
     */
    public void needsRepair() {
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
            if(strengthAfterCorrosion < this.buildingMaterial.getRepairThreshold())
                this.dwellingState = new RepairState();
        }
        this.dwellingState = new NormalState(); // No repair needed if less than 1 day since last repair
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
        strength /=10;
        if (strength>8) strength=8;
        if (strength<3) strength=3;
        this.seismicRating = (int) (strength); // Seismic rating is strength divided by 10
        this.dwellingState = new NormalState();
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
        strength /=10;
        if (strength>8) strength=8;
        if (strength<3) strength=3;

        return this.seismicRating = (int) (strength); // Seismic rating is strength divided by 10
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public DwellingState getDwellingState() {
        return dwellingState;
    }

    public BuildingMaterial getBuildingMaterial() {
        return buildingMaterial;
    }

    public User getMaintainer() {
        return maintainer;
    }
}
