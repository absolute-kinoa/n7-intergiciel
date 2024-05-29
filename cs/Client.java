package go.cs;

import go.Channel;
import java.rmi.Naming;

public class Client {
    private static Directory dir;
    private static RemoteChannel rc;

    public static void main (String args[]) throws Exception{
        try{
            dir = (Directory) Naming.lookup("rmi://localhost:1099/directory");
            System.out.println("ERROR : Cant connect to server");

        }catch (Exception e){
            System.out.println("ERROR : Cant connect to server");
        }

        // Creating a new channel ?
    }

}
