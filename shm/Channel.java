package go.shm;

import go.Direction;
import go.Observer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Channel<T> implements go.Channel<T> {

    LinkedList<T> listValues = new LinkedList<T>();
    final Lock lock = new ReentrantLock();
    final Condition notEmpty = lock.newCondition();
    final Condition waitingValue = lock.newCondition();
    final String name;

    public Channel(String name) {
        // TODO
        this.name = name;
    }
    
    public void out(T v) {
        // TODO
        System.out.println("OUT: Starting out with value "+ v.toString() + " to list.");
        lock.lock();
//        try{
//            System.out.println("OUT: Waiting for a in method");
//            waitingValue.await();
//        }catch(InterruptedException e){
//            e.printStackTrace();
//        }
        listValues.add(v);
        System.out.println("OUT: Signaling NOTEMPTY");
        notEmpty.signalAll();
        lock.unlock();
    }
    
    public T in() {
        // TODO
        System.out.println("IN: Retrieving value");
        lock.lock();
        System.out.println("IN: Signaling WAITINGVALUE");
        waitingValue.signalAll();
        try {
            System.out.println("IN: Waiting for value");
            notEmpty.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("IN: Returning value");
        return listValues.remove();
    }

    public String getName() {
        // TODO
        return name;
    }

    public void observe(Direction dir, Observer observer) {
        // TODO
    }
        
}
