package go.cs;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Directory extends Remote {

    public void addToMap(RemoteChannel c) throws RemoteException;
    public RemoteChannel getRC(String name) throws RemoteException;
    public RemoteChannel getRC(RemoteChannel c) throws RemoteException;
}
