package go.cs;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import go.cs.*;

/**
 * Implantation d'un serveur hébergeant des canaux.
 *
 */
public class ServerImpl {

    public static void main(String args[]) throws Exception {
        Registry dns;

        try {
            dns = LocateRegistry.createRegistry(1100);
        } catch (java.rmi.server.ExportException e) {
            System.out.println("A registry is already running, proceeding...");
            dns = LocateRegistry.getRegistry("localhost",1100);
        }

        // Creation de l'objet Directory
        go.cs.Directory dir = new DirectoryImpl();
        Naming.rebind("rmi://localhost:1099/directory", dir);

        System.out.println ("Le systeme est pret.");
    }

}
