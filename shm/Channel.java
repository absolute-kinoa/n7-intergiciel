package go.shm;

import go.Direction;
import go.Observer;

import java.util.ArrayList;
import java.util.Iterator;
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
    private ArrayList<Observer> observersIn = new ArrayList<>();
    private ArrayList<Observer> observersOut = new ArrayList<>();

    public Channel(String name) {
        // TODO
        this.name = name;
    }

    public void out(T v) {
        // TODO: Appel Callback -> prevenir operation OUT
        if (!observersOut.isEmpty()){
            Iterator<Observer> iterator = observersOut.iterator();
            // Iterate through the ArrayList and remove each item
            while (iterator.hasNext()) {
                Observer obs = iterator.next();
                obs.update();
                iterator.remove();
            }
        }

        System.out.println("OUT: Starting out with value "+ v.toString() + " to list.");
        lock.lock();
        try {
            System.out.println("OUT: Adding value "+ v.toString() + " to list.");

            listValues.add(v);
            System.out.println("OUT: Signaling NOTEMPTY");
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T in() {
        // TODO: Appel Callback -> prevenir operation IN
        if (!observersIn.isEmpty()){
            Iterator<Observer> iterator = observersIn.iterator();
            // Iterate through the ArrayList and remove each item
            while (iterator.hasNext()) {
                Observer obs = iterator.next();
                obs.update();
                iterator.remove();
            }
        }

        lock.lock();
        System.out.println("IN: Retrieving value");

        try {
            while (listValues.isEmpty()) {
                System.out.println("IN:  WAITINGVALUE");
                notEmpty.await();
            }
            System.out.println("IN:  Removing value");
            return listValues.remove();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public String getName() {
        // TODO
        return name;
    }

    public void observe(Direction dir, Observer observer) {
        // TODO
//        System.out.println("adding observer in" + dir.toString());
        if (dir == Direction.In){
            observersIn.add(observer);
        }else{
            observersOut.add(observer);
        }
    }

}
