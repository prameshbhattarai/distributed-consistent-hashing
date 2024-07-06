package org.example;

import org.example.instances.Instance;
import org.example.instances.ServerInstance;
import org.example.instances.VirtualServerInstance;

import java.util.*;

/**
 * Consistent Hash Ring Table.
 */
public class ConsistentHashTable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    private static final String V_NODE_PREFIX = "Vir%d-%s";
    private final SortedMap<Integer, VirtualServerInstance> ring;
    private final int virtualNode;
    private final HashFunction hashFunction;

    public ConsistentHashTable(Collection<ServerInstance> instances, int virtualNode) {
        this.ring = new TreeMap<>();
        this.hashFunction = new HashFunction();
        this.virtualNode = virtualNode;
        initializeHashTable(instances);
    }

    private void initializeHashTable(Collection<ServerInstance> instances) {
        instances.forEach(this::addInstanceInCluster);
    }

    public synchronized void addInstanceInCluster(ServerInstance instance) {
        for (int i = 0; i < virtualNode; i++) {
            var vInstance = new VirtualServerInstance(V_NODE_PREFIX.formatted(i, instance.key()), instance);
            var hash = hashFunction.hash(vInstance.key());
            ring.putIfAbsent(hash, vInstance);
        }
    }

    public synchronized void removeInstanceFromCluster(ServerInstance instance) {
        for (int i = 0; i < virtualNode; i++) {
            var vInstance = new VirtualServerInstance(V_NODE_PREFIX.formatted(i, instance.key()), instance);
            var hash = hashFunction.hash(vInstance.key());
            ring.remove(hash);
        }
    }

    /**
     * Return Instance from Ring by hashing the API Request ID.
     * We should use same hash function to hash the apiRequestId, so that we have collision.
     * @param apiRequestId API Request ID
     * @return Virtual Instance.
     */
    public synchronized Instance getInstanceFromCluster(String apiRequestId) {
        var hash = hashFunction.hash(apiRequestId);
        // tailRing is a view of the portion of this map whose keys are greater than or equal to fromKey
        // in consistent hashing, we need to get next nearest instance
        var tailRing = ring.tailMap(hash);

        // if no nearest instance then get first instance from ring
        // else get first instance from nearest instance ring
        var key = tailRing.isEmpty() ? ring.firstKey() : tailRing.firstKey();
        return ring.get(key);
    }

    public void printCluster() {
        for (var entry : ring.entrySet()) {
            System.out.println("%s %s : %s %s".formatted(ANSI_PURPLE, entry.getKey(), entry.getValue(), ANSI_RESET));
        }
    }
}
