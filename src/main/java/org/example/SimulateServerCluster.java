package org.example;

import org.example.instances.ServerInstance;

import java.util.List;

/**
 * Class to simulate server up and down
 */
public class SimulateServerCluster implements Runnable {

    private final ConsistentHashTable consistentHashTable;
    private final List<ServerInstance> instances;
    private final int instanceSize;

    public SimulateServerCluster(ConsistentHashTable consistentHashTable, List<ServerInstance> instances) {
        this.consistentHashTable = consistentHashTable;
        this.instances = instances;
        this.instanceSize = instances.size();
    }

    @Override
    public void run() {
        removeInstances();
        addInstance();
        consistentHashTable.printCluster();
    }

    private void removeInstances() {
        var random = Main.random(instanceSize);
        var nodeToRemoved = instances.get(random);
        consistentHashTable.removeInstanceFromCluster(nodeToRemoved);

        // random remove one more
        if (random % 2 == 0) {
            nodeToRemoved = instances.get(Main.random(instanceSize));
            consistentHashTable.removeInstanceFromCluster(nodeToRemoved);
        }
    }

    private void addInstance() {
        var random = Main.random(instanceSize);
        var newNode = instances.get(random);
        consistentHashTable.addInstanceInCluster(newNode);

        // random add one more
        if (random % 2 == 0) {
            newNode = instances.get(Main.random(instanceSize));
            consistentHashTable.addInstanceInCluster(newNode);
        }
    }
}
