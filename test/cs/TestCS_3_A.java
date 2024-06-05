package go.test.cs;

import go.cs.RemoteChannel;
import go.cs.RemoteFactoryInterface;
import java.rmi.Naming;

public class TestCS_3_A {

    private static void quit(String msg) {
        System.out.println("TestCS_3_A: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] args) {
        try {
            RemoteFactoryInterface remoteFactory = (RemoteFactoryInterface) Naming.lookup("//localhost/Factory");

            RemoteChannel<String> remoteChannel = remoteFactory.newChannel("testChannel");

            // Sending multiple "in" and "out" operations
            new Thread(() -> {
                try {
                    for (int i = 0; i < 5; i++) {
                        if (i % 2 == 0) {
                            remoteChannel.out("Message " + (i + 1));
                        } else {
                            String message = remoteChannel.in();
                            System.out.println("Received: " + message);
                        }
                        Thread.sleep(1000);  // Simulate delay between operations
                    }
                    quit("ok");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
