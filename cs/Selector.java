package go.cs;

import go.Direction;
import go.Channel;
import go.Observer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Selector extends UnicastRemoteObject implements go.Selector {
    static final Lock lock = new ReentrantLock();
    static final Condition ChanIsAvailable = lock.newCondition();
    private static boolean ChanAvailable = false;
    private final HashMap<Channel, Direction> channels = new HashMap<>();
    static final LinkedList<Channel> availableChannels = new LinkedList<>();

    static class Observateur extends UnicastRemoteObject implements Observer {
        Channel itsChan;

        public Observateur(Channel c) throws RemoteException {
            super();
            itsChan = c;
        }

        @Override
        public void update() {
            lock.lock();
            try {
                availableChannels.add(itsChan);
                ChanAvailable = true;
                ChanIsAvailable.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public Selector(Map<Channel, Direction> channels) throws RemoteException {
        super();
        this.channels.putAll(channels);
        for (Channel chan : channels.keySet()) {
            Direction dir = Direction.inverse(channels.get(chan));
            chan.observe(dir, new Observateur(chan));
        }
    }

    @Override
    public Channel select() {
        lock.lock();
        try {
            while (!ChanAvailable) {
                ChanIsAvailable.await();
            }
            ChanAvailable = false;
            Channel chan = availableChannels.remove();
            Direction dir = Direction.inverse(channels.get(chan));
            chan.observe(dir, new Observateur(chan));
            return chan;
        } catch (InterruptedException | RemoteException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }
}
