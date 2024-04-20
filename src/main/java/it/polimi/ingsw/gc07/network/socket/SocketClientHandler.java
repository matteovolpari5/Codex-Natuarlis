package it.polimi.ingsw.gc07.network.socket;

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
import java.rmi.RemoteException;

public class SocketClientHandler implements VirtualView {
    final GamesManager gamesManager;
    final SocketGamesManagerServer server;
    final ObjectInputStream input;
    final ObjectOutputStream output;

    public SocketClientHandler(GamesManager gamesManager, SocketGamesManagerServer server, ObjectInputStream input, ObjectOutputStream output){
        this.gamesManager = gamesManager;
        this.server = server;
        this.input = input;
        this.output = output;
    }

    public void manageCommand(){
        GamesManagerCommand command;
        while(true){
            try{
                command = (GamesManagerCommand) input.readObject();
                gamesManager.setAndExecuteCommand(command);
                if(gamesManager.getCommandResultManager().getCommandResult().equals(CommandResult.CREATE_SERVER_GAME)){
                    //TODO in teoria dovrei creare un SocketServerGame che nel metodo runServer accetta connessioni dai client similmente a quanto viene fatto da
                    //TODO SocketGamesManagerServer, sarebbe quindi necessario creare un nuovo socket standard che accetta connessioni su una porta, non sono sicuro di come si deve procedere
                }

                //TODO l'esempio a questo punto invoca il metodo broadcastUpdate(...) di .server
                //TODO decidere come gestire a seconda dei listener
            } catch (Exception e){
                //TODO gestire eccezione
            }
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
