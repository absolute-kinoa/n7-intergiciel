package go.sock;

import java.io.*;
import java.net.*;

public class ChannelSlave<T extends Serializable> {
    private final String masterAddress;
    private final int masterPort;

    public ChannelSlave(String masterAddress, int masterPort) {
        this.masterAddress = masterAddress;
        this.masterPort = masterPort;
    }

    public void in(T message) throws IOException {
        try (Socket socket = new Socket(masterAddress, masterPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("in");
            out.writeObject(message);
        }
    }

    public T out() throws IOException {
        try (Socket socket = new Socket(masterAddress, masterPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("out");
            return (T) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage: java ChannelSlave <namingServiceAddress> <channelName> <message>");
            System.exit(1);
        }

        String namingServiceAddress = args[0];
        String channelName = args[1];
        String message = args[2];

        Naming namingService = new Naming(namingServiceAddress);
        String channelAddress = namingService.getChannelAddress(channelName);
        if (channelAddress == null) {
            System.err.println("Channel not found");
            System.exit(1);
        }
        String[] parts = channelAddress.split(":");
        String masterAddress = parts[0];
        int masterPort = Integer.parseInt(parts[1]);

        ChannelSlave<Serializable> slave = new ChannelSlave<>(masterAddress, masterPort);
        slave.in(message);
        System.out.println("Message sent: " + message);
        Serializable receivedMessage = slave.out();
        System.out.println("Message received: " + receivedMessage);
    }
}


