package it.polimi.ingsw.gc07.main;

import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;
import it.polimi.ingsw.gc07.network.socket.SocketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) throws RemoteException, IOException{ //IOException sollevata da linea 33
        // create RMI server
        String name = "VirtualServerGamesManager";
        RmiServerGamesManager serverGamesManager = RmiServerGamesManager.getRmiServerGamesManager();
        System.setProperty("java.rmi.server.hostname", "192.168.154.223");
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(name, serverGamesManager);
        System.out.println("RMI Server running");

        // create Socket server for gamesManager
        //TODO per adesso la porta è data da linea di comando, stabilire se bisogna cambiarlo
        //int port = Integer.parseInt(args[0]);   // TODO penso args[0]
        int port = 65000;
        ServerSocket sc = null;
        try{
            sc = new ServerSocket(port);
        } catch (IOException e){
            System.out.println("Unable to start the main server: unavailable port");
            throw new RuntimeException();
        }
        System.out.println("Main Socket server ready");
        SocketServer socketServer= SocketServer.getSocketServer();
        socketServer.initializeSocketServer(sc);
       socketServer.runServer();
    }
}
