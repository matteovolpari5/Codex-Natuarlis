package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.enumerations.NicknameCheck;
import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.network.SocketCommunication;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.updates.*;
import it.polimi.ingsw.gc07.utils.SafePrinter;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;

/**
 * Class that manages the whole communication with a specific client
 */
public class
SocketClientHandler implements VirtualView {
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
                SafePrinter.println("New client connected");
                manageGamesManagerCommand();
            }else{
                isReconnected = true;
                boolean interfaceType = input.readBoolean();
                gamesManager.setAndExecuteCommand(new ReconnectPlayerCommand(this, myClientNickname, false, interfaceType));
                manageGameCommand();
            }
        } catch (IOException | ClassNotFoundException e) {
            //System.out.println("manage setup - " + myClientNickname);
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
                //System.out.println("manage games manager command - " + myClientNickname);
                closeConnection();
                break;
            }
        }
        boolean isSocketClosed;
        synchronized (this){
            isSocketClosed = mySocket.isClosed();
        }
        if(!isSocketClosed){
            manageGameCommand();
        }
    }

    private void manageGameCommand(){
        GameControllerCommand command;
        while(true){
            try {
                command = (GameControllerCommand) input.readObject();
            }catch (IOException | ClassNotFoundException e){
                //System.out.println("manage game command 1 - " + myClientNickname);
                closeConnection();
                break;
            }
            synchronized (gameController) {
                //System.out.println(command.getClass());
                gameController.setAndExecuteCommand(command);
                CommandResult result = gameController.getCommandResult();
                if(result != null && result.equals(CommandResult.DISCONNECTION_SUCCESSFUL) && !gameController.isPlayerConnected(myClientNickname)){
                    gameController.setCommandResult(myClientNickname, CommandResult.SUCCESS);
                    //System.out.println("manage game command 2 - " + myClientNickname);
                    closeConnection();
                    break;
                }
            }
        }
    }

    private synchronized void closeConnection(){
        while(!mySocket.isClosed()){
            SafePrinter.println("DISCONNECTED: " + myClientNickname);
            Thread.dumpStack();
            //System.err.println("CHIUDO IL SOCKET DI : " + myClientNickname);
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

    private synchronized void receiveUpdate(Update update) throws RemoteException{
        //System.out.println(update.getClass());
        if(!mySocket.isClosed()){
            try {
                output.writeObject(update);
                //System.out.println("A");
                output.reset();
                //System.out.println("B");
                output.flush();
                //System.out.println("C");
            }catch(IOException e) {
                //System.out.println("1");
                //e.printStackTrace(); da togliere
                //System.out.println("receive update - " +myClientNickname);
                closeConnection();
                throw new RemoteException();
            }
        }else{
            //System.err.println(myClientNickname + "Socket già chiuso");
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
                //System.out.println("set server game - " + myClientNickname);
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
                //System.out.println("receive existing games update - " +myClientNickname);
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
            //System.out.println("notify join not successful - "+myClientNickname);
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