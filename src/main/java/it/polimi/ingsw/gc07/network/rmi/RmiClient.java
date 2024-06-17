package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.network.*;
import it.polimi.ingsw.gc07.updates.*;
import it.polimi.ingsw.gc07.utils.SafePrinter;
import it.polimi.ingsw.gc07.view.Ui;
import it.polimi.ingsw.gc07.view.gui.Gui;
import it.polimi.ingsw.gc07.view.tui.Tui;
import javafx.application.Application;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class representing a client using Rmi.
 */
public class RmiClient extends UnicastRemoteObject implements Client, VirtualView, PingSender {
    /**
     * Nickname of the player associated to the RmiClient.
     */
    private final String nickname;
    /**
     * Boolean value that is true if the client is connected to the server.
     * When this value is false, a disconnection occurred.
     */
    private boolean clientAlive;
    /**
     * Reference to RmiServerGamesManager, the general server.
     */
    private final VirtualServerGamesManager serverGamesManager;
    /**
     * Reference to RmiServerGame, the game specific server.
     */
    private VirtualServerGame serverGame;
    /**
     * Blocking queue containing received updates.
     */
    private final BlockingQueue<Update> updatesQueue;
    /**
     * Player's local copy of the game model.
     */
    private final GameView gameView;
    /**
     * Player's ui.
     */
    private final Ui ui;
    /**
     * Boolean that is true if the server is on.
     */
    private boolean pong;
    /**
     * Number of missed pongs to detect a disconnection.
     */
    private static final int maxMissedPongs = 3;

    /**
     * Constructor of RmiClient.
     * @param serverGamesManager general server
     * @param nickname nickname
     * @param interfaceType player's interface type
     * @throws RemoteException remote exception
     */
    public RmiClient(String nickname, boolean interfaceType, VirtualServerGamesManager serverGamesManager) throws RemoteException {
        this.nickname = nickname;
        this.serverGamesManager = serverGamesManager;
        this.serverGame = null;
        this.gameView = new GameView(nickname);
        this.clientAlive = true;
        if(interfaceType) {
            // run application on new thread
            new Thread(() -> {
                Application.launch(Gui.class);
                System.exit(0);
            }).start();
            this.ui = Gui.getGuiInstance();
            this.ui.setNickname(nickname);
            this.ui.setClient(this);
        }else {
            this.ui = new Tui(nickname, this);
        }
        this.gameView.addViewListener(ui);
        this.pong = true;
        this.updatesQueue = new LinkedBlockingQueue<>();
        startUpdateExecutor();
    }

    /**
     * Getter method for clientAlive.
     * @return value of clientAlive
     */
    @Override
    public synchronized boolean isClientAlive() {
        return clientAlive;
    }

    /**
     * Setter method for clientAlive.
     * @param isAlive value of clientAlive
     */
    @Override
    public synchronized void setClientAlive(boolean isAlive) {
        this.clientAlive = isAlive;

        // stop ui
        if(!isAlive) {
            ui.stopUi();
        }
    }

    /**
     * Getter method for gameView.
     * @return client's game views
     */
    @Override
    public GameView getGameView() {
        return gameView;
    }

    /**
     * Method used to set pong to true when a pong is received.
     */
    private synchronized void setPong() {
        this.pong = true;
    }

    /**
     * Method that allows the client to connect with RmiServerGamesManager, the general server.
     * @param interfaceType interface type
     */
    public void connectToGamesManagerServer(boolean interfaceType) {
        boolean connectionType = true; // Rmi client
        try {
            serverGamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand(nickname, connectionType, interfaceType));
            serverGamesManager.connect(nickname, this);
        }catch(RemoteException e) {
            System.exit(-1);
        }
    }

    /**
     * Method used from RmiServerGamesManager to restart the cli if the joining was not successful.
     * @throws RemoteException remote exception
     */
    @Override
    public void notifyJoinNotSuccessful() throws RemoteException {
        SafePrinter.println("\nCould not add you to the game, retry.\n");
        new Thread(this::runJoinGameInterface).start();
    }

    /**
     * Method that allows a player to reconnect to a game with Rmi.
     * @param nickname nickname
     * @param interfaceType new interface type
     */
    public void reconnectPlayer(String nickname, boolean interfaceType) {
        boolean connectionType = true; // Rmi client
        try {
            serverGamesManager.setAndExecuteCommand(new ReconnectPlayerCommand(this, nickname, connectionType, interfaceType));
        } catch (RemoteException e) {
            SafePrinter.println("\nConnection failed.\n");
            setClientAlive(false);
        }
    }

    /**
     * Method that allows to set a RmiServerGame, the game specific server, for the client.
     * @param gameId game id
     * @throws RemoteException remote exception
     */
    @Override
    public void setServerGame(int gameId) throws RemoteException {
        try {
            this.serverGame = serverGamesManager.getGameServer(gameId);
        }catch(RemoteException e) {
            SafePrinter.println("\nConnection failed.\n");
            setClientAlive(false);
        }
        // game joined
        new Thread(this::startGamePing).start();
        new Thread(this::checkPong).start();
        new Thread(this::runGameInterface).start();
    }

    /**
     * Method used to run the lobby ui.
     */
    @Override
    public void runJoinGameInterface() {
        assert(ui != null);
        ui.runJoinGameInterface();
    }

    /**
     * Method used to run the game ui.
     */
    @Override
    public void runGameInterface() {
        assert(ui != null);
        ui.runGameInterface();
    }

    /**
     * Method to execute a games manager command.
     * @param gamesManagerCommand command to execute
     */
    @Override
    public void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) {
        try {
            serverGamesManager.setAndExecuteCommand(gamesManagerCommand);
        }catch(RemoteException e) {
            // if not already detected by ping
            SafePrinter.println("\nConnection failed.\n");
            setClientAlive(false);  // setter is synchronized
            ui.runJoinGameInterface();
        }
    }

    /**
     * Method to execute a game command.
     * @param gameCommand command to execute
     */
    @Override
    public void setAndExecuteCommand(GameControllerCommand gameCommand) {
        try {
            serverGame.setAndExecuteCommand(gameCommand);
        }catch(RemoteException e) {
            // if not already detected by ping
            SafePrinter.println("\nConnection failed.\n");
            setClientAlive(false);  // setter is synchronized
        }
    }

    /**
     * Method used to periodically send pings to the server.
     */
    @Override
    public void startGamePing() {
        while(true) {
            if(!isClientAlive()) {
                break;
            }
            try {
                serverGame.setAndExecuteCommand(new SendPingCommand(nickname));
            }catch(RemoteException e) {
                // connection failed
                SafePrinter.println("Connection failed.");
                setClientAlive(false);  // setter is synchronized
            }
            try {
                Thread.sleep(1000); // wait one second between two ping
            }catch(InterruptedException e) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * Remote method, used to send a pong to the client.
     * @throws RemoteException remote exception
     */
    @Override
    public void sendPong() throws RemoteException {
        setPong();
    }

    /**
     * Method used to close the connection when a disconnection is detected
     */
    @Override
    public void closeConnection() throws RemoteException {
    }

    /**
     * Method that periodically checks if the client is receiving pongs from the server.
     */
    private void checkPong() {
        int missedPong = 0;
        while(true) {
            if(!isClientAlive()) {
                // can be set by others
                break;
            }
            synchronized(this) {
                if(pong) {
                    missedPong = 0;
                }else {
                    missedPong ++;
                    if(missedPong >= maxMissedPongs) {
                        SafePrinter.println("\nConnection failed.\n");
                        setClientAlive(false);
                        break;
                    }
                }
                pong = false;
            }
            try {
                Thread.sleep(1000); // wait one second between two pong checks
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * Method that manages received updates by inserting them in a blocking queue.
     * @param update received update
     */
    public void receiveUpdate(Update update) {
        try {
            // blocking queues are thread safe
            updatesQueue.put(update);
        }catch(InterruptedException e) {
            throw new RuntimeException();
        }
        setPong();
    }

    /**
     * Method used to show the client existing games.
     * @param existingGamesUpdate existing games update
     * @throws RemoteException remote exception
     */
    @Override
    public synchronized void receiveExistingGamesUpdate(ExistingGamesUpdate existingGamesUpdate) throws RemoteException {
        receiveUpdate(existingGamesUpdate);
    }

    /**
     * Method thar starts a new thread, that takes updates from the blocking queue and executes them.
     */
    private void startUpdateExecutor() {
        new Thread(() -> {
            while(isClientAlive()) {
                Update update;
                try {
                    update = updatesQueue.take();
                }catch(InterruptedException e) {
                    throw new RuntimeException();
                }
                update.execute(gameView);
            }
        }).start();
    }
}

