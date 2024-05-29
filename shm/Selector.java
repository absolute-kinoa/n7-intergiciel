package go.shm;

import go.Direction;
import go.Channel;
import go.Observer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Selector implements go.Selector {

    // Attributs pour la concurrence
    static final Lock lock = new ReentrantLock();
    static final Condition ChanIsAvailable = lock.newCondition();
    private static boolean ChanAvailable = false;

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
            lock.lock();
            System.out.println("> OBS: Un in() est là");
            availableChannels.add(itsChan);
            ChanAvailable = true;
            System.out.println("> OBS: ADDING " + itsChan.getName() + " to available list.");
            ChanIsAvailable.signal();
            lock.unlock();
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
        lock.lock();
        try{
            System.out.println("> SELECTOR: WAITING FOR AVAILABILITY");
            while(!ChanAvailable)
                ChanIsAvailable.await();
            ChanAvailable = false;
            Channel chan = availableChannels.remove();
            Direction dir = Direction.inverse(channels.get(chan));
            chan.observe(dir, new Observateur(chan));
            System.out.println("> SELECTOR: CHAN " + chan.getName() + " IS AVAILABLE !!");
            return chan;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

}
