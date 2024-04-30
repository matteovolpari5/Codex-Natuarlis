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
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class RmiServerGamesManager extends UnicastRemoteObject implements VirtualServerGamesManager {
    /**
     * Instance of RmiServerGamesManager.
     */
    private static volatile RmiServerGamesManager myRmiServerGamesManager = null;
    /**
     * Virtual views of connected clients.
     */
    private final List<VirtualView> clients;
    /**
     * Map containing the RmiServerGame of every game.
     */
    private final Map<Integer, RmiServerGame> rmiServerGames;
    /**
     * Queue containing commands to execute.
     */
    private final BlockingDeque<GamesManagerCommand> commandsQueue;

    /**
     * Constructor of class RmiServerGamesManager.
     */
    private RmiServerGamesManager() throws RemoteException {
        this.clients = new ArrayList<>();
        this.rmiServerGames = new HashMap<>();
        this.commandsQueue = new LinkedBlockingDeque<>();
        startCommandExecutor();
    }

    private void startCommandExecutor() {
        new Thread(() -> {
            while(true) {
                try {
                    GamesManagerCommand command = commandsQueue.take();
                    GamesManager.getGamesManager().setAndExecuteCommand(command);

                    // only for testing
                    System.out.println(GamesManager.getGamesManager().getCommandResult());
                }catch(InterruptedException e) {
                    System.err.println("Channel closed");
                    break;
                }
            }
        }).start();
    }

    public static synchronized RmiServerGamesManager getRmiServerGamesManager() {
        if(myRmiServerGamesManager == null) {
            try {
                myRmiServerGamesManager = new RmiServerGamesManager();
                System.out.println("creating new");
            }catch(RemoteException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        System.out.println("Passing");
        return myRmiServerGamesManager;
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
        // add command to queue
        try {
            // blocking queues are thread safe
            commandsQueue.put(gamesManagerCommand);
        }catch(InterruptedException e) {
            // TODO
            e.printStackTrace();
            throw new RemoteException();
        }
    }

    public void setServerGame(String nickname, int gameId) {
        assert(GamesManager.getGamesManager().getCommandResult().equals(CommandResult.SET_SERVER_GAME)): "Wrong method call";
        assert(rmiServerGames.containsKey(gameId));
        try {
            VirtualView virtualView = getVirtualView(nickname);
            if(virtualView == null) {
                throw new RuntimeException();
            }
            virtualView.setServerGame(rmiServerGames.get(gameId));
        }catch(RemoteException e) {
            // TODO
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void createServerGame(String nickname, int gameId) {
        assert(GamesManager.getGamesManager().getCommandResult().equals(CommandResult.CREATE_SERVER_GAME)): "Wrong method call";
        try {
            VirtualView virtualView = getVirtualView(nickname);
            if(virtualView == null) {
                throw new RuntimeException();
            }
            rmiServerGames.put(gameId, new RmiServerGame(GamesManager.getGamesManager().getGameById(gameId)));
            virtualView.setServerGame(rmiServerGames.get(gameId));
        }catch(RemoteException e) {
            // TODO
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void displayGames(String nickname) {
        assert(GamesManager.getGamesManager().getCommandResult().equals(CommandResult.DISPLAY_GAMES)): "Wrong method call";
        try {
            // get virtual view
            VirtualView virtualView = getVirtualView(nickname);
            if(virtualView == null) {
                throw new RuntimeException();
            }
            ExistingGamesUpdate update = new ExistingGamesUpdate(GamesManager.getGamesManager().getFreeGamesDetails());
            virtualView.receiveExistingGamesUpdate(update);
        }catch(RemoteException e) {
            // TODO
            e.printStackTrace();
            throw new RuntimeException();
        }
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

    public void deleteServerGame(int gameId) {
        RmiServerGame rmiServerGame = null;
        for(int id: rmiServerGames.keySet()) {
            if(id == gameId) {
                rmiServerGame = rmiServerGames.get(id);
            }
        }
        if(rmiServerGame != null){
            rmiServerGames.remove(gameId);
        }
    }
}
