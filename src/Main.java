import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.util.Random;

/**
 * Main class for forestry simulation that manages multiple forests.
 * Allows operations such as adding, cutting, growing, and reaping trees,
 * as well as saving and loading forest data.
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Forest> forests = new ArrayList<>();
    private static Random random = new Random();

    /**
     * Main method to run the forestry simulation.
     *
     * @param args Command line arguments that should contain forest filenames to manage.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Forestry Simulation");
        System.out.println("----------------------------------");

        if (args.length == 0) {
            System.out.println("No forests to manage. Exiting program.");
            return;
        }

        int currentForestIndex = 0;
        try {
            Forest forest = loadForest(args[currentForestIndex]);
            forests.add(forest);
            System.out.println("Initializing from " + args[currentForestIndex]);
        } catch (Exception e) {
            System.out.println("Error initializing forest from file: " + args[currentForestIndex] + ".csv");
            e.printStackTrace();
        }

        while (true) {
            Forest currentForest = forests.get(currentForestIndex);
            System.out.print("(P)rint, (A)dd, (C)ut, (G)row, (R)eap, (S)ave, (L)oad, (N)ext, e(X)it : ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.isEmpty()) continue;
            char choice = input.charAt(0);
            switch (choice) {
                case 'P':
                    printForest(currentForest);
                    break;
                case 'A':
                    addRandomTree(currentForest);
                    break;
                case 'C':
                    cutTree(currentForest);
                    break;
                case 'G':
                    currentForest.growTreesInForest();
                    break;
                case 'R':
                    reapTrees(currentForest);
                    break;
                case 'S':
                    saveForest(currentForest);
                    break;
                case 'L':
                    loadNewForestPrompt(currentForestIndex);
                    break;
                case 'N':
                    currentForestIndex = handleNextForest(args, currentForestIndex);
                    break;
                case 'X':
                    System.out.println("Exiting the Forestry Simulation");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    /**
     * Prints detailed information about the trees within the specified forest.
     * Includes total and average height statistics.
     *
     * @param forest The forest to print details for.
     */
    private static void printForest(Forest forest) {
        ArrayList<Tree> trees = forest.getTrees();
        System.out.println("Forest name: " + forest.getName());
        double totalHeight = 0;
        for (Tree tree : trees) {
            totalHeight += tree.getHeight();
            System.out.printf("     %d %-7s %d %7.2f' %5.1f%%\n", tree.getSpecies(),
                    tree.getYearPlanted(), tree.getHeight(), tree.getGrowthRate());
        }
        double averageHeight = trees.size() > 0 ? totalHeight / trees.size() : 0;
        System.out.printf("There are %d trees, with an average height of %.2f\n", trees.size(), averageHeight);
    }

    /**
     * Adds a randomly generated tree to the specified forest.
     *
     * @param forest The forest to which a new tree will be added.
     */
    private static void addRandomTree(Forest forest) {
        Tree tree = new Tree(Tree.Species.values()[random.nextInt(Tree.Species.values().length)],
                2022, 10 + random.nextDouble() * 10, 10 + random.nextDouble() * 10);
        forest.add(tree);
    }

    /**
     * Prompts the user to select a tree to cut from the specified forest and removes it.
     *
     * @param forest The forest from which a tree will be cut.
     */
    private static void cutTree(Forest forest) {
        while (true) {
            System.out.print("Tree number to cut down: ");
            String input = scanner.nextLine();
            try {
                int index = Integer.parseInt(input);
                if (index >= 0 && index < forest.getTrees().size()) {
                    forest.cutTree(index);
                    return; // return after successful operation
                } else {
                    System.out.println("Tree number " + index + " does not exist");
                    return; // return to main prompt if tree number does not exist
                }
            } catch (NumberFormatException e) {
                System.out.println("That is not an integer");
                // continue the loop if input is not an integer
            }
        }
    }

    /**
     * Prompts user for a height threshold and reaps trees taller than this height.
     * Tall trees are replaced with new trees, and details of reaped trees are printed.
     *
     * @param forest The forest to reap tall trees from.
     */
    private static void reapTrees(Forest forest) {
        while (true) {
            System.out.print("Height to reap from: ");
            String input = scanner.nextLine();
            try {
                double height = Double.parseDouble(input);
                List<Tree[]> reapedTrees = forest.reapTrees(height);
                for (Tree[] reapedTree : reapedTrees) {
                    System.out.printf("Reaping the tall tree  %-7s %d %7.2f' %5.1f%%\n",
                            reapedTree[0].getSpecies(), reapedTree[0].getYearPlanted(), reapedTree[0].getHeight(), reapedTree[0].getGrowthRate());
                    System.out.printf("Replaced with new tree %-7s %d %7.2f' %5.1f%%\n",
                            reapedTree[1].getSpecies(), reapedTree[1].getYearPlanted(), reapedTree[1].getHeight(), reapedTree[1].getGrowthRate());
                }
                return; // return after successful operation
            } catch (NumberFormatException e) {
                System.out.println("That is not a number");
                // continue the loop if input is not a number
            }
        }
    }

    /**
     * Loads a forest from the specified CSV file.
     *
     * @param filename The filename (without extension) of the CSV file to load.
     * @return The loaded forest.
     * @throws IOException If there is an error reading the file.
     */
    private static Forest loadForest(String filename) throws IOException {
        Forest forest = new Forest(filename.replace(".csv", ""));
        try (BufferedReader reader = new BufferedReader(new FileReader("src/" + filename + ".csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Tree.Species species = Tree.Species.valueOf(parts[0].toUpperCase());
                int yearPlanted = Integer.parseInt(parts[1]);
                double height = Double.parseDouble(parts[2]);
                double growthRate = Double.parseDouble(parts[3]);
                forest.add(new Tree(species, yearPlanted, height, growthRate));
            }
        }
        return forest;
    }

    /**
     * Saves the specified forest to a file.
     *
     * @param forest The forest to save.
     */
    private static void saveForest(Forest forest) {
        String filename = forest.getName() + ".db";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(forest);
        } catch (IOException e) {
            System.out.println("Error saving the forest to file: " + e.getMessage());
        }
    }

    /**
     * Prompts user for a filename and attempts to load a new forest from file.
     * If successful, replaces the current forest in the list; otherwise, retains the old forest.
     *
     * @param currentForestIndex The index of the current forest being replaced.
     */
    private static void loadNewForestPrompt(int currentForestIndex) {
        System.out.print("Enter forest name: ");
        String filename = scanner.nextLine().trim();
        Forest newForest = loadNewForest(filename);
        if (newForest != null) {
            forests.set(currentForestIndex, newForest);
        } else {
            System.out.println("Old forest retained");
        }
    }

    /**
     * Handles switching to the next forest in the list based on command line arguments.
     *
     * @param args The array of command line arguments containing forest filenames.
     * @param currentForestIndex The current index of the forest being managed.
     * @return The new current index after switching forests.
     */
    private static int handleNextForest(String[] args, int currentForestIndex) {
        System.out.println("Moving to the next forest");
        int nextForestIndex = (currentForestIndex + 1) % args.length; // calculate the index of the next forest
        while (true) {
            if (nextForestIndex >= args.length) {
                System.out.println("No more forests to load. Returning to the first forest.");
                nextForestIndex = 0; // reset to the first forest if we've tried all forests
            }
            System.out.println("Initializing from " + args[nextForestIndex]);
            try {
                Forest nextForest = loadForest(args[nextForestIndex]);
                forests.add(nextForest);
                currentForestIndex = forests.size() - 1; // update currentForestIndex to the last index of the list
                return currentForestIndex; // break the loop if the forest is loaded successfully
            } catch (Exception e) {
                System.out.println("Error opening/reading " + args[nextForestIndex] + ".csv");
                nextForestIndex = (nextForestIndex + 1) % args.length; // move to the next forest
                if (nextForestIndex == currentForestIndex) { // if we've tried all forests and none could be loaded
                    System.out.println("No valid forests could be loaded. Exiting program.");
                    return -1; // indicate program termination
                }
            }
        }
    }

    /**
     * Loads a forest from a .db file.
     *
     * @param filename The name of the file to load the forest from.
     * @return The loaded forest or null if the load fails.
     */
    private static Forest loadNewForest(String filename) {
        Forest forest = null;
        try (ObjectInputStream in = new ObjectOutputStream(new FileInputStream(filename + ".db"))) {
            forest = (Forest) in.readObject();
            System.out.println("Forest '" + filename + "' has been loaded.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error opening/reading " + filename + ".db: " + e.getMessage());
        }
        return forest;
    }
}
