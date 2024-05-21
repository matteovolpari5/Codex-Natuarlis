package it.polimi.ingsw.gc07.main;

import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;
import it.polimi.ingsw.gc07.network.socket.SocketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) throws IOException {

        // create Rmi server
        String name = "VirtualServerGamesManager";
        RmiServerGamesManager serverGamesManager = RmiServerGamesManager.getRmiServerGamesManager();
        String serverIp = args[0];
        System.setProperty("java.rmi.server.hostname", serverIp);
        int rmiPort = Integer.parseInt(args[1]);
        Registry registry = LocateRegistry.createRegistry(rmiPort);
        registry.rebind(name, serverGamesManager);
        System.out.println("RMI Server running");

        // create Socket server for gamesManager
        int socketPort = Integer.parseInt(args[2]);
        ServerSocket sc;
        try{
            sc = new ServerSocket(socketPort);
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
