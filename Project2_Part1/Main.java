import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Our Feature Selection Algorithm\n");
        System.out.print("Please enter total number of features: ");
        int n = sc.nextInt();
        System.out.println("\nType the number of the algorithm you want to run.");
        System.out.println("\n\t1. Forward Selection\n\t2. Backward Elimination\n\t3. Bertie's Special Algorithm\n");
        int selection = sc.nextInt();

        if (selection == 1) {
            forwardSelection(n);
        }
        if (selection == 2) {
            backwardElimination(n);
        }
    }
    private static void forwardSelection(int n) {
        Random random = new Random();
        double rand = random.nextDouble(75) + 25;
        System.out.print("\nUsing no features and random evaluation, I get an accuracy of ");
        System.out.printf("%.1f", rand);
        System.out.println("%");
        HashMap<Double, int[]> finalMap = new HashMap<>();
        double finalMax = 0;
        int[] originalArray = new int[0];
        System.out.println("\nBeginning search.\n");
        for (int i = 0; i < n; i++) {
            HashMap<Double, int[]> map = new HashMap<>();
            List<int[]> lists = generateLongerArrays(originalArray, n);
            double max = 0;
            for (int[] list : lists) {
                double num2 = random.nextDouble(70) + 25;
                map.put(num2, list);
                max = Math.max(max, num2);
                System.out.print("\tUsing feature(s) {");
                for (int j = list.length - 1; j > 0; j--) {
                    System.out.print(list[j] + ", ");
                }
                System.out.print(list[0]);
                System.out.print("}, the accuracy is ");

                System.out.printf("%.1f", num2);
                System.out.println("%");
                System.out.println();
            }
            originalArray = map.get(max);
            finalMax = Math.max(finalMax, max);
            finalMap.put(max, originalArray);
            System.out.print("Feature set {");
            for (int k = originalArray.length - 1; k > 0; k--) {
                System.out.print(originalArray[k] + ", ");
            }
            System.out.print(originalArray[0]);
            System.out.print("} is the best, the accuracy is ");
            System.out.printf("%.1f", max);
            System.out.println("%\n");
        }
        int[] finalArr = finalMap.get(finalMax);
        System.out.print("Finished search!! The best feature subset {");
        for (int p = finalArr.length - 1; p > 0; p--) {
            System.out.print(finalArr[p] + ", ");
        }
        System.out.print(finalArr[0]);
        System.out.print("} is the best, the accuracy is ");
        System.out.printf("%.1f", finalMax);
        System.out.println("%\n");
    }
    private static void backwardElimination(int n) {
        int[] arr = new int[n];
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        int index = 0;
        while (index < n) {
            int num = random.nextInt(n) + 1;
            if (!set.contains(num)) {
                set.add(num);
                arr[index] = num;
                index++;
            }
        }
        System.out.print("The total " + n + " features have a sequence of ");
        for (int i = 0; i < n; i++) {
                System.out.print(arr[i]);
                System.out.print(", ");
        }
        System.out.print(" and an accuracy of ");
        double num = random.nextDouble(70) + 25;
        System.out.printf("%.1f", num);
        System.out.println("%");
        System.out.println("\nBeginning Backward Elimination:\n");
        int[] newArr = arr.clone();
        HashMap<Double, int[]> finalMap = new HashMap<>();
        double finalMax = 0;
        for (int i = 0; i < n - 1; i++) {
            HashMap<Double, int[]> hashMap = new HashMap<>();
            List<int[]> subArrays = generateSubarrays(newArr);
            double max = 0;
            for (int[] subArray : subArrays) {
                double num1 = random.nextDouble(70) + 25;
                hashMap.put(num1, subArray);
                max = Math.max(max, num1);
                System.out.print("\tUsing feature(s) {");
                for (int j = 0; j < subArray.length - 1; j++) {
                    System.out.print(subArray[j] + ", ");
                }
                System.out.print(subArray[subArray.length - 1]);
                System.out.print("}, the accuracy is ");

                System.out.printf("%.1f", num1);
                System.out.println("%");
                System.out.println();
            }
            newArr = hashMap.get(max);
            finalMax = Math.max(finalMax, max);
            finalMap.put(max, newArr);
            System.out.print("Feature set {");
            for (int k = 0; k < newArr.length - 1; k++) {
                System.out.print(newArr[k] + ", ");
            }
            System.out.print(newArr[newArr.length - 1]);
            System.out.print("} is the best, the accuracy is ");
            System.out.printf("%.1f", max);
            System.out.println("%\n");
        }
        int[] finalArr = finalMap.get(finalMax);
        System.out.print("Finished search!! The best feature subset {");
        for (int p = 0; p < finalArr.length - 1; p++) {
            System.out.print(finalArr[p] + ", ");
        }
        System.out.print(finalArr[finalArr.length - 1]);
        System.out.print("} is the best, the accuracy is ");
        System.out.printf("%.1f", finalMax);
        System.out.println("%\n");
    }
    private static List<int[]> generateSubarrays(int[] arr) {
        List<int[]> subarrays = new ArrayList<>();
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int[] subarray = new int[n - 1];
            int index = 0;
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    subarray[index] = arr[j];
                    index++;
                }
            }
            subarrays.add(subarray);
        }
        return subarrays;
    }
    private static List<int[]> generateLongerArrays(int[] originalArray, int n) {
        List<int[]> result = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (!contains(originalArray, i)) {
                int[] newArray = new int[originalArray.length + 1];
                for (int j = 0; j < originalArray.length; j++) {
                    newArray[j] = originalArray[j];
                }
                newArray[originalArray.length] = i;

                result.add(newArray);
            }
        }
        return result;
    }
    private static boolean contains(int[] array, int num) {
        for (int element : array) {
            if (element == num) {
                return true;
            }
        }
        return false;
    }
}