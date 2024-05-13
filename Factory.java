package go;

import java.util.Set;
import java.util.Map;

public interface Factory {

    /** Création ou accès à un canal existant. */
    public <T> go.Channel<T> newChannel(String name);
    
    /** Spécifie quels sont les canaux écoutés et la direction pour chacun. */
    public Selector newSelector(Map<Channel, Direction> channels);

    /** Spécifie quels sont les canaux écoutés et la même direction pour tous. */
    public Selector newSelector(Set<Channel> channels, Direction direction);

}

