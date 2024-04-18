/**
 * @Author: LEE
 * @Create: 9:44 pm on 18/04/2024
 */
public class Dwelling {
    //    Dwelling address
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

    // maybe we will figure out how to calculate Seismic rating
    public Dwelling(String address, int yearOfConstruction, int buildingLife, String buildingMaterials) {
        this.address = address;
        this.yearOfConstruction = yearOfConstruction;
        this.buildingLife = buildingLife;
        this.buildingMaterials = buildingMaterials;
        this.needRepair=false;
        this.fireAlarm=false;
    }
}