package org.example.instances;

public record VirtualServerInstance(String key, ServerInstance physicalNode) implements Instance {
    @Override
    public String ipAddress() {
        return physicalNode.ipAddress();
    }

    @Override
    public int port() {
        return physicalNode.port();
    }

}
