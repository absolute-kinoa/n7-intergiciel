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
    }

}
