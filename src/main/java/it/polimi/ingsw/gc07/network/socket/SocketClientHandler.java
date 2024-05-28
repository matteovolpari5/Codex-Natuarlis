package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.enumerations.NicknameCheck;
import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.network.SocketCommunication;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.updates.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;

/**
 * Class that manages the whole communication with a specific client
 */
public class SocketClientHandler implements VirtualView {
    private final GamesManager gamesManager;
    private GameController gameController;
    private final Socket mySocket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private  String myClientNickname;
    private boolean isReconnected;

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
                System.err.println("New client connected");
                manageGamesManagerCommand();
            }else{
                isReconnected = true;
                boolean interfaceType = input.readBoolean();
                gamesManager.setAndExecuteCommand(new ReconnectPlayerCommand(this, myClientNickname, false, interfaceType));
                manageGameCommand();
            }
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
        }
    }
    private void manageGamesManagerCommand(){
        GamesManagerCommand command;
        while(true) {
            try {
                command = (GamesManagerCommand) input.readObject();
                gamesManager.setAndExecuteCommand(command);
                if(gameController != null){
                    break;
                }
            } catch (IOException | ClassNotFoundException e){
                closeConnection();
                break;
            }
        }
        if(!mySocket.isClosed()){
            manageGameCommand();
        }
    }

    private void manageGameCommand(){
        GameControllerCommand command;
        while(true){
            try {
                command = (GameControllerCommand) input.readObject();
            }catch (IOException | ClassNotFoundException e){
                closeConnection();
                break;
            }
            synchronized (gameController) {
                gameController.setAndExecuteCommand(command);
                CommandResult result = gameController.getCommandResult();
                if(result != null && result.equals(CommandResult.DISCONNECTION_SUCCESSFUL)){
                    gameController.setCommandResult(myClientNickname, CommandResult.SUCCESS);
                    closeConnection();
                    break;
                }
            }
        }
    }

    private synchronized void closeConnection(){
        if(!mySocket.isClosed()){
            try{
                input.close();
                output.close();
                mySocket.close();
            }catch (IOException e){
                //throw new RuntimeException();
            }
        }
    }

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

    @Override
    public void receiveChatMessageUpdate(ChatMessageUpdate chatMessageUpdate) throws RemoteException {
        receiveUpdate(chatMessageUpdate);
    }

    @Override
    public void receiveDeckUpdate(DeckUpdate deckUpdate) throws RemoteException {
        receiveUpdate(deckUpdate);
    }

    @Override
    public void receiveStarterCardUpdate(StarterCardUpdate starterCardUpdate) throws RemoteException {
        receiveUpdate(starterCardUpdate);
    }

    @Override
    public void receivePlacedCardUpdate(PlacedCardUpdate placedCardUpdate) throws RemoteException {
        receiveUpdate(placedCardUpdate);
    }

    @Override
    public void receiveGameModelUpdate(GameModelUpdate gameModelUpdate) throws RemoteException {
        receiveUpdate(gameModelUpdate);
    }

    @Override
    public void receivePlayersUpdate(PlayersUpdate playerJoinedUpate) throws RemoteException {
        receiveUpdate(playerJoinedUpate);
    }

    @Override
    public void receiveCommandResultUpdate(CommandResultUpdate commandResultUpdate) throws RemoteException {
        receiveUpdate(commandResultUpdate);
    }

    @Override
    public void receiveStallUpdate(StallUpdate stallUpdate) throws RemoteException {
        receiveUpdate(stallUpdate);
    }

    @Override
    public void receiveConnectionUpdate(ConnectionUpdate connectionUpdate) throws RemoteException {
        receiveUpdate(connectionUpdate);
    }

    @Override
    public void receiveCardHandUpdate(CardHandUpdate cardHandUpdate) throws RemoteException {
        receiveUpdate(cardHandUpdate);
    }

    @Override
    public void receiveScoreUpdate(ScoreUpdate scoreUpdate) throws RemoteException {
        receiveUpdate(scoreUpdate);
    }

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

    @Override
    public void receiveGameEndedUpdate(GameEndedUpdate gameEndedUpdate) throws RemoteException {
        receiveUpdate(gameEndedUpdate);
    }

    @Override
    public void sendPong() throws RemoteException {
        receiveUpdate(new PongUpdate());
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

    /**
     * Method used to send a secret objectives update.
     * @param secretObjectivesUpdate secret objectives update
     * @throws RemoteException remote exception
     */
    @Override
    public void receiveSecretObjectivesUpdate(SecretObjectivesUpdate secretObjectivesUpdate) throws RemoteException {
        receiveUpdate(secretObjectivesUpdate);
    }
}