import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Path filePath;
        System.out.println("Welcome to Our Feature Selection Algorithm\n");
        System.out.print("Please choose the dataset you want to test (1 for small-test-dataset, 2 for large-test-dataset): ");
        Scanner sc = new Scanner(System.in);
        String enter = sc.nextLine();
        if (enter.equals("1")) {
            filePath = Paths.get("/Users/taochen/Desktop/Past Classes/CS 170/Project2/part2/test_data/CS170_Spring_2023_Small_data__16.txt");
        } else {
            filePath = Paths.get("/Users/taochen/Desktop/Past Classes/CS 170/Project2/part2/test_data/CS170_Spring_2023_Large_data__16.txt");
        }

        List<Integer> features = new ArrayList<>();
        List<String> lines = Files.readAllLines(filePath);

        // Determine the number of features based on the first row of data
        String[] firstRowValues = lines.get(0).split("\\s+");
        int numFeatures = firstRowValues.length;

        // Assuming you want to consider features from index 1 to numFeatures-1
        for (int i = 2; i < numFeatures; i++) {
            features.add(i - 1);
        }

        double[][] matrix = new double[lines.size()][];

        for (int i = 0; i < lines.size(); i++) {
            String[] values = lines.get(i).split("\\s+");
            matrix[i] = new double[numFeatures - 1]; // Consider features from index 1 to numFeatures-1

            for (int j = 0; j < numFeatures - 1; j++) {
                if (!values[j + 1].isEmpty()) {
                    matrix[i][j] = Double.parseDouble(values[j + 1]);
                }
            }
        }

        System.out.println("\nType the number of the algorithm you want to run.");
        System.out.println("\n\t1. Forward Selection\n\t2. Backward Elimination\n\t3. Bertie's Special Algorithm\n");
        int selection = sc.nextInt();
        System.out.println("\nThis dataset has " + (matrix[0].length - 1) + " features, with " + matrix.length + " instances.");
        if (selection == 1) {
            forwardSelection(matrix, features);
        }
        if (selection == 2) {
            backwardElimination(matrix, features);
        }
        if (selection == 3) {
            //recursiveFeatureElimination(matrix, features);
        }
    }

    private static void forwardSelection(double[][] matrix, List<Integer> features) {
        Random random = new Random();
        double rand = random.nextDouble(75) + 25;
        HashMap<Double, Integer> hashMap = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        List<Integer> selectedFeatures = new ArrayList<>();
        System.out.print("\nRunning nearest neighbor with no features (default rate), using “leaving-one-out” evaluation, I\n" +
                "get an accuracy of ");
        System.out.printf("%.1f", rand);
        System.out.println("%");
        Map<Double, List<Integer>> finalBest = new HashMap<>();
        finalBest.put(rand, selectedFeatures);
        int featureSize = 1;
        double maxAccuracy = 0;
        double finalMax = 0;
        System.out.println("\nBeginning Forward Selection...\n");

        while (featureSize < matrix[0].length - 1) {
            maxAccuracy = 0;
            System.out.println("current selected feature: " + selectedFeatures);

            for (int feature : features) {
                if (!visited.contains(feature)) {
                    double num = findAccuracy(matrix, feature, selectedFeatures);
                    hashMap.put(num, feature);
                    maxAccuracy = Math.max(maxAccuracy, num);
                    System.out.println("Try adding feature " + feature + ", the accuracy is " + num + "%");
                }
            }

            System.out.println("\nThe best accuracy is " + maxAccuracy + " of feature " + hashMap.get(maxAccuracy));
            selectedFeatures.add(hashMap.get(maxAccuracy));
            visited.add(hashMap.get(maxAccuracy));
            finalBest.put(maxAccuracy, new ArrayList<>(selectedFeatures));
            System.out.println("Features: " + selectedFeatures + "\n");
            finalMax = Math.max(maxAccuracy, finalMax);
            featureSize++;
        }

        System.out.println("The best combination is " + finalBest.get(finalMax) + " with the accuracy of " + finalMax + "%");
    }

    private static void backwardElimination(double[][] matrix, List<Integer> features) {
        HashMap<Double, Integer> hashMap = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        List<Integer> selectedFeatures = new ArrayList<>();

        // Initialize selectedFeatures with all features
        for (int i = 1; i < matrix[0].length - 1; i++) {
            selectedFeatures.add(i);
        }

        Map<Double, List<Integer>> finalBest = new HashMap<>();
        int featureSize = selectedFeatures.size(); // Number of features initially
        double maxAccuracy = 0;
        double finalMax = 0;
        System.out.println("\nBeginning Backward Elimination...\n");
        double num = findAccuracy(matrix, matrix[0].length - 1, selectedFeatures);
        selectedFeatures.add(matrix[0].length - 1);
        System.out.println("\nWith selected features: " + selectedFeatures + ", the accuracy is " + num + "%");

        while (featureSize > 0) {
            maxAccuracy = 0;
            int removedFeature = -1;
            System.out.println("Current selected features: " + selectedFeatures);

            // Iterate through each selected feature and find the accuracy when removing it
            for (int feature : selectedFeatures) {
                List<Integer> tempFeatures = new ArrayList<>(selectedFeatures);
                tempFeatures.remove(Integer.valueOf(feature));
                double accuracy = findAccuracy(matrix, tempFeatures);
                hashMap.put(accuracy, feature);
                maxAccuracy = Math.max(maxAccuracy, accuracy);
                System.out.println("Considering features combination of " + tempFeatures + ", the accuracy is " + accuracy + "%");
            }

            removedFeature = hashMap.get(maxAccuracy);
            selectedFeatures.remove(Integer.valueOf(removedFeature));
            visited.add(removedFeature);
            finalBest.put(maxAccuracy, new ArrayList<>(selectedFeatures));
            System.out.println("Remove feature " + removedFeature + ", we got the best features combination " + selectedFeatures + "\n");
            finalMax = Math.max(maxAccuracy, finalMax);
            featureSize--;
        }

        System.out.println("The best combination is " + finalBest.get(finalMax) + " with the accuracy of " + finalMax + "%");
    }

    private static double findAccuracy(double[][] matrix, int feature, List<Integer> selectedFeatures) {
        List<Integer> temps = new ArrayList<>();
        temps.addAll(selectedFeatures);
        temps.add(feature);
        int correct = 0;
        for (int testInstance = 0; testInstance < matrix.length; testInstance++) {
            Map<Double, Integer> map = new HashMap<>();
            double minDistance = Double.MAX_VALUE;
            for (int row = 0; row < matrix.length; row++) {
                if (row != testInstance) {
                    double distance = 0;
                    for (int temp : temps) {
                        double num = matrix[row][temp] - matrix[testInstance][temp];
                        distance += num * num;
                    }
                    distance = Math.sqrt(distance);
                    map.put(distance, row);
                    minDistance = Math.min(minDistance, distance);
                }
            }
            if (matrix[testInstance][0] == matrix[map.get(minDistance)][0]) {
                correct++;
            }
        }
        return 100 * (double) correct / matrix.length;
    }
    private static double findAccuracy(double[][] matrix, List<Integer> selectedFeatures) {
        int correct = 0;

        for (int testInstance = 0; testInstance < matrix.length; testInstance++) {
            Map<Double, Integer> map = new HashMap<>();
            double minDistance = Double.MAX_VALUE;

            for (int row = 0; row < matrix.length; row++) {
                if (row != testInstance) {
                    double distance = 0;

                    for (int tempFeature : selectedFeatures) {
                        double num = matrix[row][tempFeature] - matrix[testInstance][tempFeature];
                        distance += num * num;
                    }

                    distance = Math.sqrt(distance);
                    map.put(distance, row);
                    minDistance = Math.min(minDistance, distance);
                }
            }

            if (matrix[testInstance][0] == matrix[map.get(minDistance)][0]) {
                correct++;
            }
        }
        return 100 * (double) correct / matrix.length;
    }
}