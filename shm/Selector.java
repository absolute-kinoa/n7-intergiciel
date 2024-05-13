package go.shm;

import go.Direction;
import go.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Selector implements go.Selector {

    HashMap<Channel, Direction> channels = new HashMap<Channel, Direction>();

    public Selector(Map<Channel, Direction> channels) {
        this.channels = (HashMap<Channel, Direction>) channels;
    }

    public Channel select() {
        // TODO
        return null;
    }

}
