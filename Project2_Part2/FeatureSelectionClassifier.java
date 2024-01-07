import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class FeatureSelectionClassifier {
    public static void main(String[] args) throws IOException {
        Path filePath;
        System.out.print("Please choose dataset you want to test (1 for small-test-dataset, 2 for large-test-dataset): ");
        Scanner sc = new Scanner(System.in);
        String enter = sc.nextLine();
        if (enter.equals("1")) {
            filePath = Paths.get("/Users/taochen/Desktop/CS 170/Project2/part2/data/small-test-dataset.txt");
        } else {
            filePath = Paths.get("/Users/taochen/Desktop/CS 170/Project2/part2/data/large-test-dataset.txt");
        }

        List<Integer> features = new ArrayList<>();
        List<String> lines = Files.readAllLines(filePath);
        System.out.print("Please enter the feature(s) you want to test (separated by spaces): ");
        String input = sc.nextLine();
        String[] str = input.split(" ");

        for (String value : str) {
            int number = Integer.parseInt(value);
            features.add(number);
        }

        double[][] matrix = new double[lines.size()][];

        for (int i = 0; i < lines.size(); i++) {
            String[] values = lines.get(i).split("\\s+");
            matrix[i] = new double[values.length];

            for (int j = 0; j < values.length; j++) {
                if (!values[j].isEmpty()) {
                    matrix[i][j] = Double.parseDouble(values[j]);
                }
            }
        }

        int correct = 0;

        System.out.println("Performing classification:");

        for (int testInstance = 0; testInstance < matrix.length; testInstance++) {
            Map<Double, Integer> map = new HashMap<>();
            double minDistance = Double.MAX_VALUE;

            System.out.println("Classifying instance " + (testInstance + 1));

            long startTime = System.nanoTime();

            for (int row = 0; row < matrix.length; row++) {
                if (row != testInstance) {
                    double distance = 0;
                    System.out.println("  Comparing with instance " + (row + 1));
                    for (int feature : features) {
                        double num = matrix[row][feature + 1] - matrix[testInstance][feature + 1];
                        distance += num * num;
                    }
                    distance = Math.sqrt(distance);
                    map.put(distance, row);
                    minDistance = Math.min(minDistance, distance);
                }
            }

            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double elapsedTimeMs = elapsedTime / 1_000_000.0;

            System.out.print("  Nearest neighbor to instance " + (testInstance + 1) + ": " + (map.get(minDistance) + 1));
            System.out.println(" (Distance: " + minDistance + ")");
            if (matrix[testInstance][1] == matrix[map.get(minDistance)][1]) {
                correct++;
            }

            System.out.println("  Time taken: " + elapsedTimeMs + " milliseconds\n");
        }

        System.out.println("Classification complete.");
        System.out.print("There are ");
        System.out.print(correct);
        System.out.print(" correct tests\nThe accuracy is ");
        double accuracy = 100 * (double) correct / matrix.length;
        System.out.println(accuracy + "%");
    }
}
