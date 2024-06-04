package go.cs;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerImpl {
    public static void main(String[] args) {
        try {
            Factory factory = new Factory();
            RemoteFactory remoteFactory = new RemoteFactory(factory);
            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.rebind("Factory", remoteFactory);
            System.out.println("Server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
