package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.controller.GamesManagerCommand;
import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RMIServerGamesManagerGamesManager implements VirtualServerGamesManager {
    /**
     * Games manager controller.
     */
    private final GamesManager gamesManager;
    /**
     * Virtual views of connected clients.
     */
    private final List<VirtualView> clients;

    /**
     * Constructor of class RMIServerGamesManagerGamesManager.
     * @param gamesManager games manager
     * @throws RemoteException remote exception
     */
    public RMIServerGamesManagerGamesManager(GamesManager gamesManager) throws RemoteException {
        this.gamesManager = gamesManager;
        this.clients = new ArrayList<>();
    }

    /**
     * Method that allows a client to connect with the RMI server.
     * @param client client to connect
     * @throws RemoteException remote exception
     */
    @Override
    public synchronized void connect(VirtualView client) throws RemoteException {
        clients.add(client);
        System.err.println("New client connected");
    }

    /**
     * Method that allows the client to execute a games manager command.
     * @param gamesManagerCommand games manager command to set and execute
     * @throws RemoteException remote exception
     */
    @Override
    public void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) throws RemoteException {
        gamesManagerCommand.setGamesManager(gamesManager);
        gamesManager.setAndExecuteCommand(gamesManagerCommand);
        System.out.println(gamesManager.getCommandResultManager().getCommandResult());
        if(gamesManager.getCommandResultManager().getCommandResult().equals(CommandResult.SUCCESS)) {
            // TODO stampa aggiornamento al client
        }else if(gamesManager.getCommandResultManager().getCommandResult().equals(CommandResult.SET_GAME)) {
            //TODO
            // se non esiste un RMIServerGame per il Game, crearlo
            // setServerGame sul RMIClient
        }else {
            System.out.println(gamesManager.getCommandResultManager().getCommandResult().getResultMessage());
        }
    }
}
