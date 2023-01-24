package Task2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static class Event {
        private final int day;
        private final int hour;
        private final int minute;
        private final String status;

        public Event(String day, String hour, String minute, String status) {
            this.day = Integer.parseInt(day);
            this.hour = Integer.parseInt(hour);
            this.minute = Integer.parseInt(minute);
            this.status = status;
        }

        public int getDay() {
            return day;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        public String getStatus() {
            return status;
        }
    }

    public static void main(String[] args) {

        List<String> inputData = readData();
        int numberOfRecords = Integer.parseInt(inputData.get(0));
        List<String> outputData = new ArrayList<>();
        Map<Integer, List<Event>> rocketData = new HashMap<>();
        List<Integer> rocketNumbers = new ArrayList<>();

        for (int i = 1; i < numberOfRecords + 1; i++) {
            String[] eventData = inputData.get(i).split(" ");
            int rocketNumber = Integer.parseInt(eventData[3]);
            if (!rocketData.containsKey(rocketNumber))
                rocketNumbers.add(rocketNumber);
            List<Event> tmp = rocketData.get(rocketNumber);
            if (tmp == null)
                tmp = new ArrayList<Event>();
            tmp.add(new Event(eventData[0], eventData[1], eventData[2], eventData[4]));
            rocketData.put(rocketNumber, tmp);
        }

        rocketNumbers = rocketNumbers.stream().sorted().collect(Collectors.toList());

        for (Integer rocketNumber: rocketNumbers) {
            outputData.add(getRocketMinutes(rocketNumber, rocketData.get(rocketNumber)));
        }

        writeData(outputData);
    }

    private static String getRocketMinutes(int rocketNumber, List<Event> events) {

        events.sort(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                if (o1.day < o2.day)
                    return -1;
                else if(o1.day > o2.day)
                    return 1;
                else {
                    if (o1.hour < o2.hour)
                        return -1;
                    else if(o1.hour > o2.hour)
                        return 1;
                    else {
                        if (o1.minute < o2.minute)
                            return -1;
                        else if(o1.minute > o2.minute)
                            return 1;
                        else return 0;
                    }
                }
            }
        });

        int shift = 1;
        int i = 0;
        int numberOfMinutes = 0;

        while (i < events.size()) {

            Event tmpStart = events.get(i);
            Event tmpFinish = tmpStart;
            while (!tmpFinish.status.equals("C") && !tmpFinish.status.equals("S")) {
                tmpFinish = events.get(i + shift);
                shift++;
            }

            numberOfMinutes += countMinutes(tmpStart, tmpFinish);

            i = i + shift;
            shift = 1;

        }

        return String.valueOf(numberOfMinutes);
    }

    public static int countMinutes(Event start, Event finale) {
        int finMin = finale.minute + (finale.hour * 60) + (finale.day * 1440);
        int startMin = start.minute + (start.hour * 60) + (start.day * 1440);

        return finMin - startMin;
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