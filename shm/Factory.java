package go.shm;

import go.Direction;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static go.shm.Selector.lock;

public class Factory implements go.Factory {
    private final Map<String, go.Channel<?>> channelsMap = new HashMap<>();

    /** Création ou accès à un canal existant. */
    public <T> go.Channel<T> newChannel(String name) {
        lock.lock();
        try {
            if (channelsMap.containsKey(name)) {
                return (go.Channel<T>) channelsMap.get(name);
            } else {
                go.Channel<T> newChannel = new go.shm.Channel<>(name);
                channelsMap.put(name, newChannel);
                return newChannel;
            }
        } finally {
            lock.unlock();
        }
    }

    /** Spécifie quels sont les canaux écoutés et la direction pour chacun. */
    public go.Selector newSelector(Map<go.Channel, Direction> channels) {
        return new go.shm.Selector(channels);
    }

    /** Spécifie quels sont les canaux écoutés et la même direction pour tous. */
    public go.Selector newSelector(Set<go.Channel> channels, Direction direction) {
        return newSelector(channels
                           .stream()
                           .collect(Collectors.toMap(Function.identity(), e -> direction)));
    }

}

