package go.cs;

import go.Channel;
import go.Direction;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteChannel<T> extends Remote {
    void out(T v) throws RemoteException;
    T in() throws RemoteException;
    String getName() throws RemoteException;
    void observe(Direction direction, RemoteObserver observer) throws RemoteException;
}
