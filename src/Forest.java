import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a forest containing trees.
 */
public class Forest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<Tree> trees;

    /**
     * Constructs a forest with a given name.
     *
     * @param name The name of the forest.
     */
    public Forest(String name) {
        this.name = name;
        this.trees = new ArrayList<>();
    }

    /**
     * Adds a tree to the forest.
     *
     * @param tree The tree to be added.
     */
    public void addTree(Tree tree) {
        trees.add(tree);
    }

    /**
     * Cuts down a tree from the forest.
     *
     * @param index The index of the tree to be cut down.
     */
    public void cutDownTree(int index) {
        if (index >= 0 && index < trees.size()) {
            trees.remove(index);
        }
    }

    /**
     * Simulates one year of growth for all trees in the forest.
     */
    public void growForest() {
        for (Tree tree : trees) {
            tree.growForOneYear();
        }
    }

    /**
     * Reaps trees from the forest based on a given height threshold.
     *
     * @param height The height threshold for reaping trees.
     * @return A list containing arrays of old and new trees reaped from the forest.
     */
    public List<Tree[]> reapTrees(double height) {
        List<Tree[]> reapedTrees = new ArrayList<>();
        for (int i = 0; i < trees.size(); i++) {
            if (trees.get(i).getHeight() > height) {
                Tree oldTree = trees.get(i);
                // Create a new tree with random attributes
                Tree newTree = new Tree(
                        Tree.Species.values()[new Random().nextInt(Tree.Species.values().length)],
                        2022, 10 + new Random().nextDouble() * 10, 10 + new Random().nextDouble() * 10);
                trees.set(i, newTree); // Replace the old tree with the new one
                reapedTrees.add(new Tree[]{oldTree, newTree}); // Add both trees to the list
            }
        }
        return reapedTrees;
    }

    /**
     * Gets the name of the forest.
     *
     * @return The name of the forest.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the list of trees in the forest.
     *
     * @return The list of trees.
     */
    public ArrayList<Tree> getTrees() {
        return trees;
    }
}
