package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public class Channel<T> implements go.Channel<T> {

    private String name;
    private RemoteChannel<T> rChan;

    public Channel(String name) throws RemoteException {
        this.name = name;
    }

    public void addRC(RemoteChannel rc){
        this.rChan = rc;
    }

    public void out(T v) {
        try{
            rChan.out(v);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public T in() {
        try{
            return rChan.in();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void observe(Direction direction, Observer observer) {
        try{
            rChan.observe(direction, observer);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
