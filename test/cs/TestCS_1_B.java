package go.test.cs;

import go.cs.RemoteChannel;
import go.cs.RemoteFactoryInterface;

import java.rmi.Naming;

public class TestCS_1_B {

    private static void quit(String msg) {
        System.out.println("TestCS20a: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }
    public static void main(String[] args) {

        try {
            RemoteFactoryInterface remoteFactory = (RemoteFactoryInterface) Naming.lookup("//localhost/Factory");

            RemoteChannel<String> remoteChannel = remoteFactory.newChannel("testChannel");

            // Receiving a message
            new Thread(() -> {
                try {
                    String message = remoteChannel.in();
                    System.out.println("Received: " + message);
                    quit("ok");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try { Thread.sleep(5000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
