import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Forest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<Tree> trees;

    public Forest(String name) {
        this.name = name;
        this.trees = new ArrayList<>();
    }

    public void addTree(Tree tree) {
        trees.add(tree);
    }

    public void cutDownTree(int index) {
        if (index >= 0 && index < trees.size()) {
            trees.remove(index);
        }
    }

    public void growForest() {
        for (Tree tree : trees) {
            tree.growForOneYear();
        }
    }

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

    public String getName() {
        return name;
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }
}