package go.cs;

import go.Channel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class DirectoryImpl extends UnicastRemoteObject implements Directory {
    HashMap<String, RemoteChannel> mapChannel = new HashMap<>();

    protected DirectoryImpl() throws RemoteException {
        super();
    }

    @Override
    public void addToMap(RemoteChannel c) throws RemoteException {
        if (mapChannel.containsKey(c.getName())){
            System.out.println("ERROR: Channel already in the hashmap.");
        }
        else{
            mapChannel.put(c.getName(), c);
        }
    }

    @Override
    public RemoteChannel getRC(String name) throws RemoteException {
        if (mapChannel.containsKey(name)){
            return mapChannel.get(name);
        }
        else{
            throw new RemoteException("Does not exists");
        }
    }

    @Override
    public RemoteChannel getRC(RemoteChannel c) throws RemoteException {
        if (mapChannel.containsKey(c.getName())){
            return mapChannel.get(c.getName());
        }
        else{
            throw new RemoteException("Does not exists");
        }

    }


}
