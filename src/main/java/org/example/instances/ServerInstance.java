package org.example.instances;

public record ServerInstance(String key, String ipAddress, int port) implements Instance {
}
