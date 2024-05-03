package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.NicknameCheck;
import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.game_commands.GamesManagerCommand;
import it.polimi.ingsw.gc07.model.Player;
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
    private static RmiServerGamesManager myRmiServerGamesManager;
    /**
     * Virtual views of managed clients.
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

    /**
     * Method that starts the command executor, a thread that takes tasks
     * from the blocking queue and executes them.
     * Used to make RMI asynchronous.
     */
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

    /**
     * Method used to get the only existing instance of RmiServerGamesManager,
     * which uses the Singleton pattern.
     * @return RmiServerGamesManager instance
     */
    public static synchronized RmiServerGamesManager getRmiServerGamesManager() {
        if(myRmiServerGamesManager == null) {
            try {
                myRmiServerGamesManager = new RmiServerGamesManager();
            }catch(RemoteException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
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

    @Override
    public NicknameCheck checkNickname(String nickname) throws RemoteException {
        return GamesManager.getGamesManager().checkNickname(nickname);
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

    /**
     * Method that allows to set a RmiServerGame for the client.
     * @param nickname player's nickname
     * @param gameId game id
     */
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

    /**
     * Method that allows to create a new RmiServerGame, used when the player joins a new game.
     * Creates a new RmiServerGame and calls setServerGame to set it.
     * @param nickname player's nickname
     * @param gameId game id
     */
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

    /**
     * Method used to notify that joining was not successful.
     */
    public void notifyJoinNotSuccessful(String nickname) {
        try {
            VirtualView virtualView = getVirtualView(nickname);
            if(virtualView == null) {
                throw new RuntimeException();
            }
            virtualView.notifyJoinNotSuccessful();
        }catch(RemoteException e) {
            // TODO
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Method used to display existing games to a player who requests it.
     * @param nickname player's nickname
     */
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
     * @param nickname client's nickname
     * @return virtual view
     * @throws RemoteException remote exception
     */
    public VirtualView getVirtualView(String nickname) throws RemoteException {
        for(VirtualView client : clients) {
            if(client.getNickname().equals(nickname)) {
                return client;
            }
        }
        return null;
    }

    /**
     * Method used to delete the game server from the map once the game is ended.
     * @param gameId game id of the game to remove
     */
    public void deleteGame(int gameId) {
        GamesManager gamesManager = GamesManager.getGamesManager();
        GameController gameController = gamesManager.getGameById(gameId);

        // delete virtual views
        for(Player p: gameController.getPlayers()) {
            try {
                if(p.getConnectionType()) {
                    removeVirtualView(p.getNickname());
                }
            }catch(RemoteException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

        // delete server game
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

    public void removeVirtualView(String nickname) throws RemoteException {
        VirtualView virtualView = getVirtualView(nickname);
        clients.remove(virtualView);
    }
}
