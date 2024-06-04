package go.cs;

import go.Channel;
import go.Direction;
import go.Observer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteChannelImpl<T> extends UnicastRemoteObject implements RemoteChannel<T> {
    private final Channel<T> channel;

    public RemoteChannelImpl(Channel<T> channel) throws RemoteException {
        this.channel = channel;
    }

    @Override
    public void out(T v) throws RemoteException {
        channel.out(v);
    }

    @Override
    public T in() throws RemoteException {
        return channel.in();
    }

    @Override
    public String getName() throws RemoteException {
        return channel.getName();
    }

    @Override
    public void observe(Direction direction, RemoteObserver observer) throws RemoteException {
        channel.observe(direction, new Observer() {
            @Override
            public void update() {
                try {
                    observer.update();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
