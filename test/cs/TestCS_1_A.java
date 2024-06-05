package go.test.cs;

import go.cs.RemoteChannel;
import go.cs.RemoteFactoryInterface;

import java.rmi.Naming;

public class TestCS_1_A {

    private static void quit(String msg) {
        System.out.println("TestCS20a: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] args) {
        try {
            RemoteFactoryInterface remoteFactory = (RemoteFactoryInterface) Naming.lookup("//localhost/Factory");

            RemoteChannel<String> remoteChannel = remoteFactory.newChannel("testChannel");

            // Sending a message
            new Thread(() -> {
                try {
                    remoteChannel.out("Hello, World!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try { Thread.sleep(5000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
            }).start();

            quit("ok");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
