import java.io.Serializable;

/**
 * Represents a tree with a species type, year planted, height, and growth rate.
 * This class implements Serializable to allow objects to be saved to a file
 */
public class Tree implements Serializable {
    // Enum for specifying species of trees.
    public enum Species { BIRCH, MAPLE, FIR }

    // Serial version UID for serialization.
    private static final long serialVersionUID = 1L;

    // The species of the tree.
    private Species species;

    // The year the tree was planted.
    private int yearPlanted;

    // Current height of the tree in meters.
    private double height;

    // Annual growth rate as a percentage.
    private double growthRate;

    /**
     * Constructs a new Tree object with the specified species, year planted, initial height, and growth rate.
     *
     * @param species the species of the tree
     * @param yearPlanted the year the tree was planted
     * @param height the initial height of the tree
     * @param growthRate the annual growth rate of the tree
     */
    public Tree(Species species, int yearPlanted, double height, double growthRate) {
        this.species = species;
        this.yearPlanted = yearPlanted;
        this.height = height;
        this.growthRate = growthRate;
    }

    /**
     * Returns the species of the tree.
     *
     * @return the species of the tree
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * Returns the year the tree was planted.
     *
     * @return the year the tree was planted
     */
    public int getYearPlanted() {
        return yearPlanted;
    }

    /**
     * Returns the current height of the tree.
     *
     * @return the current height of the tree in meters
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the annual growth rate of the tree.
     *
     * @return the growth rate as a percentage
     */
    public double getGrowthRate() {
        return growthRate;
    }

    /**
     * Simulates the growth of the tree for one year.
     * The height of the tree is increased based on the growth rate.
     */
    public void growYear() {
        // Increase the tree's height by its growth rate percentage.
        height += height * growthRate / 100;
    }
}
