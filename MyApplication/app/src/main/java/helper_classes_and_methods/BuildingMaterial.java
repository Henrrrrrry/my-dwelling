package helper_classes_and_methods;

import java.io.Serializable;

/**
 * Building material enum, design for calculate the current strength of building,
 * get the seismic rating, determine if a building is need a repair and repair it
 *
 * Author: Xinfei Li u7785177 enum development
 *         Hongyu Li: created the initial enums (STEEL,BRICK,CONCRETE,WOOD)
 *
 */

public enum BuildingMaterial implements Serializable {
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




