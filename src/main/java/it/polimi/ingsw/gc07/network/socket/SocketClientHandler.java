package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.game_commands.GameCommand;
import it.polimi.ingsw.gc07.game_commands.GamesManagerCommand;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.updates.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        this.gamesManager = gamesManager;
        this.gameController = null;
        this.mySocket = mySocket;
        this.input = new ObjectInputStream(mySocket.getInputStream());
        this.output = new ObjectOutputStream(mySocket.getOutputStream());
        new Thread(this::manageGamesManagerCommand).start();
    }

    private void manageGamesManagerCommand(){

        GamesManagerCommand command;

        while(true) {
            try {
                command = (GamesManagerCommand) input.readObject();
                synchronized (gamesManager){
                    gamesManager.setAndExecuteCommand(command);
                    CommandResult result = gamesManager.getCommandResult();
                    //TODO i listener sono stati invocati dal model in seguito alla modifica causata dal command
                    //TODO ad un certo punto nell'esecuzione del command si arriva all'invocazione dei metodi sottostanti per notificare
                    //TODO quindi non bisogna notificare l'utente adesso
                    if(result.equals(CommandResult.SUCCESS)){
                        break;
                    }
                    if(result.equals(CommandResult.CREATE_SERVER_GAME) || result.equals(CommandResult.SET_SERVER_GAME)){
                        String commandNickname = command.getNickname();
                        int gameId = gamesManager.getGameIdWithPlayer(commandNickname);
                        if(gameId < 0){
                            throw new RuntimeException();
                        }
                        this.gameController = gamesManager.getGameById(gameId);

                        //TODO output.write->.reset()->.flush()
                        //output.writeBytes("Benvenuto"); //TODO oppure .writeChars(...);
                        //output.flush();
                        // TODO necessario anche se non è un oggetto?
                        break;
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
        gameController.addListener(this);
        manageGameCommand();
    }

    private void manageGameCommand(){
        GameCommand command;
        while(true){
            try{
                command = (GameCommand) input.readObject();
                synchronized (gameController){
                    gameController.setAndExecuteCommand(command);
                    CommandResult result = gameController.getCommandResult();
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
            System.out.println("Error while closing connection");
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


    @Override
    public void receiveChatMessageUpdate(ChatMessageUpdate chatMessageUpdate) {
        //TODO il messaggio è creato dal model, dato che la comunicazione con il client è gestita da questa classe, il model invoca il metodo di questa classe
        try {
            output.writeObject(chatMessageUpdate);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveDeckUpdate(DeckUpdate deckUpdate) {
        try {
            output.writeObject(deckUpdate);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveStarterCardUpdate(StarterCardUpdate starterCardUpdate) {
        try {
            output.writeObject(starterCardUpdate);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receivePlacedCardUpdate(PlacedCardUpdate placedCardUpdate) {
        try {
            output.writeObject(placedCardUpdate);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveGameModelUpdate(GameModelUpdate gameModelUpdate) {
        try {
            output.writeObject(gameModelUpdate);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receivePlayerJoinedUpdate(PlayerJoinedUpdate playerJoinedUpate) throws RemoteException {
        try {
            output.writeObject(playerJoinedUpate);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveCommandResultUpdate(CommandResultUpdate commandResultUpdate) {
        try {
            output.writeObject(commandResultUpdate);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveStallUpdate(StallUpdate stallUpdate) {
        try {
            output.writeObject(stallUpdate);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveConnectionUpdate(ConnectionUpdate connectionUpdate) {
        try {
            output.writeObject(connectionUpdate);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveCardHandUpdate(CardHandUpdate cardHandUpdate) {
        try {
            output.writeObject(cardHandUpdate);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveScoreUpdate(ScoreUpdate scoreUpdate) {
        try {
            output.writeObject(scoreUpdate);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveExistingGamesUpdate(ExistingGamesUpdate existingGamesUpdate) throws RemoteException {
        // TODO
    }

    @Override
    public void receiveDecksUpdate(DeckUpdate deckUpdate) throws RemoteException {
        // TODO
    }
}

//TODO riassunto:
//ServerMain crea socket lato server sulla porta specificata, successivamente crea SocketServer passandogli anche il socket e lo esegue.
//SocketServer quando si collega un client crea un SocketClientHandler a cui si assegnano input e output stream del socket della connesione con il client e avvia in un altro thread il SocketClientHandler.
//SocketClientHandler in manageCommand() gestisce i messaggi inviati dal client.

//ClientMain collega il client al socket lato server sulla porta specificata, la connessione viene accettata da .accept() in SocketServer.
//Una volta stabilita la connessione ClientMain crea SocketClient passando input e output stream e lo esegue.
//Quando SocketClient viene creato crea VirtualSocketServer che si occupa di gestire l'output, quindi la comunicazione in uscita verso gamesManager
