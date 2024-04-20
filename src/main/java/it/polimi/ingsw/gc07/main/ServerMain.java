package it.polimi.ingsw.gc07.main;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;
import it.polimi.ingsw.gc07.network.socket.SocketGamesManagerServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) throws RemoteException, IOException {
        // create RMI server
        String name = "VirtualServerGamesManager";
        RmiServerGamesManager serverGamesManager = new RmiServerGamesManager(GamesManager.getGamesManager());
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(name, serverGamesManager);
        System.out.println("GamesManager bound");

        // create Socket server for gamesManager
        //TODO per adesso la porta è data da linea di comando, stabilire se bisogna cambiarlo
        int port = Integer.parseInt(args[1]);
        ServerSocket sc = new ServerSocket(port);
        new SocketGamesManagerServer(sc, GamesManager.getGamesManager()).runServer(); //TODO runServer() dovrebbe essere un metodo privato
    }
}
