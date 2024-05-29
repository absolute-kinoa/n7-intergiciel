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

    final String name;
    LinkedList<T> listValues = new LinkedList<>();

    // Attributes for concurrency
    private boolean usedValue = true;
    final Lock lock = new ReentrantLock();
    final Condition notEmpty = lock.newCondition();
    final Condition waitingValue = lock.newCondition();

    // List of observers
    private final ArrayList<Observer> observersIn = new ArrayList<>();
    private final ArrayList<Observer> observersOut = new ArrayList<>();

    public Channel(String name) {
        this.name = name;
    }

    public void out(T v) {
        lock.lock();
        try {
            // Notify OUT observers
            notifyObservers(observersOut);

            while (!usedValue) {
                waitingValue.await();
            }

            usedValue = false;
            listValues.add(v);
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public T in() {
        lock.lock();
        try {
            // Notify IN observers
            notifyObservers(observersIn);

            while (usedValue) {
                notEmpty.await();
            }

            T value = listValues.remove();
            usedValue = true;
            waitingValue.signal();
            return value;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    private void notifyObservers(ArrayList<Observer> observers) {
        Iterator<Observer> iterator = observers.iterator();
        while (iterator.hasNext()) {
            Observer observer = iterator.next();
            observer.update();
            iterator.remove();
        }
    }

    public String getName() {
        return name;
    }

    public void observe(Direction dir, Observer observer) {
        if (dir == Direction.In) {
            observersIn.add(observer);
        } else {
            observersOut.add(observer);
        }
    }
}
