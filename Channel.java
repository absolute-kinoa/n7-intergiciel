package go;

/** Un canal de communication synchrone, qui permet d'envoyer et recevoir des messages de type T. */
public interface Channel<T> extends java.io.Serializable {

    /** Envoi synchrone d'un message.
     * Bloque tant que le message ne peut pas être envoyé. */
    public void out(T v);
    
    /** Réception synchrone d'un message.
     * Bloque tant qu'il n'y a pas de message disponible. */
    public T in();

    /** Nom du canal.
     * On suppose que les noms sont uniques (en particulier pour client-serveur ou point-à-point).
     */
    public String getName();

    /** Adds an observer to the set of observers for this channel, for the
     * specified direction. The observer is notified when a in/out operation
     * is pending, and then it is removed: an observation fires at most once.
     * If an in/out operation is already pending when observe is called, it
     * immediately fires. */
    public void observe(Direction direction, Observer observer);

}
