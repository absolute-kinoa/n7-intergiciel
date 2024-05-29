package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import go.Selector;

/* Test 12 : Select out */
public class TestShm12 {

    private static void quit(String msg) {
        System.out.println("TestShm12: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c1 = factory.newChannel("c1");
        Channel<Integer> c2 = factory.newChannel("c2");
        Channel<Integer> c3 = factory.newChannel("c3");

        Selector s = factory.newSelector(java.util.Set.of(c1, c2, c3), Direction.Out);
        new Thread(() -> {
                try { Thread.sleep(2000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();
        
        new Thread(() -> {
                try { Thread.sleep(100);  } catch (InterruptedException e) { }
                Channel<Integer> c = s.select(); //Selectionne le channel 1
                c.out(4);
                Channel<Integer> cc = s.select(); //Sélectionne le channel 2
                cc.out(6);
        }).start();

        new Thread(() -> {
                try { Thread.sleep(100);  } catch (InterruptedException e) { }
                int v = c1.in();
                if (v != 4) quit("KO");
                v = c2.in();
                if (v != 6) quit("KO");

                quit("ok");
        }).start();
        
                   
    }
}
