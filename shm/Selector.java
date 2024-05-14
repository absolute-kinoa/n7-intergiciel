package go.shm;

import go.Direction;
import go.Channel;
import go.Observer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Selector implements go.Selector {

    // Attributs pour la concurrence
    private boolean usedValue = true;
    static final Lock lock = new ReentrantLock();
    static final Condition ChanIsAvailable = lock.newCondition();
    private static boolean ChanAvailable = false;

    // List of channels that you want to listen on
    HashMap<Channel, Direction> channels = new HashMap<Channel, Direction>();
    // Channels that are available
    static LinkedList<Channel> availableChannels = new LinkedList<>();

    static class ObservationInTest implements Observer {

        Channel itsChan;

        public ObservationInTest(Channel c){
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
    static class ObservationOutTest implements Observer {
        Channel itsChan;

        public ObservationOutTest(Channel c){
            itsChan = c;
        }

        public void update() {
            lock.lock();
            System.out.println("> OBS: Un out() est là");
            availableChannels.add(itsChan);
            ChanAvailable = true;
            System.out.println("> OBS: ADDING " + itsChan.getName() + " to available list.");
            ChanIsAvailable.signal();
            lock.unlock();

        }
    }


    public Selector(Map<Channel, Direction> channels) {
        this.channels = (HashMap<Channel, Direction>) channels;
        Direction dir;
        for (Channel chan : channels.keySet()) {
            dir = Direction.inverse(channels.get(chan));
            System.out.println("Clé : " + chan + ", Valeur : " + dir);
            if (dir== Direction.In){
                chan.observe(dir,new ObservationInTest(chan));
            }else{
                chan.observe(dir,new ObservationOutTest(chan));
            }
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
