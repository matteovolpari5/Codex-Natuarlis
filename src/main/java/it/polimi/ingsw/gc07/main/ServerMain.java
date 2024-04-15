package it.polimi.ingsw.gc07.main;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.network.rmi.RMIServerGamesManagerGamesManager;
import it.polimi.ingsw.gc07.network.rmi.VirtualServerGamesManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain {
    public static void main(String[] args) throws RemoteException {
        String name = "VirtualServerGamesManager";
        VirtualServerGamesManager virtualServerGamesManager = new RMIServerGamesManagerGamesManager(new GamesManager());
        VirtualServerGamesManager stub = (VirtualServerGamesManager) UnicastRemoteObject.exportObject(virtualServerGamesManager, 0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(name, stub);
        System.out.println("GamesManager bound");
    }
}
