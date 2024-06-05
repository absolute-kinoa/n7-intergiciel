package go.test.shm;

import go.Channel;
import go.Factory;

/** Un unique in/out, commençant par in */
// On lance 3 in/out à la suite
public class TestShmX3 {

    private static void quit(String msg) {
        System.out.println("TestShmX3: " + msg);
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
            if(v!=4) quit("KO");

            v=c.in();
            if(v!=2) quit("KO");

            v=c.in();
            if(v!=13) quit("KO");

            quit("ok");
        }).start();

        new Thread(() -> {
            try { Thread.sleep(100);  } catch (InterruptedException e) { }
            c.out(4);
            c.out(2);
            c.out(13);
        }).start();


    }
}
