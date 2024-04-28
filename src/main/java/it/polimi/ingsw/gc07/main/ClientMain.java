package it.polimi.ingsw.gc07.main;

import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import it.polimi.ingsw.gc07.network.VirtualServerGamesManager;
import it.polimi.ingsw.gc07.network.socket.SocketClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
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

            boolean wrongInput = true;
            boolean connectionType = false;
            while(wrongInput) {
                System.out.println("Insert connection type (1 = RMI, 0 = Socket)");
                System.out.print("> ");
                int connectionTypeInt = scan.nextInt();
                scan.nextLine();
                if(connectionTypeInt == 1) {
                    wrongInput = false;
                    connectionType = true;
                }else if(connectionTypeInt == 0) {
                    wrongInput = false;
                    connectionType = false;
                }else {
                    System.out.println("No such connection type");
                }
            }

            wrongInput = true;
            boolean interfaceType = false;
            while(wrongInput) {
                System.out.println("Insert interface type(1 = GUI, 0 = TUI)");
                System.out.print("> ");
                int interfaceTypeInt = scan.nextInt();
                scan.nextLine();
                if(interfaceTypeInt == 1) {
                    wrongInput = false;
                    interfaceType = true;
                }else if(interfaceTypeInt == 0) {
                    wrongInput = false;
                    interfaceType = false;
                }else {
                    System.out.println("No such interface type");
                }
            }

            if(connectionType == true) {
                // RMI
                Registry registry = LocateRegistry.getRegistry(ip, 1234);
                VirtualServerGamesManager server = (VirtualServerGamesManager) registry.lookup("VirtualServerGamesManager");

                RmiClient newRmiClient = new RmiClient(server, nickname);
                try {
                    server.connect(newRmiClient);
                    newRmiClient.connectToGamesManager(connectionType, interfaceType);
                    newRmiClient.runCliJoinGame();
                }catch(RemoteException e) {
                    //TODO
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }else {
                // Socket
                //TODO per adesso la porta e l'host è data da linea di comando, stabilire se bisogna cambiarlo
                String host = args[0];
                int port = Integer.parseInt(args[1]);
                Socket sc = new Socket(host, port);
                SocketClient socketClient= new SocketClient(nickname, sc);
                socketClient.connectToGamesManager(connectionType, interfaceType);
            }
        }catch(RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException();
            //TODO manage remote exception
        }catch(NotBoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
            //TODO manage not bound exception
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
            //TODO sollevata dal costruttore di Socket
        } catch (IOException e) {
            throw new RuntimeException(e);
            //TODO sollevata dal costruttore di Socket
        }
    }
}
