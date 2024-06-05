package go.test.shm;

import go.Channel;
import go.Factory;

/** Un unique in/out, commençant par in */
// On lance le thread in en premier
public class TestShmX2 {

    private static void quit(String msg) {
        System.out.println("TestShmX2: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c = factory.newChannel("c");

        System.out.println("Name of factory: "+c.getName());

        new Thread(() -> {
            try { Thread.sleep(2000);  } catch (InterruptedException e) { }
            quit("KO (deadlock)");
        }).start();


        new Thread(() -> {
            int v = c.in();
            quit(v == 4 ? "ok" : "KO");
        }).start();

        new Thread(() -> {
            try { Thread.sleep(100);  } catch (InterruptedException e) { }
            c.out(4);
        }).start();


    }
}
