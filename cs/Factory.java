package go.cs;

import go.Channel;
import go.Direction;
import go.Selector;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Factory implements go.Factory {
    private final Map<String, Channel<?>> channelsMap = new HashMap<>();

    @Override
    public <T> Channel<T> newChannel(String name) {
        synchronized (channelsMap) {
            if (channelsMap.containsKey(name)) {
                return (Channel<T>) channelsMap.get(name);
            } else {
                go.cs.Channel<T> newChannel = new go.cs.Channel<>(name);
                channelsMap.put(name, newChannel);
                return newChannel;
            }
        }
    }

    @Override
    public Selector newSelector(Map<Channel, Direction> channels) {
        try {
            return new go.cs.Selector(channels);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Selector newSelector(Set<Channel> channels, Direction direction) {
        return newSelector(channels.stream().collect(Collectors.toMap(Function.identity(), e -> direction)));
    }
}
