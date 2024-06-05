package go.test.cs;

import go.cs.RemoteChannel;
import go.cs.RemoteFactoryInterface;
import java.rmi.Naming;

public class TestCS_2_B {

    private static void quit(String msg) {
        System.out.println("TestCS_1_B: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] args) {

        try {
            RemoteFactoryInterface remoteFactory = (RemoteFactoryInterface) Naming.lookup("//localhost/Factory");

            RemoteChannel<String> remoteChannel = remoteFactory.newChannel("testChannel");

            // Receiving multiple messages
            new Thread(() -> {
                try {
                    for (int i = 0; i < 5; i++) {
                        String message = remoteChannel.in();
                        System.out.println("Received: " + message);
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
