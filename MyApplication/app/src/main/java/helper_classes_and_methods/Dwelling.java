package helper_classes_and_methods;

/**
 * @Author: LEE
 * @Create: 9:44 pm on 18/04/2024
 */
public class Dwelling {
    // Static instance of the class
    private static Dwelling instance;

    // Dwelling address
    private static String address;
    // Seismic rating 1-10, the higher the better
    private int seismicRating;
    // Boolean value representing if a dwelling needs repair
    private boolean needRepair;
    // The year of building construction
    private static int yearOfConstruction;
    // Life of building
    private int buildingLife;
    // Materials of building
    private static String buildingMaterials;
    // Default as false, if true means there's a fire, need to notify all users who followed to this building.
    private boolean fireAlarm;

    // Private constructor to prevent outside instantiation
    private Dwelling(String address, int yearOfConstruction, int buildingLife, String buildingMaterials) {
        Dwelling.address = address;
        Dwelling.yearOfConstruction = yearOfConstruction;
        this.buildingLife = buildingLife;
        Dwelling.buildingMaterials = buildingMaterials;
        this.needRepair = false;
        this.fireAlarm = false;
    }

    // Default private constructor
    private Dwelling() {
    }

    // Public static method to get the instance of the class
    public static Dwelling getInstance(String address, int yearOfConstruction, int buildingLife, String buildingMaterials) {
        if (instance == null) {
            instance = new Dwelling(address, yearOfConstruction, buildingLife, buildingMaterials);
        }
        return instance;
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
