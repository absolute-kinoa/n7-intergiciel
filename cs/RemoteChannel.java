package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteChannel<T> extends Remote {

    String getName() throws RemoteException;

    T in() throws RemoteException;

    void out(T v) throws RemoteException;

    void observe( Direction dir, Observer observer) throws RemoteException;

}
