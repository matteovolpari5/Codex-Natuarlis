package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.Game;
import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.controller.GamesManagerCommand;
import it.polimi.ingsw.gc07.model.CommandResult;
import it.polimi.ingsw.gc07.model_view.ModelView;
import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketClientHandler implements VirtualView {
    private final GamesManager gamesManager;
    private final Game game;
    private final SocketGamesManagerServer GamesManagerserver;
    private final Socket mySocket;

    public SocketClientHandler(GamesManager gamesManager, SocketGamesManagerServer server, Socket mySocket){
        this.gamesManager = gamesManager;
        this.GamesManagerserver = server;
        this.mySocket = mySocket;
        this.game = null;
    }

    public void manageGamesManagerCommand(){
        GamesManagerCommand command;
        ObjectInputStream input = null;
        ObjectOutputStream output = null;
        while(true){
            try{
                input = new ObjectInputStream(mySocket.getInputStream());
                output = new ObjectOutputStream(mySocket.getOutputStream());
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
                        this.game = gamesManager.getGameById(gameId);
                        int port = GamesManagerserver.manageConnectionToGame(result, gameId);
                        Socket socketClient = new Socket("host", port);
                        //TODO decidere come gestire la sostituzione dell'input e dell'output, per ora sostituiti direttamente da ora in avanti
                        input = new ObjectInputStream(socketClient.getInputStream());
                        output = new ObjectOutputStream(socketClient.getOutputStream());
                        output.writeBytes("Benvenuto"); //TODO oppure .writeChars(...);
                        output.flush();
                        output.reset(); //TODO necessario anche se non è un oggetto?
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
        //oppure alla riga 55 non si crea un'altro socket ma si riusa mySocket, in tal caso bisgona capire come gestire la chiusura del socket, input e output
    }

    private void manageGameCommand(){
        //TODO completare
        closeConnection(mySocket, input, output); //forse realizzare come alla riga 73 così mySocket è il socket con Game, comunque sia deve essere cambiato mySocket
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
}

//TODO riassunto:
//ServerMain crea socket lato server sulla porta specificata, successivamente crea SocketGamesManagerServer passandogli anche il socket e lo esegue.
//SocketGamesManagerServer quando si collega un client crea un SocketClientHandler a cui si assegnano input e output stream del socket della connesione con il client e avvia in un altro thread il SocketClientHandler.
//SocketClientHandler in manageCommand() gestisce i messaggi inviati dal client.

//ClientMain collega il client al socket lato server sulla porta specificata, la connessione viene accettata da .accept() in SocketGamesManagerServer.
//Una volta stabilita la connessione ClientMain crea SocketClient passando input e output stream e lo esegue.
//Quando SocketClient viene creato crea VirtualSocketServerGamesManager che si occupa di gestire l'output, quindi la comunicazione in uscita verso gamesManager
