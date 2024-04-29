
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.util.Random;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Forest> forests = new ArrayList<>();
    private static Random random = new Random();

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
            System.out.println();
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
                    System.out.println();
                    printForest(currentForest);
                    System.out.println();
                    break;
                case 'A':
                    addRandomTree(currentForest);
                    System.out.println();
                    break;
                case 'C':
                    cutTree(currentForest);
                    System.out.println();
                    break;
                case 'G':
                    currentForest.growForest();
                    System.out.println();
                    break;
                case 'R':
                    reapTrees(currentForest);
                    System.out.println();
                    break;
                case 'S':
                    saveForest(currentForest);
                    System.out.println();
                    break;
                case 'L':
                    System.out.print("Enter forest name: ");
                    String filename = scanner.nextLine().trim();
                    Forest newForest = loadNewForest(filename);
                    if (newForest != null) {
                        forests.set(currentForestIndex, newForest);
                    } else {
                        System.out.println("Old forest retained");
                    }
                    System.out.println();
                    break;
                case 'N':
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
                            System.out.println();
                            break; // break the loop if the forest is loaded successfully
                        } catch (Exception e) {
                            System.out.println("Error opening/reading " + args[nextForestIndex] + ".csv");
                            nextForestIndex = (nextForestIndex + 1) % args.length; // move to the next forest
                            if (nextForestIndex == currentForestIndex) { // if we've tried all forests and none could be loaded
                                System.out.println("No valid forests could be loaded. Exiting program.");
                                return;
                            }
                        }
                    }
                    break;
                case 'X':
                    System.out.println();
                    System.out.println("Exiting the Forestry Simulation");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private static void printForest(Forest forest) {
        ArrayList<Tree> trees = forest.getTrees();
        System.out.println("Forest name: " + forest.getName());
        double totalHeight = 0;
        for (int i = 0; i < trees.size(); i++) {
            Tree tree = trees.get(i);
            totalHeight += tree.getHeight();
            System.out.printf("     %d %-7s %d %7.2f' %5.1f%%\n", i, tree.getSpecies(),
                    tree.getYearPlanted(), tree.getHeight(), tree.getGrowthRate());
        }
        double averageHeight = trees.size() > 0 ? totalHeight / trees.size() : 0;
        System.out.printf("There are %d trees, with an average height of %.2f\n", trees.size(), averageHeight);
    }

    private static void addRandomTree(Forest forest) {
        Tree tree = new Tree(Tree.Species.values()[random.nextInt(Tree.Species.values().length)],
                2022, 10 + random.nextDouble() * 10, 10 + random.nextDouble() * 10);
        forest.addTree(tree);
        // System.out.println("Added a new tree: " + tree.getSpecies());
    }

    private static void cutTree(Forest forest) {
        while (true) {
            System.out.print("Tree number to cut down: ");
            String input = scanner.nextLine();
            try {
                int index = Integer.parseInt(input);
                if (index >= 0 && index < forest.getTrees().size()) {
                    forest.cutDownTree(index);
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
                System.out.println("That is not an integer");
                // continue the loop if input is not a number
            }
        }
    }

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
                forest.addTree(new Tree(species, yearPlanted, height, growthRate));
            }
        }
        return forest;
    }

    private static void saveForest(Forest forest) {
        String filename = forest.getName() + ".db";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(forest);
        } catch (IOException e) {
            System.out.println("Error saving the forest to file: " + e.getMessage());
        }
    }

    private static Forest loadNewForest(String filename) {
        Forest forest = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename + ".db"))) {
            forest = (Forest) in.readObject();
            System.out.println("Forest '" + filename + "' has been loaded.");
        } catch (IOException e) {
            System.out.println("Error opening/reading " + filename + ".db");
        } catch (ClassNotFoundException e) {
            System.out.println("Error in data format: " + e.getMessage());
        }
        return forest;
    }
}
