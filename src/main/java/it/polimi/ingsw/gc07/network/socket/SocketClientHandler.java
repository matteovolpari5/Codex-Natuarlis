package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.controller.GamesManager;
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

    public void manageGamesManagerCommand(){

        GamesManagerCommand command;

        while(true){
            try{
                command = (GamesManagerCommand) input.readObject();
                synchronized (gamesManager){
                    gamesManager.setAndExecuteCommand(command);
                    CommandResult result = gamesManager.getCommandResultManager().getCommandResult();
                    //TODO per mostrare l'esito al client basta: output.writeObject(result).flush();  output.reset(); output.flush(); ?
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
                        output.writeBytes("Benvenuto"); //TODO oppure .writeChars(...);

                        output.flush();
                         //TODO necessario anche se non è un oggetto?
                        break;
                    }
                }
                //TODO l'esempio a questo punto invoca il metodo broadcastUpdate(...) di .server
                //TODO decidere come gestire a seconda dei listener
            } catch (Exception e){
                //TODO gestire eccezione
                break;
            }
        }
        manageGameCommand();
    }

    private void manageGameCommand(){
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

    }
    @Override
    public String getNickname() throws RemoteException {
        return null;
    }



















    // TODO aggiunti perchè altrimenti non compila



    @Override
    public void receiveChatMessageUpdate(ChatMessageUpdate chatMessageUpdate) {

    }

    @Override
    public void receiveCommonObjectiveUpdate(CommonObjectiveUpdate commonObjectiveUpdate) {

    }

    @Override
    public void receiveDeckUpdate(DeckUpdate deckUpdate) {

    }

    @Override
    public void receiveStarterCardUpdate(StarterCardUpdate starterCardUpdate) {

    }

    @Override
    public void receivePlacedCardUpdate(PlacedCardUpdate placedCardUpdate) {

    }

    @Override
    public void receiveGameModelUpdate(GameModelUpdate gameModelUpdate) {

    }

    @Override
    public void receivePlayerJoinedUpdate(PlayerJoinedUpdate playerJoinedUpdate) {

    }

    @Override
    public void receiveCommandResultUpdate(CommandResultUpdate commandResultUpdate) {

    }

    @Override
    public void receiveStallUpdate(StallUpdate stallUpdate) {

    }

    @Override
    public void receiveConnectionUpdate(ConnectionUpdate connectionUpdate) {

    }

    @Override
    public void receiveCardHandUpdate(CardHandUpdate cardHandUpdate) {

    }

    @Override
    public void receiveScoreUpdate(ScoreUpdate scoreUpdate) {

    }
}

//TODO riassunto:
//ServerMain crea socket lato server sulla porta specificata, successivamente crea SocketGamesManagerServer passandogli anche il socket e lo esegue.
//SocketGamesManagerServer quando si collega un client crea un SocketClientHandler a cui si assegnano input e output stream del socket della connesione con il client e avvia in un altro thread il SocketClientHandler.
//SocketClientHandler in manageCommand() gestisce i messaggi inviati dal client.

//ClientMain collega il client al socket lato server sulla porta specificata, la connessione viene accettata da .accept() in SocketGamesManagerServer.
//Una volta stabilita la connessione ClientMain crea SocketClient passando input e output stream e lo esegue.
//Quando SocketClient viene creato crea VirtualSocketServerGamesManager che si occupa di gestire l'output, quindi la comunicazione in uscita verso gamesManager
