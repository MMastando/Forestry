import java.io.Serializable;

/**
 * Represents a tree with specific characteristics such as species,
 * year planted, height, and growth rate. This class is serializable
 * to allow its instances to be saved to a file or transferred over the network.
 */
public class Tree implements Serializable {
    // Enum for specifying the species of a tree.
    public enum Species { BIRCH, MAPLE, FIR }
    
    // Serial version UID for serialization.
    private static final long serialVersionUID = 1L;
    
    // The species of the tree.
    private Species species;
    
    // The year in which the tree was planted.
    private int yearPlanted;
    
    // The current height of the tree in meters.
    private double height;
    
    // The annual growth rate of the tree as a percentage.
    private double growthRate;

    /**
     * Constructs a new Tree object with specified species, year planted, height, and growth rate.
     *
     * @param species     The species of the tree.
     * @param yearPlanted The year the tree was planted.
     * @param height      The current height of the tree.
     * @param growthRate  The growth rate of the tree per year as a percentage.
     */
    public Tree(Species species, int yearPlanted, double height, double growthRate) {
        this.species = species;
        this.yearPlanted = yearPlanted;
        this.height = height;
        this.growthRate = growthRate;
    }

    /**
     * Simulates the growth of the tree for one year.
     * The height of the tree is increased based on the growth rate.
     */
    public void growForOneYear() {
        // Increase the height by the growth rate percentage.
        height += height * growthRate / 100;
    }

    // Getters for the tree properties:

    /**
     * Returns the species of the tree.
     *
     * @return The tree species.
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * Returns the year the tree was planted.
     *
     * @return The year planted.
     */
    public int getYearPlanted() {
        return yearPlanted;
    }

    /**
     * Returns the current height of the tree.
     *
     * @return The height of the tree.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the growth rate of the tree.
     *
     * @return The growth rate as a percentage.
     */
    public double getGrowthRate() {
        return growthRate;
    }
}
