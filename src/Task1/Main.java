package Task1;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        List<String> inputData = readData();
        int numberOfCandidates = Integer.parseInt(inputData.get(0));
        List<String> outputData = new ArrayList<>(numberOfCandidates);
        for (int i = 0; i < numberOfCandidates; i++) {
            String[] userData = inputData.get(1 + i).split(",");
            outputData.add(userCode(userData[0], userData[1], userData[2], userData[3] + userData[4]));
        }
        writeData(outputData);

    }

    private static String userCode(String surname, String name, String thirdname, String dayAndMonth) {

        Set<Character> symbols = new HashSet<>();

        for(Character n : surname.toCharArray()) {
            symbols.add(n);
        }

        for(Character n : name.toCharArray()) {
            symbols.add(n);
        }

        for(Character n : thirdname.toCharArray()) {
            symbols.add(n);
        }

        int sumVal = 0;

        for (Character ch : dayAndMonth.toCharArray()) {
            sumVal += Integer.parseInt(ch.toString());
        }

        int valOfFirstLetter = (int)surname.charAt(0) - 64;

        int summaryVal = symbols.size() + (sumVal * 64) + (valOfFirstLetter * 256);
        String valInHex = Integer.toHexString(summaryVal);

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            try {
                result.append(valInHex.charAt(valInHex.length() - i - 1));
            } catch (Exception e) {
              result.append(0);
            }
        }

        return result.reverse().toString().toUpperCase();
    }

    private static List<String> readData() {
        List<String> text = new ArrayList<>();
        try (BufferedReader inputFile = new BufferedReader(new FileReader("input.txt"))) {
            String currentLine = inputFile.readLine();

            while (currentLine != null) {
                text.add(currentLine);
                currentLine = inputFile.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    private static void writeData(List<String> writeData) {
        try (BufferedWriter outputFile = new BufferedWriter(new FileWriter("output.txt"))) {

            for (String currentLine : writeData) {
                outputFile.write(currentLine);
                outputFile.write(" ");
            }

            outputFile.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}