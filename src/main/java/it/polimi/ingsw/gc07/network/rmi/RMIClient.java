package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.controller.AddPlayerToPendingCommand;

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
            System.out.println("Insert a character to perform an action:");
            // TODO probabilmente AddPlayerToPending non è qua (?)
            System.out.println("- Q to connect to games manager"); // AddPlayerToPendingCommand
            System.out.println("- W to join an existing game"); // JoinExistingGameCommand
            System.out.println("- E to join an new game"); // JoinNewGameCommand
            System.out.print("> ");
            String command = scan.nextLine();
            switch(command){
                case "Q":
                    System.out.println("Insert nickname: ");
                    System.out.print("> ");
                    String nickname = scan.nextLine();
                    System.out.println("Insert connection type (1 = RMI, 0 = Socket)");
                    System.out.print("> ");
                    int connectionTypeInt = scan.nextInt();
                    boolean connectionType;
                    if(connectionTypeInt == 1) {
                        connectionType = true;
                    }else if(connectionTypeInt == 0) {
                        connectionType = false;
                    }else {
                        System.out.println("No such connection type");
                        break;
                    }
                    System.out.println("Insert interface type(1 = GUI, 0 = TUI)");
                    System.out.print("> ");
                    int interfaceTypeInt = scan.nextInt();
                    boolean interfaceType;
                    if(interfaceTypeInt == 1) {
                        interfaceType = true;
                    }else if(interfaceTypeInt == 0) {
                        interfaceType = false;
                    }else {
                        System.out.println("No such interface type");
                        break;
                    }
                    try {
                        server.setAndExecuteCommand(new AddPlayerToPendingCommand(nickname, connectionType, interfaceType));
                    } catch (RemoteException e) {
                        // TODO
                        throw new RuntimeException(e);
                    }
                    break;
                case "W":
                    break;
                case "E":
                    break;
                default:
                    System.out.println("The provided character doesn't refer to any action");
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
