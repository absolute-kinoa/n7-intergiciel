package go.test;

import go.Channel;
import go.Factory;

/** Un unique in/out, commençant par in */
// On lance 3 in/out à la suite
public class TestShmX5 {

    private static void quit(String msg) {
        System.out.println("TestShmX5: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c = factory.newChannel("c");

        System.out.println("Name of factory: "+c.getName());

        new Thread(() -> {
            try { Thread.sleep(2000);  } catch (InterruptedException e) { }
            quit("ok (waiting for a new in operation)");
        }).start();


        new Thread(() -> {
            int v = c.in();
            v=c.in();
        }).start();

        new Thread(() -> {
            c.out(4);
        }).start();

        new Thread(() -> {
            c.out(2);
        }).start();

        new Thread(() -> {
            c.out(13);
        }).start();


    }
}
