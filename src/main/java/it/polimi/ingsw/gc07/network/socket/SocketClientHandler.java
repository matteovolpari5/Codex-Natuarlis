package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.game_commands.GameCommand;
import it.polimi.ingsw.gc07.game_commands.GamesManagerCommand;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.network.VirtualServerGame;
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

    public SocketClientHandler(GamesManager gamesManager, Socket mySocket) throws IOException {
        System.out.println("SCH-costruttore>>>>>>>>>");
        InputStream temp_input;
        OutputStream temp_output;

        this.gamesManager = gamesManager;
        System.out.println("SCH> gamesManager ok");

        this.gameController = null;
        System.out.println("SCH> GameController ok");

        this.mySocket = mySocket;
        System.out.println("SCH> mySocket ok");

        temp_output = this.mySocket.getOutputStream();
        System.out.println("SCH> temp_output ok");

        this.output = new ObjectOutputStream(temp_output);
        System.out.println("SCH> ObjectOutputStream output ok");
        output.flush();

        temp_input = this.mySocket.getInputStream();
        System.out.println("SCH> temp_input ok");

        this.input = new ObjectInputStream(temp_input);
        System.out.println("SCH> ObjectInputStream input ok");

        new Thread(this::manageGamesManagerCommand).start();
    }

    private void manageGamesManagerCommand(){
        System.out.println("SCH-manageGamesManagerCommand>>>>>>>>>");
        GamesManagerCommand command;

        while(true) {
            System.out.println("SCH> ascolto");
            try {
                command = (GamesManagerCommand) input.readObject();
                System.out.println("SCH> ho letto un command ricevuto");
                synchronized (gamesManager){
                    gamesManager.setAndExecuteCommand(command);
                    System.out.println("SCH> l'ho eseguito");
                    CommandResult result = gamesManager.getCommandResult();
                    System.out.println("SCH> leggo il command result");
                    //TODO i listener sono stati invocati dal model in seguito alla modifica causata dal command
                    //TODO ad un certo punto nell'esecuzione del command si arriva all'invocazione dei metodi sottostanti per notificare
                    //TODO quindi non bisogna notificare l'utente adesso
                    if(result.equals(CommandResult.SUCCESS)){
                        System.out.println("SCH> successo");
                    }
                    System.out.println(result);
                    if(result.equals(CommandResult.CREATE_SERVER_GAME) || result.equals(CommandResult.SET_SERVER_GAME)){
                        String commandNickname = command.getNickname();
                        int gameId = gamesManager.getGameIdWithPlayer(commandNickname);
                        if(gameId < 0){
                            throw new RuntimeException();
                        }
                        this.gameController = gamesManager.getGameById(gameId);
                        gameController.addListener(this);
                        System.out.println("SCH> eseguito ultimo command");
                        //TODO output.write->.reset()->.flush()
                        //output.writeBytes("Benvenuto"); //TODO oppure .writeChars(...);
                        //output.flush();
                        // TODO necessario anche se non è un oggetto?
                        break;
                    }else if(result.equals(CommandResult.DISPLAY_GAMES)) {
                        // create update
                        ExistingGamesUpdate update = new ExistingGamesUpdate(GamesManager.getGamesManager().getFreeGamesDetails());
                        // send update
                        receiveUpdate(update);
                    }else{
                        //TODO come gestire se il command ha avuto esito negativo? In rmi non viene controllato da nessuno l'esito del command
                    }
                }
                //TODO l'esempio a questo punto invoca il metodo broadcastUpdate(...) di .server
                //TODO decidere come gestire a seconda dei listener
                //TODO AGGIRONAMENTO: nell'esempio la propagazione dell'aggiornamento dello stato del modello parte dalla classe di rete, nel nostro caso
                //TODO deve partire dal modello, quindi in questa classe non bisogna far partire alcun update
            } catch (Exception e){
                //TODO gestire eccezione
                break;
            }
        }
        System.out.println("SCH> passo a manageGameCommand()");
        manageGameCommand();
    }

    private void manageGameCommand(){
        System.out.println("SCH-manageGameCommand()>>>>>>>>>");
        GameCommand command;
        while(true){
            System.out.println("SCH> ascolto");
            try{
                command = (GameCommand) input.readObject();
                System.out.println("SCH> ho letto un command ricevuto");
                synchronized (gameController){
                    gameController.setAndExecuteCommand(command);
                    System.out.println("SCH> l'ho eseguito");
                    CommandResult result = gameController.getCommandResult();
                    System.out.println("SCH> leggo il command result");
                    //TODO come prima: in rmi nessuno controlla l'esito del command
                    if(result.equals(CommandResult.SUCCESS)){
                        //TODO mostrare esito
                    }else{
                        //TODO mostrare errore
                    }
                }
            } catch (Exception e){
                //TODO gestire eccezione
                break;
            }
        }
        closeConnection(mySocket, input, output);
    }

    public void closeConnection(Socket mySocket, ObjectInputStream input, ObjectOutputStream output){
        try{
            input.close();
            output.close();
            mySocket.close();
        }catch (IOException e){
            System.out.println("SCH> Error while closing connection");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    @Override
    public void setServerGame(VirtualServerGame serverGame) throws RemoteException {
        //TODO non utilizzato in socket, potrebbe
    }
    @Override
    public String getNickname() throws RemoteException {
        //TODO in rmi è utilizzato per assegnare al client lo stub del server una volta che entra in una partita
        //TODO in socket non necessario
        return null;
    }

    // TODO aggiunti perchè altrimenti non compila

    private void receiveUpdate(Update update) throws IOException{
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
            receiveUpdate(existingGamesUpdate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}