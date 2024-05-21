package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.enumerations.NicknameCheck;
import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.updates.*;

import java.io.*;
import java.net.Socket;
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

    public SocketClientHandler(GamesManager gamesManager, Socket mySocket) throws IOException {
        System.out.println("SCH> costruttore");
        InputStream temp_input;
        OutputStream temp_output;

        this.gamesManager = gamesManager;
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
        System.out.println("SCH-T> manageSetUp");
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
                System.err.println("SCH-T> New client connected");
                manageGamesManagerCommand();
            }else{
                isReconnected = true;
                boolean interfaceType = input.readBoolean();
                gamesManager.setAndExecuteCommand(new ReconnectPlayerCommand(this, myClientNickname, false, interfaceType));
                manageGameCommand();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\nSCH-T> Connection failed.\n");
            closeConnection();
        }
    }
    private void manageGamesManagerCommand(){
        System.out.println("SCH-T> manageGMCommand");
        GamesManagerCommand command;
        while(true) {
            try {
                command = (GamesManagerCommand) input.readObject();
                gamesManager.setAndExecuteCommand(command);
                if(gameController != null){
                    break;
                }
            } catch (Exception e){
                System.out.println("\nSCH-T> Connection failed.\n");
                closeConnection();
                break;
            }
        }
        manageGameCommand();
    }

    private void manageGameCommand(){
        System.out.println("SCH-T> manageGCommand");
        GameControllerCommand command;
        while(true){
            try{
                command = (GameControllerCommand) input.readObject();
                synchronized (gameController) {
                    gameController.setAndExecuteCommand(command);
                    CommandResult result = gameController.getCommandResult();
                    if(result != null && result.equals(CommandResult.DISCONNECTION_SUCCESSFUL)){
                        // TODO closeConnection(mySocket,input,output);
                    }
                }
            } catch (Exception e){
                closeConnection();
                break;
            }
        }
    }

    private void closeConnection(){
        try{
            input.close();
            output.close();
            mySocket.close();
            System.out.println("SCH> Closed connection");
        }catch (IOException e){
            System.out.println("SCH - closeConnection> Error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void setServerGame(int gameId) throws RemoteException {
        if(!isReconnected){
            String result = "Game joined.";
            System.out.println(result);
            try {
                output.writeObject(result);
                output.reset();
                output.flush();
            } catch (IOException e) {
                System.out.println("\nSCH-T(1)> Connection failed.\n");
                closeConnection();
            }
        }
        this.gameController = gamesManager.getGameById(gameId);
    }

    private synchronized void receiveUpdate(Update update) {
        try {
            //TODO forse verificare con isConnected()
            output.writeObject(update);
            output.reset();
            output.flush();
        }catch(IOException e) {
            System.out.println("\nSCH-T(2)> Connection failed.\n");
            closeConnection();
        }
    }


    @Override
    public void receiveChatMessageUpdate(ChatMessageUpdate chatMessageUpdate) {
        receiveUpdate(chatMessageUpdate);
    }

    @Override
    public void receiveDeckUpdate(DeckUpdate deckUpdate) {
        receiveUpdate(deckUpdate);
    }

    @Override
    public void receiveStarterCardUpdate(StarterCardUpdate starterCardUpdate) {
        receiveUpdate(starterCardUpdate);
    }

    @Override
    public void receivePlacedCardUpdate(PlacedCardUpdate placedCardUpdate) {
        receiveUpdate(placedCardUpdate);
    }

    @Override
    public void receiveGameModelUpdate(GameModelUpdate gameModelUpdate) {
        receiveUpdate(gameModelUpdate);
    }

    @Override
    public void receivePlayerJoinedUpdate(PlayerJoinedUpdate playerJoinedUpate) throws RemoteException {
        receiveUpdate(playerJoinedUpate);
    }

    @Override
    public void receiveCommandResultUpdate(CommandResultUpdate commandResultUpdate) {
        receiveUpdate(commandResultUpdate);
    }

    @Override
    public void receiveStallUpdate(StallUpdate stallUpdate) {
        receiveUpdate(stallUpdate);
    }

    @Override
    public void receiveConnectionUpdate(ConnectionUpdate connectionUpdate) {
        receiveUpdate(connectionUpdate);
    }

    @Override
    public void receiveCardHandUpdate(CardHandUpdate cardHandUpdate) {
        receiveUpdate(cardHandUpdate);
    }

    @Override
    public void receiveScoreUpdate(ScoreUpdate scoreUpdate) {
        receiveUpdate(scoreUpdate);
    }

    @Override
    public void receiveExistingGamesUpdate(ExistingGamesUpdate existingGamesUpdate) throws RemoteException {
        synchronized (this) {
            try {
                output.writeObject("Display successful.");
                output.reset();
                output.flush();
            } catch (IOException e) {
                System.out.println("\nSCH-T(3)> Connection failed.\n");
                closeConnection();
            }
        }
        receiveUpdate(existingGamesUpdate);
    }

    @Override
    public synchronized void notifyJoinNotSuccessful() throws RemoteException {
        String error = "Action not successful.";
        try {
            output.writeObject(error);
            output.reset();
            output.flush();
        } catch (IOException e) {
            System.out.println("\nSCH-T(4)> Connection failed.\n");
            closeConnection();
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
}