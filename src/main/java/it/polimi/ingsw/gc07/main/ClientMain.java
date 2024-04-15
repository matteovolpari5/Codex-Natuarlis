package it.polimi.ingsw.gc07.main;

import it.polimi.ingsw.gc07.network.rmi.RMIClient;
import it.polimi.ingsw.gc07.network.rmi.VirtualServerGamesManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Insert nickname: ");
            System.out.print("> ");
            String nickname = scan.nextLine();

            System.out.println("Insert IP: ");
            System.out.print("> ");
            String ip = scan.nextLine();
            //TODO check IP
            // deve avere un formato che vada bene

            Registry registry = LocateRegistry.getRegistry(ip, 1234);
            VirtualServerGamesManager server = (VirtualServerGamesManager) registry.lookup("VirtualServerGamesManager");

            RMIClient newRMIClient = new RMIClient(server, nickname);
            try {
                server.connect(newRMIClient);
                newRMIClient.runCliJoinGame();
            }catch(RemoteException e) {
                //TODO
                e.printStackTrace();
                throw new RuntimeException();
            }
        }catch(RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException();
            //TODO manage remote exception
        }catch(NotBoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
            //TODO manage not bound exception
        }
    }
}
