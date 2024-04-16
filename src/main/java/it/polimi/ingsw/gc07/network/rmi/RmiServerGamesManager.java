package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.controller.GamesManagerCommand;
import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RmiServerGamesManager extends UnicastRemoteObject implements VirtualServerGamesManager {
    /**
     * Games manager controller.
     */
    private final GamesManager gamesManager;
    /**
     * Virtual views of connected clients.
     */
    private final List<VirtualView> clients;
    /**
     * Map containing the RmiServerGame of every game.
     */
    private Map<Integer, RmiServerGame> rmiServerGames;

    /**
     * Constructor of class RmiServerGamesManager.
     * @param gamesManager games manager
     * @throws RemoteException remote exception
     */
    public RmiServerGamesManager(GamesManager gamesManager) throws RemoteException {
        this.gamesManager = gamesManager;
        this.clients = new ArrayList<>();
        this.rmiServerGames = new HashMap<>();
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
        gamesManager.setAndExecuteCommand(gamesManagerCommand);
        System.out.println(gamesManager.getCommandResultManager().getCommandResult());
        if(gamesManager.getCommandResultManager().getCommandResult().equals(CommandResult.SUCCESS)) {
            // TODO stampa aggiornamento al client
        }else if(gamesManager.getCommandResultManager().getCommandResult().equals(CommandResult.SET_SERVER_GAME)) {
            String commandNickname = gamesManagerCommand.getNickname();
            int gameId = gamesManager.getGameIdWithPlayer(commandNickname);
            if(gameId < 0) {
                throw new RuntimeException();
            }
            VirtualView virtualView = getVirtualView(commandNickname);
            if(virtualView == null) {
                throw new RuntimeException();
            }
            virtualView.setServerGame(rmiServerGames.get(gameId));
        }else if(gamesManager.getCommandResultManager().getCommandResult().equals(CommandResult.CREATE_SERVER_GAME)) {
            String commandNickname = gamesManagerCommand.getNickname();
            int gameId = gamesManager.getGameIdWithPlayer(commandNickname);
            if(gameId < 0) {
                throw new RuntimeException();
            }
            VirtualView virtualView = getVirtualView(commandNickname);
            if(virtualView == null) {
                throw new RuntimeException();
            }
            rmiServerGames.put(gameId, new RmiServerGame(gamesManager.getGameById(gameId)));
            virtualView.setServerGame(rmiServerGames.get(gameId));
        }else {
            System.out.println(gamesManager.getCommandResultManager().getCommandResult().getResultMessage());
        }
    }

    // trova la virtual view associata al nickname del command
    private VirtualView getVirtualView(String commandNickname) throws RemoteException {
        for(VirtualView client : clients) {
            if(client.getNickname().equals(commandNickname)) {
                return client;
            }
        }
        return null;
    }
}
