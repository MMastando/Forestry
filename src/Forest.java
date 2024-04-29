import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a forest containing a collection of trees. This class is capable
 * of adding trees, simulating tree growth, and managing tree removal based on
 * specific conditions.
 */
public class Forest implements Serializable {
    // Serial version UID for serialization.
    private static final long serialVersionUID = 1L;

    // Name of the forest.
    private String name;

    // List to store the trees in the forest.
    private ArrayList<Tree> trees;

    /**
     * Constructs a new Forest object with the specified name.
     *
     * @param name the name of the forest
     */
    public Forest(String name) {
        this.name = name;
        this.trees = new ArrayList<>();
    }

    /**
     * Returns the name of the forest.
     *
     * @return the name of the forest
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of trees in the forest.
     *
     * @return the list of trees
     */
    public ArrayList<Tree> getTrees() {
        return trees;
    }

    /**
     * Adds a new tree to the forest.
     *
     * @param tree the tree to add
     */
    public void add(Tree tree) {
        trees.add(tree);
    }

    /**
     * Removes a tree from the forest at the specified index, if the index is valid.
     *
     * @param index the index of the tree to be removed
     */
    public void cutTree(int index) {
        if (index >= 0 && index < trees.size()) {
            trees.remove(index);
        }
    }

    /**
     * Simulates the growth of each tree in the forest for one year.
     */
    public void growTreesInForest() {
        for (Tree tree : trees) {
            tree.growYear();
        }
    }

    /**
     * Reaps trees that exceed a certain height, replacing them with new trees,
     * and returns a list of arrays containing the old and new trees.
     *
     * @param height the height threshold for reaping trees
     * @return a list of Tree arrays where each array contains an old tree and a new replacement tree
     */
    public List<Tree[]> reapTrees(double height) {
        List<Tree[]> reapedTrees = new ArrayList<>();
        for (int i = 0; i < trees.size(); i++) {
            if (trees.get(i).getHeight() > height) {
                Tree oldTree = trees.get(i);
                Tree newTree = new Tree(Tree.Species.values()[new Random().nextInt(Tree.Species.values().length)],
                        2022, 10 + new Random().nextDouble() * 10, 10 + new Random().nextDouble() * 10);
                trees.set(i, newTree);
                reapedTrees.add(new Tree[]{oldTree, newTree});
            }
        }
        return reapedTrees;
    }
}
