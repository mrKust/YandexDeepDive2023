package Task5;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {

    private static class Order {
        private final int start;
        private final int end;
        private final int cost;

        public Order(String start, String end, String cost) {
            this.start = Integer.parseInt(start);
            this.end = Integer.parseInt(end);
            this.cost = Integer.parseInt(cost);
        }
    }

    private static class Query {
        private final int start;
        private final int end;
        private final int type;
        private final int queue;
        private String result;

        public Query(String start, String end, String cost, int queue) {
            this.start = Integer.parseInt(start);
            this.end = Integer.parseInt(end);
            this.type = Integer.parseInt(cost);
            this.queue = queue;
        }

        public void setResult (String res) {
            this.result = res;
        }
    }

    public static void main(String[] args) {

        List<String> inputData = readData();
        int numberOfOrders = Integer.parseInt(inputData.get(0));
        List<Order> orders = new ArrayList<>(numberOfOrders);

        for (int i = 1; i < numberOfOrders + 1; i++) {
            String[] orderData = inputData.get(i).split(" ");
            orders.add(new Order(orderData[0], orderData[1], orderData[2]));
        }

        int numberOfQueries = Integer.parseInt(inputData.get(numberOfOrders + 1));
        List<Query> queries = new ArrayList<>(numberOfQueries);
        int num = 0;
        for (int i = 1 + numberOfOrders + 1; i < 1+ numberOfOrders + numberOfQueries + 1; i++) {
            String[] queryData = inputData.get(i).split(" ");
            queries.add(new Query(queryData[0], queryData[1], queryData[2], num));
            num++;
        }

        List<String> outputData = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfOrders);
        CountDownLatch latch = new CountDownLatch(numberOfQueries);

        for (int i = 0; i < numberOfQueries; i++) {
            Query tmp = queries.get(i);
            executor.execute(() -> {
                tmp.setResult(execQuery(tmp, orders));
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();

        List<String> res = queries.stream().map((obj) -> obj.result).collect(Collectors.toList());

        writeData(res);
    }

    private static String execQuery(Query query, List<Order> orderList) {
        if (query.type == 1)
            return summaryPrice(query.start, query.end, orderList);
        if (query.type == 2)
            return summaryLength(query.start, query.end, orderList);

        return null;
    }

    private static String summaryPrice(int start, int end, List<Order> orderList) {
        int summaryPrice = 0;

        for (Order currentOrder : orderList) {
            if ( (currentOrder.start >= start) && (currentOrder.start <= end) )
                summaryPrice += currentOrder.cost;
        }

        return String.valueOf(summaryPrice);
    }

    private static String summaryLength(int start, int end, List<Order> orderList) {
        int summaryLength = 0;

        for (Order currentOrder : orderList) {
            if ( (currentOrder.end >= start) && (currentOrder.end <= end) )
                summaryLength += currentOrder.end - currentOrder.start;
        }

        return String.valueOf(summaryLength);

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
