package go.cs;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteFactoryInterface extends Remote {
    <T> RemoteChannel<T> newChannel(String name) throws RemoteException;
}
