package org.example;


import org.example.instances.ServerInstance;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int VIRTUAL_NODE_REPLICAS = 2;
    private static final Random random = new Random();

    public static void main(String[] args) {
        var physicalInstances = getServerInstances();

        // initialize physical instances in ring
        var consistent = new ConsistentHashTable(physicalInstances, VIRTUAL_NODE_REPLICAS);

        // simulate server scaling up and down
        var serverSimulate = new SimulateServerCluster(consistent, physicalInstances);

        // simulate API request to ring
        var apiSimulate = new SimulateAPIRequest(consistent);

        var executorServerService = Executors.newSingleThreadScheduledExecutor();
        executorServerService.scheduleAtFixedRate(serverSimulate, 1, 5, TimeUnit.SECONDS);

        var executorAPIService = Executors.newSingleThreadScheduledExecutor();
        executorAPIService.scheduleAtFixedRate(apiSimulate, 1, 1, TimeUnit.SECONDS);
    }

    private static List<ServerInstance> getServerInstances() {
        var instance1 = new ServerInstance("Key1", "127.0.0.1", 8081);
        var instance2 = new ServerInstance("Key2", "127.0.0.2", 8082);
        var instance3 = new ServerInstance("Key3", "127.0.0.3", 8083);
        var instance4 = new ServerInstance("Key4", "127.0.0.4", 8084);
        var instance5 = new ServerInstance("Key5", "127.0.0.5", 8085);
        var instance6 = new ServerInstance("Key6", "127.0.0.6", 8086);
        return List.of(instance1, instance2, instance3, instance4, instance5, instance6);
    }

    public static int random(int max) {
        return random.nextInt(max);
    }

}