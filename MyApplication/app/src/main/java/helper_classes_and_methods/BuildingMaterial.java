package helper_classes_and_methods;

/**
 * Author: Xinfei Li
 * ID: u7785177
 * Create: 29/04/2024   3:00 pm
 * Last Edit: 29/04/2024   11:10 pm
 */

public enum BuildingMaterial {
    STEEL(150, 0.01, 75),  // initial strength, corrosion factor, repair threshold, repair rate
    BRICK(120, 0.006, 50),
    CONCRETE(140, 0.008, 65),
    WOOD(90, 0.005, 30);

    private final int initialStrength;   // Base strength when new
    private final double corrosionFactor; // Daily reduction in strength
    private final int repairThreshold;    // Strength threshold below which repairs are needed



    // Constructor for enum to set material properties
    BuildingMaterial(int initialStrength, double corrosionFactor, int repairThreshold) {
        this.initialStrength = initialStrength;
        this.corrosionFactor = corrosionFactor;
        this.repairThreshold = repairThreshold;
    }

    // Getter for the initial strength
    public int getInitialStrength() {
        return initialStrength;
    }

    // Getter for the corrosion factor
    public double getCorrosionFactor() {
        return corrosionFactor;
    }

    // Getter for the repair threshold
    public int getRepairThreshold() {
        return repairThreshold;
    }

}




