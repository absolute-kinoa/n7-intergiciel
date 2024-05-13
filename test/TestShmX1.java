package go.test;

import go.Channel;
import go.Factory;
import go.Observer;
import go.Direction;

/** Un unique in/out, commençant par in */
public class TestShmX1 {

    static class ObservationInTest implements Observer {
        public void update() {
            System.out.println("OBS: Un in() est là");
        }
    }
    static class ObservationOutTest implements Observer {
        public void update() {
            System.out.println("OBS: Un out() est là");
        }
    }
    private static void quit(String msg) {
        System.out.println("TestShm01: " + msg);
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
            try { Thread.sleep(100);  } catch (InterruptedException e) { }
            c.observe(Direction.Out, new ObservationOutTest());
            c.out(4);
        }).start();

        new Thread(() -> {
            c.observe(Direction.In, new ObservationInTest());
            int v = c.in();
            quit(v == 4 ? "ok" : "KO");
        }).start();


    }
}
