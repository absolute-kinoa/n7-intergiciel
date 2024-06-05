package go.cs;

import go.Direction;
import go.Channel;
import go.Observer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Selector implements go.Selector {

    // Attributs pour la concurrence
    private static Semaphore sem = new Semaphore(0);

    // List of channels that you want to listen on
    HashMap<Channel, Direction> channels = new HashMap<Channel, Direction>();
    // Channels that are available
    static LinkedList<Channel> availableChannels = new LinkedList<>();

    static class Observateur implements Observer {
        Channel itsChan;

        public Observateur(Channel c){
            itsChan = c;
        }

        public void update() {
            System.out.println("> OBS: Un in() est là");
            availableChannels.add(itsChan);
            System.out.println("> OBS: ADDING " + itsChan.getName() + " to available list.");
            if (sem.availablePermits() == 0) {
                sem.release();
            }
        }
    }

    public Selector(Map<Channel, Direction> channels) {
        this.channels = new HashMap<>(channels);
        Direction dir;
        for (Channel chan : channels.keySet()) {
            dir = Direction.inverse(channels.get(chan));
            System.out.println("Clé : " + chan.getName() + " attend Valeur : " + Direction.inverse(dir));
            chan.observe(dir, new Observateur(chan));
        }
    }

    public Channel select() {
        try{
            System.out.println("> SELECTOR: WAITING FOR AVAILABILITY");
            sem.acquire();
            Channel chan = availableChannels.remove();
            Direction dir = Direction.inverse(channels.get(chan));
            chan.observe(dir, new Observateur(chan));
            System.out.println("> SELECTOR: CHAN " + chan.getName() + " IS AVAILABLE !!");
            return chan;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
