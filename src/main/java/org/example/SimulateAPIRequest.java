package org.example;

/**
 * Class to simulate API request
 */
public class SimulateAPIRequest implements Runnable {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private final ConsistentHashTable consistentHashTable;

    public SimulateAPIRequest(ConsistentHashTable consistentHashTable) {
        this.consistentHashTable = consistentHashTable;
    }

    @Override
    public void run() {
        sendRequest();
    }

    private void sendRequest() {
        var random = Main.random(100);
        var apiRequestId = "%d".formatted(random);
        System.out.printf("%s Request with API Request %s %s%n", ANSI_BLUE, apiRequestId, ANSI_RESET);
        var virtualNode = consistentHashTable.getInstanceFromCluster(apiRequestId);
        System.out.printf("%s Result from Virtual Node %s %s%n", ANSI_GREEN, virtualNode, ANSI_RESET);
    }
}
