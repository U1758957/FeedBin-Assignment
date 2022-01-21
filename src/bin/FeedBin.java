package bin;

/**
 *
 * FeedBin.java
 * A R Grundy
 *
 * Modified by Gary Allen,
 * Modified by Zac King (U1758957)
 *
 * Last update January 2022
 *
 * A class for modelling the state and operation of an "animal feed bin"
 * as used in the factory production of animal feedstuffs
 *
 */
public class FeedBin {

    // FeedBin instance variables
    private int binNumber;
    private String productName;
    private double maxVolume;
    private double currentVolume;

    /**
     * FeedBin Constructor
     * @param binNumber the bin identifier
     * @param productName the product in the bin
     */
    public FeedBin(int binNumber, String productName) {

        this.binNumber = binNumber; // Bin identifier
        this.productName = productName; // Product in bin

        this.maxVolume = 40.0d; // Maximum capacity in cubic meters
        this.currentVolume = 0.0d; // Bin starts in  the empty state

    }

    /**
     * Used to change the product assigned to the bin.
     * Can only do this if the bin is empty, i.e., use flush() method first
     * @param newName the new product name to be used in the bin
     * @return true if a new product name was added via the current volume being empty, false otherwise
     */
    public boolean setProductName(String newName) {

        if (currentVolume == 0.0d) {
            this.productName = newName;
            return true;
        } else return false;

    }

    /**
     * Used to completely empty the bin
     */
    public void flush() {
        this.currentVolume = 0.0d;
    }

    /**
     * Can only add if there is sufficient room
     * @param volume the volume of product to be added in cubic meters
     * @return true if the volume was added due to sufficient room, false otherwise
     */
    public boolean addProduct(double volume) {

        if (maxVolume >= (currentVolume + volume)) {
            this.currentVolume += volume;
            return true;
        } else return false;

    }

}
