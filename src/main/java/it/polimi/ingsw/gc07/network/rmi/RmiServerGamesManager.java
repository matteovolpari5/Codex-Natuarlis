package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.game_commands.GamesManagerCommand;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.network.VirtualServerGamesManager;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.updates.ExistingGamesUpdate;

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
    private final Map<Integer, RmiServerGame> rmiServerGames;

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
    public synchronized void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) throws RemoteException {
        gamesManager.setAndExecuteCommand(gamesManagerCommand);
        if(
                gamesManager.getCommandResult().equals(CommandResult.SET_SERVER_GAME) ||
                gamesManager.getCommandResult().equals(CommandResult.CREATE_SERVER_GAME) ||
                gamesManager.getCommandResult().equals(CommandResult.DISPLAY_GAMES)
        ) {
            // get virtual view
            String commandNickname = gamesManagerCommand.getNickname();
            VirtualView virtualView = getVirtualView(commandNickname);
            if(virtualView == null) {
                throw new RuntimeException();
            }
            // if display games
            if(gamesManager.getCommandResult().equals(CommandResult.DISPLAY_GAMES)) {
                ExistingGamesUpdate update = new ExistingGamesUpdate(gamesManager.getFreeGamesDetails());
                virtualView.receiveExistingGamesUpdate(update);
                return;
            }
            // get game id
            int gameId = gamesManager.getGameIdWithPlayer(commandNickname);
            if(gameId < 0) {
                throw new RuntimeException();
            }
            // set server game
            if(gamesManager.getCommandResult().equals(CommandResult.CREATE_SERVER_GAME)) {
                rmiServerGames.put(gameId, new RmiServerGame(gamesManager.getGameById(gameId)));
            }
            virtualView.setServerGame(rmiServerGames.get(gameId));
        }

        // only for testing
        System.out.println(gamesManager.getCommandResult());
    }

    /**
     * Method that finds the VirtualView associated with a client with a certain nickname.
     * @param commandNickname client's nickname
     * @return virtual view
     * @throws RemoteException remote exception
     */
    private VirtualView getVirtualView(String commandNickname) throws RemoteException {
        for(VirtualView client : clients) {
            if(client.getNickname().equals(commandNickname)) {
                return client;
            }
        }
        return null;
    }
}
