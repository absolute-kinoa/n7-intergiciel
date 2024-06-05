package go.test.cs;

import go.cs.RemoteChannel;
import go.cs.RemoteFactoryInterface;
import java.rmi.Naming;

public class TestCS_3_B {

    private static void quit(String msg) {
        System.out.println("TestCS_3_B: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] args) {

        try {
            RemoteFactoryInterface remoteFactory = (RemoteFactoryInterface) Naming.lookup("//localhost/Factory");

            RemoteChannel<String> remoteChannel = remoteFactory.newChannel("testChannel");

            // Receiving multiple "in" and "out" operations
            new Thread(() -> {
                try {
                    for (int i = 0; i < 5; i++) {
                        if (i % 2 == 0) {
                            String message = remoteChannel.in();
                            System.out.println("Received: " + message);
                        } else {
                            remoteChannel.out("Response " + (i + 1));
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
