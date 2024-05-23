package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.network.*;
import it.polimi.ingsw.gc07.updates.*;
import it.polimi.ingsw.gc07.view.Ui;
import it.polimi.ingsw.gc07.view.gui.Gui;
import it.polimi.ingsw.gc07.view.tui.Tui;
import javafx.application.Application;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class RmiClient extends UnicastRemoteObject implements Client, VirtualView, PingSender {
    /**
     * Nickname of the player associated to the RmiClient.
     */
    private final String nickname;
    /**
     * Send ping attribute.
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
    private final BlockingDeque<Update> updatesQueue;
    /**
     * Player's local copy of the game model.
     */
    private final GameView gameView;
    /**
     * Player's ui.
     */
    private final Ui ui;
    /**
     * Boolean that is true if the server is on
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
     * @throws RemoteException remote exception
     */
    public RmiClient(String nickname, boolean interfaceType, VirtualServerGamesManager serverGamesManager) throws RemoteException {
        this.nickname = nickname;
        this.serverGamesManager = serverGamesManager;
        this.serverGame = null;
        this.gameView = new GameView(nickname);
        this.clientAlive = true;
        if(interfaceType) {
            Application.launch(Gui.class);
            this.ui = Gui.getGuiInstance();
            this.ui.setNickname(nickname);
            this.ui.setClient(this);
        }else {
            this.ui = new Tui(nickname, this);
        }
        this.gameView.addViewListener(ui);
        this.pong = true;
        this.updatesQueue = new LinkedBlockingDeque<>();
        startUpdateExecutor();
    }

    /**
     * Getter method for isClientAlive.
     * @return value of isClientAlive
     */
    @Override
    public synchronized boolean isClientAlive() {
        return clientAlive;
    }

    /**
     * Setter method for isClientAlive.
     * @param isAlive value of isClientAlive
     */
    @Override
    public synchronized void setClientAlive(boolean isAlive) {
        this.clientAlive = isAlive;
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
            System.out.println("\nConnection failed. - connectToGamesManagerServer\n");
            setClientAlive(false);
        }
    }

    /**
     * Method used from RmiServerGamesManager to restart the cli if the joining was not successful.
     */
    public void notifyJoinNotSuccessful() throws RemoteException {
        System.out.println("\nCould not add you to the game, retry.\n");
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
            System.out.println("\nConnection failed. - reconnectPlayer\n");
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
            System.out.println("\nConnection failed.- setServerGame\n");
            setClientAlive(false);
        }
        // game joined
        new Thread(this::startGamePing).start();
        new Thread(this::checkPong).start();
        new Thread(this::runGameInterface).start();
    }

    /**
     * Method used to start the user interface.
     */
    public void startInterface() {
        assert(ui != null);
        ui.runJoinGameInterface();
    }

    /**
     * Method used to run the lobby ui.
     */
    public void runJoinGameInterface() {
        assert(ui != null);
        ui.runJoinGameInterface();
    }

    /**
     * Method used to run the game ui.
     */
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
            System.out.println("\nConnection failed. - setAndExecuteCommand\n");
            setClientAlive(false);  // setter is synchronized
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
            System.out.println("\nConnection failed.- setAndExecuteCommand\n");
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
                System.out.println("Connection failed. Press enter. - ping");
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
                        System.out.println("\nConnection failed - missed pong.\n");
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
    private void receiveUpdate(Update update) {
        try {
            // blocking queues are thread safe
            updatesQueue.put(update);
        }catch(InterruptedException e) {
            throw new RuntimeException();
        }
        setPong();
    }

    /**
     * Method thar starts a new thread, that takes updates from the blocking queue and executes them.
     */
    private void startUpdateExecutor() {
        new Thread(() -> {
            while(isClientAlive()) {
                try {
                    Update update = updatesQueue.take();
                    update.execute(gameView);
                }catch(InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        }).start();
    }

    /**
     * Method used to notify the player he has received a new chat chatMessage.
     * @param chatMessageUpdate chat message update
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveChatMessageUpdate(ChatMessageUpdate chatMessageUpdate) throws RemoteException {
        receiveUpdate(chatMessageUpdate);
    }

    /**
     * Method used to show the client his starter card.
     * @param starterCardUpdate starter card update
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveStarterCardUpdate(StarterCardUpdate starterCardUpdate) throws RemoteException {
        receiveUpdate(starterCardUpdate);
    }

    /**
     * Method used to notify players that a card has been placed.
     * @param placedCardUpdate placed card update
     * @throws RemoteException remote exception
     */
    @Override
    public void receivePlacedCardUpdate(PlacedCardUpdate placedCardUpdate) throws RemoteException {
        receiveUpdate(placedCardUpdate);
    }

    /**
     * Method used to notify a game model update.
     * @param gameModelUpdate game model update
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveGameModelUpdate(GameModelUpdate gameModelUpdate) throws RemoteException {
        receiveUpdate(gameModelUpdate);
    }

    /**
     * Method used to notify a player joined update.
     * @param playerJoinedUpdate player joined update
     * @throws RemoteException remote exception
     */
    @Override
    public void receivePlayerJoinedUpdate(PlayerJoinedUpdate playerJoinedUpdate) throws RemoteException {
        receiveUpdate(playerJoinedUpdate);
    }

    /**
     * Method used to notify a command result update.
     * @param commandResultUpdate command result update
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveCommandResultUpdate(CommandResultUpdate commandResultUpdate) throws RemoteException {
        receiveUpdate(commandResultUpdate);
    }

    /**
     * Method used to send a stall update.
     * @param stallUpdate stall update
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveStallUpdate(StallUpdate stallUpdate) throws RemoteException {
        receiveUpdate(stallUpdate);
    }

    /**
     * Method used to send a connection update.
     * @param connectionUpdate connection update
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveConnectionUpdate(ConnectionUpdate connectionUpdate) throws RemoteException {
        receiveUpdate(connectionUpdate);
    }

    /**
     * Method used to send a card hand update.
     * @param cardHandUpdate card hand update
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveCardHandUpdate(CardHandUpdate cardHandUpdate) throws RemoteException {
        receiveUpdate(cardHandUpdate);
    }

    /**
     * Method used to show the client an updated score.
     * @param scoreUpdate score update
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveScoreUpdate(ScoreUpdate scoreUpdate) throws RemoteException {
        receiveUpdate(scoreUpdate);
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
     * Method used to show the client a deck update.
     * @param deckUpdate deck update
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveDeckUpdate(DeckUpdate deckUpdate) throws RemoteException {
        receiveUpdate(deckUpdate);
    }

    /**
     * Method used to show the client an update telling the game is ended and containing winners.
     * @param gameEndedUpdate game ended update
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveGameEndedUpdate(GameEndedUpdate gameEndedUpdate) throws RemoteException {
        receiveUpdate(gameEndedUpdate);
    }

    /**
     * Method used to notify the player the full content of the chat after a reconnection.
     * @param fullChatUpdate full message update
     */
    @Override
    public void receiveFullChatUpdate(FullChatUpdate fullChatUpdate) throws RemoteException {
        receiveUpdate(fullChatUpdate);
    }

    /**
     * Method used to notify players the full game field after a reconnection.
     * @param fullGameFieldUpdate full game field content
     */
    @Override
    public void receiveFullGameFieldUpdate(FullGameFieldUpdate fullGameFieldUpdate) throws RemoteException {
        receiveUpdate(fullGameFieldUpdate);
    }
}
