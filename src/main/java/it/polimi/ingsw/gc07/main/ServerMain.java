package it.polimi.ingsw.gc07.main;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.network.rmi.RMIServer;
import it.polimi.ingsw.gc07.network.rmi.VirtualServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain {
    public static void main(String[] args) throws RemoteException {
        String name = "VirtualServer";
        VirtualServer virtualServer = new RMIServer(new GamesManager());
        VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(virtualServer, 0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(name, stub);
        System.out.println("GamesManager bound");
    }
}
