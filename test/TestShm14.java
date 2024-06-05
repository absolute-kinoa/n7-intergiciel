package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import go.Selector;

/* TEST 14 : 2 Select en parallèle */
public class TestShm14 {

    private static void quit(String msg) {
        System.out.println("TestShm14: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c1 = factory.newChannel("c1");
        Channel<Integer> c2 = factory.newChannel("c2");
        Channel<Integer> c3 = factory.newChannel("c3");
        Channel<Integer> c4 = factory.newChannel("c4");

        Selector s = factory.newSelector(java.util.Map.of(c1, Direction.Out,
                                                          c2, Direction.In));
        Selector s1 = factory.newSelector(java.util.Map.of(c3, Direction.Out,
                                                           c4, Direction.In));
        new Thread(() -> {
                try { Thread.sleep(2000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        new Thread(() -> {
                Channel<Integer> c = s.select();
                int v = c.in();
                if (v != 4) quit("KO");
                try { Thread.sleep(100);  } catch (InterruptedException e) { }
                    Channel<Integer> cc = s1.select();
                    cc.out(8);
        }).start();

        new Thread(() -> {
                @SuppressWarnings("unchecked")
                Channel<Integer> c = s.select();
                c.out(4);
                Channel<Integer> cc = s1.select();
                int v = cc.in();
                if (v != 8) quit("KO");
        }).start();
        
                   
    }
}
