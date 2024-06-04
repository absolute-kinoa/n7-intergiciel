package go.cs;

import go.Channel;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteFactory extends UnicastRemoteObject implements RemoteFactoryInterface {
    private final Factory factory;

    public RemoteFactory(Factory factory) throws RemoteException {
        this.factory = factory;
    }

    @Override
    public <T> RemoteChannel<T> newChannel(String name) throws RemoteException {
        Channel<T> channel = factory.newChannel(name);
        return new RemoteChannelImpl<>(channel);
    }
}
