package it.polimi.ingsw.gc07.network.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RMIClient extends UnicastRemoteObject implements VirtualView {
    final VirtualServer server;

    protected RMIClient(VirtualServer server) throws RemoteException {
        this.server = server;
    }

    private void run() {
        try {
            server.connect(this);
            runCli();
        }catch(RemoteException e) {
            //TODO
        }
    }

    private void runCli() {
        Scanner scan = new Scanner(System.in);
        while(true) {
            System.out.print("> ");
            String command = scan.nextLine();
            if(command != null) {
                System.out.println("Command received: " + command);
            }
        }
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(args[0], 1234);
            VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");
            new RMIClient(server).run();
        }catch(RemoteException e) {
            //TODO manage remote exception
        }catch(NotBoundException e) {
            //TODO manage not bound exception
        }
    }
}
