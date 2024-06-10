package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.controller.NicknameCheck;
import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.controller.CommandResult;
import it.polimi.ingsw.gc07.network.SocketCommunication;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.updates.*;
import it.polimi.ingsw.gc07.utils.SafePrinter;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * Class that manages the whole communication with a specific client.
 */
public class SocketClientHandler implements VirtualView {
    /**
     * Reference to games manager.
     */
    private final GamesManager gamesManager;
    /**
     * Reference to game controller.
     */
    private GameController gameController;
    /**
     * Socket used for communication.
     */
    private final Socket mySocket;
    /**
     * input channel used for communication.
     */
    private final ObjectInputStream input;
    /**
     * Output channel used for communication.
     */
    private final ObjectOutputStream output;
    /**
     * Name of the client associated.
     */
    private  String myClientNickname;
    /**
     * Boolean that is true if the client comes from a disconnection.
     */
    private boolean isReconnected;

    /**
     * Constructor of SocketClientHandler
     * @param mySocket socket connected to the client
     * @throws IOException I/O exception
     */
    public SocketClientHandler(Socket mySocket) throws IOException {
        InputStream temp_input;
        OutputStream temp_output;

        this.gamesManager = GamesManager.getGamesManager();
        this.gameController = null;
        this.mySocket = mySocket;

        temp_output = this.mySocket.getOutputStream();
        this.output = new ObjectOutputStream(temp_output);
        output.flush();

        temp_input = this.mySocket.getInputStream();
        this.input = new ObjectInputStream(temp_input);

        new Thread(this::manageSetUp).start();
    }

    /**
     * Method used to finalize the setup of the SocketClientHandler
     */
    private void manageSetUp(){
        NicknameCheck check;
        try {
            do{
                this.myClientNickname = (String) input.readObject();
                check = GamesManager.getGamesManager().checkNickname(myClientNickname);
                output.writeObject(check);
                output.reset();
                output.flush();
            }while(check.equals(NicknameCheck.EXISTING_NICKNAME));
            if(check.equals(NicknameCheck.NEW_NICKNAME)){
                isReconnected = false;
                GamesManager.getGamesManager().addVirtualView(myClientNickname, this);
                SafePrinter.println("New client connected");
                manageGamesManagerCommand();
            }else{
                isReconnected = true;
                boolean interfaceType = input.readBoolean();
                gamesManager.setAndExecuteCommand(new ReconnectPlayerCommand(this, myClientNickname, false, interfaceType));
                manageGameCommand();
            }
        } catch (IOException | ClassNotFoundException e) {
            try {
                closeConnection();
            } catch (RemoteException ex) {
                // exception can never be thrown
            }
        }
    }

    /**
     * Method used to receive and execute games manager command from the client
     */
    private void manageGamesManagerCommand(){
        GamesManagerCommand command;
        boolean isSocketClosed;
        while(true) {
            synchronized (this){
                isSocketClosed = mySocket.isClosed();
            }
            if(isSocketClosed){
                return;
            }
            try {
                command = (GamesManagerCommand) input.readObject();
                gamesManager.setAndExecuteCommand(command);
                if(gameController != null){
                    break;
                }
            } catch (IOException | ClassNotFoundException e){
                try {
                    closeConnection();
                } catch (RemoteException ex) {
                    // exception can never be thrown
                }
                break;
            }
        }
        synchronized (this){
            isSocketClosed = mySocket.isClosed();
        }
        if(!isSocketClosed){
            manageGameCommand();
        }
    }

    /**
     * Method used to receive and execute game command from the client
     */
    private void manageGameCommand(){
        GameControllerCommand command;
        boolean isSocketClosed;
        while(true){
            synchronized (this){
                isSocketClosed = mySocket.isClosed();
            }
            if(isSocketClosed){
                break;
            }
            try {
                command = (GameControllerCommand) input.readObject();
            }catch (IOException | ClassNotFoundException e){
                try {
                    closeConnection();
                } catch (RemoteException ex) {
                    // exception can never be thrown
                }
                break;
            }
            synchronized (gameController) {
                gameController.setAndExecuteCommand(command);
                CommandResult result = gameController.getCommandResult();
                if(result != null && result.equals(CommandResult.DISCONNECTION_SUCCESSFUL) && !gameController.isPlayerConnected(myClientNickname)){
                    gameController.setCommandResult(myClientNickname, CommandResult.SUCCESS);
                    try {
                        closeConnection();
                    } catch (RemoteException e) {
                        // exception can never be thrown
                    }
                    break;
                }
            }
        }
    }

    /**
     * Method used to close the socket connection when a disconnection is detected
     */
    public synchronized void closeConnection() throws RemoteException{
        while(!mySocket.isClosed()){
            try{
                input.close();
                output.close();
                mySocket.close();
            }catch (IOException e){
                e.printStackTrace();
                //throw new RuntimeException();
            }
        }
    }

    /**
     * Method that sends updates to the client
     * @param update update to be sent
     * @throws RemoteException remote exception
     */
    private synchronized void receiveUpdate(Update update) throws RemoteException{
        if(!mySocket.isClosed()){
            try {
                output.writeObject(update);
                output.reset();
                output.flush();
            }catch(IOException e) {
                closeConnection();
                throw new RemoteException();
            }
        }else{
            throw new RemoteException();
        }
    }

    /**
     * Method used to set the game controller
     * @param gameId game id
     * @throws RemoteException remote exception
     */
    @Override
    public void setServerGame(int gameId) throws RemoteException {
        if(!isReconnected){
            try {
                output.writeObject(SocketCommunication.GAME_JOINED);
                output.reset();
                output.flush();
            } catch (IOException e) {
                closeConnection();
                throw new RemoteException();
            }
        }
        this.gameController = gamesManager.getGameById(gameId);
    }

    /**
     * Method used to send an update to notify the player he has received a new chat chatMessage.
     * @param chatMessageUpdate chat message update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveChatMessageUpdate(ChatMessageUpdate chatMessageUpdate) throws RemoteException {
        receiveUpdate(chatMessageUpdate);
    }

    /**
     * Method used to send to the client a deck update.
     * @param deckUpdate deck update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveDeckUpdate(DeckUpdate deckUpdate) throws RemoteException {
        receiveUpdate(deckUpdate);
    }

    /**
     * Method used to send to the client his starter card.
     * @param starterCardUpdate starter card update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveStarterCardUpdate(StarterCardUpdate starterCardUpdate) throws RemoteException {
        receiveUpdate(starterCardUpdate);
    }

    /**
     * Method used to notify the player that a card has been placed.
     * @param placedCardUpdate placed card update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receivePlacedCardUpdate(PlacedCardUpdate placedCardUpdate) throws RemoteException {
        receiveUpdate(placedCardUpdate);
    }

    /**
     * Method used to send a game model update.
     * @param gameModelUpdate game model update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveGameModelUpdate(GameModelUpdate gameModelUpdate) throws RemoteException {
        receiveUpdate(gameModelUpdate);
    }

    /**
     * Method used to send a player joined update.
     * @param playerJoinedUpate player joined update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receivePlayersUpdate(PlayersUpdate playerJoinedUpate) throws RemoteException {
        receiveUpdate(playerJoinedUpate);
    }

    /**
     * Method used to send a command result update.
     * @param commandResultUpdate command result update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveCommandResultUpdate(CommandResultUpdate commandResultUpdate) throws RemoteException {
        receiveUpdate(commandResultUpdate);
    }

    /**
     * Method used to send a stall update.
     * @param stallUpdate stall update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveStallUpdate(StallUpdate stallUpdate) throws RemoteException {
        receiveUpdate(stallUpdate);
    }

    /**
     * Method used to send a connection update.
     * @param connectionUpdate connection update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveConnectionUpdate(ConnectionUpdate connectionUpdate) throws RemoteException {
        receiveUpdate(connectionUpdate);
    }

    /**
     * Method used to send a card hand update.
     * @param cardHandUpdate card hand update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveCardHandUpdate(CardHandUpdate cardHandUpdate) throws RemoteException {
        receiveUpdate(cardHandUpdate);
    }

    /**
     * Method used to send to the client an updated score.
     * @param scoreUpdate score update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveScoreUpdate(ScoreUpdate scoreUpdate) throws RemoteException {
        receiveUpdate(scoreUpdate);
    }

    /**
     * Method used to send to the client existing games.
     * @param existingGamesUpdate existing games update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveExistingGamesUpdate(ExistingGamesUpdate existingGamesUpdate) throws RemoteException {
            try {
                output.writeObject(SocketCommunication.DISPLAY_SUCCESSFUL);
                output.reset();
                output.flush();
            } catch (IOException e) {
                closeConnection();
                throw new RemoteException();
            }
        receiveUpdate(existingGamesUpdate);
    }

    /**
     * Method used notify the client that the joining was not successful.
     * @throws RemoteException remote exception
     */
    @Override
    public void notifyJoinNotSuccessful() throws RemoteException {
        try {
            output.writeObject(SocketCommunication.ACTION_NOT_SUCCESSFUL);
            output.reset();
            output.flush();
        } catch (IOException e) {
            closeConnection();
            throw new RemoteException();
        }
    }

    /**
     * Method used to send to the client an update telling the game is ended and containing winners.
     * @param gameEndedUpdate game ended update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveGameEndedUpdate(GameEndedUpdate gameEndedUpdate) throws RemoteException {
        receiveUpdate(gameEndedUpdate);
    }

    /**
     * Method used to send a pong to the client.
     * @throws RemoteException remote exception
     */
    @Override
    public void sendPong() throws RemoteException {
        receiveUpdate(new PongUpdate());
    }

    /**
     * Method used to send to the player the full content of the chat after a reconnection.
     * @param fullChatUpdate full message update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveFullChatUpdate(FullChatUpdate fullChatUpdate) throws RemoteException {
        receiveUpdate(fullChatUpdate);
    }

    /**
     * Method used to send to the player the full game field after a reconnection.
     * @param fullGameFieldUpdate full game field content to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveFullGameFieldUpdate(FullGameFieldUpdate fullGameFieldUpdate) throws RemoteException {
        receiveUpdate(fullGameFieldUpdate);
    }

    /**
     * Method used to send a secret objectives update.
     * @param secretObjectivesUpdate secret objectives update to be sent
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveSecretObjectivesUpdate(SecretObjectivesUpdate secretObjectivesUpdate) throws RemoteException {
        receiveUpdate(secretObjectivesUpdate);
    }
}