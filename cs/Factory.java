package go.cs;

import go.Channel;
import go.Direction;

import java.rmi.Remote;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.rmi.Naming;

public class Factory implements go.Factory {

    private final Map<String, go.cs.Channel<?>> channelsMap = new HashMap<>();

    /** Création ou accès à un canal existant.
     * Côté serveur, le canal est créé au premier appel avec un nom donné ;
     * les appels suivants avec le même nom donneront accès au même canal.
     */
    public <T> go.Channel<T> newChannel(String name) {
        Directory d = null;
        try {
            if (channelsMap.containsKey(name)){
                return (Channel<T>) channelsMap.get(name);
            }
            else {
                d = (Directory) Naming.lookup("rmi://localhost:1100/directory");
                RemoteChannel rc = d.getRC(name);
                if (rc == null){
                    rc = new RemoteChannelImpl(name);
                }
                Channel c = new go.cs.Channel(name);
                ((go.cs.Channel) c).addRC(rc);
                channelsMap.put(name, (go.cs.Channel) c);
                return c;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    /** Spécifie quels sont les canaux écoutés et la direction pour chacun. */
    public go.Selector newSelector(Map<go.Channel, Direction> channels) {
        // TODO
        return null;
    }

    /** Spécifie quels sont les canaux écoutés et la même direction pour tous. */
    public go.Selector newSelector(Set<go.Channel> channels, Direction direction) {
        return newSelector(channels
                           .stream() 
                           .collect(Collectors.toMap(Function.identity(), e -> direction)));
    }

}

