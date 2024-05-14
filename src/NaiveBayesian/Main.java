package NaiveBayesian;

import DecisionTree.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static int[][] readInput(String filename) {
        try {
            int[][] matrix = new int[150][];
            int rowIndex = 0;
            File file = new File(filename);
            Scanner scan_file = new Scanner(file);

            String[] tokenized_attributes;

            while (scan_file.hasNextLine()) {
                tokenized_attributes = scan_file.nextLine().split(",");

                ArrayList<Double> integerList = new ArrayList<>(
                        Arrays.stream(tokenized_attributes)
                                .mapToDouble(Double::parseDouble)
                                .boxed()
                                .collect(Collectors.toList())
                );


                matrix[rowIndex] = arrayListToArray(integerList);
                rowIndex++;
            }
            return matrix;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static int[] arrayListToArray(ArrayList<Double> row) {
        int[] arrayRow = new int[5];
        for (int i = 0; i < row.size(); i++) {
            arrayRow[i] = row.get(i).intValue();
        }
        return arrayRow;
    }

    public static ArrayList<Integer> getAllRows (int[][] matrix) {
        ArrayList<Integer> rows = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            rows.add(i);
        }
        return rows;
    }

    public static int[] getCustomerInput() {
        // read input from keyboard
        int [] listAttrVal = new int[4];

        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < listAttrVal.length; i++) {
            System.out.printf("Enter value for attribute %d: ", i);
            int val = scanner.nextInt();
            listAttrVal[i] = val;
        }
        return listAttrVal;
    }
    public static void main(String[] args) {
        int[][] matrix = readInput("/Users/zhihe/CSC466/labs/src/NaiveBayesian/data.txt");

        NaiveBayesian algo = new NaiveBayesian(matrix);
        int[] listOfValAttrAssoc = getCustomerInput();

        System.out.println("Expected category: " + (algo.findCategory(listOfValAttrAssoc)));
    }
}
