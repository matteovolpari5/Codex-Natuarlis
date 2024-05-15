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
                manageGamesManagerCommand();
            }else{
                isReconnected = true;
                boolean interfaceType = input.readBoolean();
                synchronized (gamesManager){
                    gamesManager.setAndExecuteCommand(new ReconnectPlayerCommand(this, myClientNickname, false, interfaceType));
                }
                manageGameCommand();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void manageGamesManagerCommand(){
        System.out.println("SCH-T> manageGMCommand");
        GamesManagerCommand command;

        while(true) {
            try {
                command = (GamesManagerCommand) input.readObject();
                synchronized (gamesManager){
                    gamesManager.setAndExecuteCommand(command);
                    if(gameController != null){
                        break;
                    }
                }//TODO come gestire se il command ha avuto esito negativo? In rmi non viene controllato da nessuno l'esito del command
            } catch (Exception e){
                //TODO gestire eccezione
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
                synchronized (gameController){
                    gameController.setAndExecuteCommand(command);
                    CommandResult result = gameController.getCommandResult(); // TODO SendPingCommand non modifica commandResult
                    if(result != null && result.equals(CommandResult.DISCONNECTION_SUCCESSFUL)){
                        //closeConnection(mySocket,input,output);
                    }
                    //TODO come prima: in rmi nessuno controlla l'esito del command
                    /*if(result.equals(CommandResult.SUCCESS)){
                        //TODO mostrare esito
                    }else{
                        //TODO mostrare errore
                    }*/
                }
            } catch (Exception e){
                //TODO gestire eccezione
                e.printStackTrace();
                throw new RuntimeException();
                //closeConnection(mySocket,input,output);
            }
        }
        //closeConnection(mySocket, input, output);
    }

    public void closeConnection(){
        try{
            input.close();
            output.close();
            mySocket.close();
            System.out.println("SCH> Closed connection");
        }catch (IOException e){
            System.out.println("SCH> Error while closing connection");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public String getNickname() throws RemoteException {
        return myClientNickname;
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
                throw new RuntimeException(e);
            }
        }
        this.gameController = gamesManager.getGameById(gameId);
    }

    private void receiveUpdate(Update update) throws IOException{
        System.out.println("INVIO UPDATE");
        output.writeObject(update);
        output.reset();
        output.flush();
    }


    @Override
    public void receiveChatMessageUpdate(ChatMessageUpdate chatMessageUpdate) {
        try {
            receiveUpdate(chatMessageUpdate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveDeckUpdate(DeckUpdate deckUpdate) {
        try {
            receiveUpdate(deckUpdate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveStarterCardUpdate(StarterCardUpdate starterCardUpdate) {
        try {
            receiveUpdate(starterCardUpdate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receivePlacedCardUpdate(PlacedCardUpdate placedCardUpdate) {
        try {
            receiveUpdate(placedCardUpdate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveGameModelUpdate(GameModelUpdate gameModelUpdate) {
        try {
            receiveUpdate(gameModelUpdate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receivePlayerJoinedUpdate(PlayerJoinedUpdate playerJoinedUpate) throws RemoteException {
        try {
            receiveUpdate(playerJoinedUpate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveCommandResultUpdate(CommandResultUpdate commandResultUpdate) {
        try {
            receiveUpdate(commandResultUpdate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveStallUpdate(StallUpdate stallUpdate) {
        try {
            receiveUpdate(stallUpdate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveConnectionUpdate(ConnectionUpdate connectionUpdate) {
        try {
            receiveUpdate(connectionUpdate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveCardHandUpdate(CardHandUpdate cardHandUpdate) {
        try {
            receiveUpdate(cardHandUpdate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveScoreUpdate(ScoreUpdate scoreUpdate) {
        try {
            receiveUpdate(scoreUpdate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveExistingGamesUpdate(ExistingGamesUpdate existingGamesUpdate) throws RemoteException {
        try {
            output.writeObject("Display successful.");
            output.reset();
            output.flush();
            receiveUpdate(existingGamesUpdate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyJoinNotSuccessful() throws RemoteException {
        String error = "Action not successful.";
        try {
            output.writeObject(error);
            output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}