package go.sock;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ChannelMaster<T extends Serializable> {
    private final ServerSocket serverSocket;
    private final BlockingQueue<T> channelQueue;
    private final Naming namingService;
    private final String channelName;

    public ChannelMaster(int port, Naming namingService, String channelName) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.channelQueue = new LinkedBlockingQueue<>();
        this.namingService = namingService;
        this.channelName = channelName;

        // Register the channel with the naming service
        String address = InetAddress.getLocalHost().getHostAddress() + ":" + port;
        namingService.registerChannel(channelName, address);

        System.out.println("ChannelMaster running on port " + port);
        System.out.println("Registered channel '" + channelName + "' with address " + address);
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler<>(clientSocket, channelQueue).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ClientHandler<T extends Serializable> extends Thread {
        private final Socket clientSocket;
        private final BlockingQueue<T> channelQueue;

        public ClientHandler(Socket clientSocket, BlockingQueue<T> channelQueue) {
            this.clientSocket = clientSocket;
            this.channelQueue = channelQueue;
        }

        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                String request = (String) in.readObject();
                if (request.equals("in")) {
                    T message = (T) in.readObject();
                    channelQueue.put(message);
                } else if (request.equals("out")) {
                    T message = channelQueue.take();
                    out.writeObject(message);
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage: java ChannelMaster <port> <namingServiceAddress> <channelName>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String namingServiceAddress = args[1];
        String channelName = args[2];

        Naming namingService = new Naming(namingServiceAddress);
        ChannelMaster<Serializable> master = new ChannelMaster<>(port, namingService, channelName);
        master.start();
    }
}
