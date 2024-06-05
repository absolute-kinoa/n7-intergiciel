package go.shm;

import go.Direction;
import go.Observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Channel<T> implements go.Channel<T> {

    final String name;
    LinkedList<T> listValues = new LinkedList<>();

    // Attributes for concurrency
    private boolean reading = false, writing = false;
    private Semaphore sIn = new Semaphore(0), sOut = new Semaphore(0);

    // List of observers
    private final ArrayList<Observer> observersIn = new ArrayList<>();
    private final ArrayList<Observer> observersOut = new ArrayList<>();

    public Channel(String name) {
        this.name = name;
    }

    public void out(T v) {
        writing = true;
        // Notify OUT observers
        notifyObservers(observersOut);
        listValues.add(v);
        try {
            sOut.release();
            sIn.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        writing = false;

    }

    public T in() {
        reading = true;
        notifyObservers(observersIn);
        try {
            sOut.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        sIn.release();
        reading = false;
        T value = listValues.remove();
        return value;
        // Notify IN observers

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
