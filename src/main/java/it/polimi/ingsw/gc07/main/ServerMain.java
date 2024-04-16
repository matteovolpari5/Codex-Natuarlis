package it.polimi.ingsw.gc07.main;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) throws RemoteException {
        String name = "VirtualServerGamesManager";
        RmiServerGamesManager serverGamesManager = new RmiServerGamesManager(GamesManager.getGamesManager());
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(name, serverGamesManager);
        System.out.println("GamesManager bound");
    }
}
