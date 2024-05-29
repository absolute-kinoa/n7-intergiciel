package go.sock;

import java.util.concurrent.ConcurrentHashMap;

public class Naming {
    private final ConcurrentHashMap<String, String> registry;

    public Naming() {
        registry = new ConcurrentHashMap<>();
    }

    public Naming(String address) {
        // This constructor is a placeholder if we need to initialize the Naming service
        // with some address or configuration.
        this();
    }

    public void registerChannel(String channelName, String address) {
        registry.put(channelName, address);
    }

    public String getChannelAddress(String channelName) {
        return registry.get(channelName);
    }

    public static void main(String[] args) {
        Naming namingService = new Naming();
        namingService.registerChannel("channel1", "127.0.0.1:8080");
        String address = namingService.getChannelAddress("channel1");
        System.out.println("Address of channel1: " + address);
    }
}
