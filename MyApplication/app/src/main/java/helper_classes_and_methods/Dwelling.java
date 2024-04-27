package helper_classes_and_methods;

/**
 * @Author: LEE
 * @Create: 9:44 pm on 18/04/2024
 */
public class Dwelling {
    //    helperclassesmethods.Dwelling address
    static String address;
    //    Seismic rating 1-10, the higher the better
    int seismicRating;
    //    Boolean value representing if a dwelling need repair
    boolean needRepair;
    //    The year of building construction
    static int yearOfConstruction;
    //    Life of building
    int buildingLife;
    //    Materials of building
    static String buildingMaterials;
    //    Default as false, if true means there's a fire, need to notify all users who followed to this building.
    boolean fireAlarm;

    //  figure out a function to calculate Seismic rating & needRepair
    public Dwelling(String address, int yearOfConstruction, int buildingLife, String buildingMaterials) {
        this.address = address;
        this.yearOfConstruction = yearOfConstruction;
        this.buildingLife = buildingLife;
        this.buildingMaterials = buildingMaterials;
        this.needRepair=false;
        this.fireAlarm=false;
    }

    public Dwelling() {
    }



//  delete useless getter/setter, maybe need to change public to private
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

    public boolean isNeedRepair() {
        return needRepair;
    }

    public void setNeedRepair(boolean needRepair) {
        this.needRepair = needRepair;
    }

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
    }
}