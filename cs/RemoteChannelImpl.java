package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RemoteChannelImpl<T> extends UnicastRemoteObject implements RemoteChannel<T>{

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

    Channel chan;

    protected RemoteChannelImpl(String name, Channel c) throws RemoteException {
        super();
        this.chan = c;
        this.name = name;
    }

    protected RemoteChannelImpl(String name) throws RemoteException {
        super();
        this.name = name;
    }
    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public T in() throws RemoteException {
        System.out.println("RC " + getName() + " - IN OPERATION START");
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
            System.out.println("RC " + getName() + " - IN OPERATION DONE");
            return value;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void out(T v) throws RemoteException {
        lock.lock();
        System.out.println("RC " + getName() + " - OUT OPERATION START");
        try {
            // Notify OUT observers
            notifyObservers(observersOut);

            while (!usedValue) {
                waitingValue.await();
            }

            usedValue = false;
            listValues.add(v);
            notEmpty.signal();
            System.out.println("RC " + getName() + " - OUT OPERATION DONE");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void notifyObservers(ArrayList<Observer> observers) {
        try {
            System.out.println("RC " + getName() + " - NOTIFYING OBSERVERS");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Iterator<Observer> iterator = observers.iterator();
        while (iterator.hasNext()) {
            Observer observer = iterator.next();
            observer.update();
            iterator.remove();
        }
    }

    @Override
    public void observe(Direction dir, Observer observer) throws RemoteException {
        if (dir == Direction.In) {
            System.out.println("RC " + getName() + " - IN OBSERVER ADDED");
            observersIn.add(observer);
        } else {
            observersOut.add(observer);
            System.out.println("RC " + getName() + " - OUT OBSERVER ADDED ");

        }

    }
}
