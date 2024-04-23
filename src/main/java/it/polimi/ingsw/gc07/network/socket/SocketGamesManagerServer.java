package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.controller.GamesManagerCommand;
import it.polimi.ingsw.gc07.model.CommandResult;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.network.VirtualServerGamesManager;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GamesManager server used in socket based communication, this class manage the ServerSocket opened on the port used by clients to communicate
 * When a new connection with a client is created, a new SocketClientHandler is associated with the Socket used for the communication with the client
 */
public class SocketGamesManagerServer {
    private final ServerSocket mySocket;
    private final List<SocketClientHandler> clients;


    public SocketGamesManagerServer(ServerSocket mySocket){
        this.mySocket = mySocket;
        this.clients = new ArrayList<>();
    }

    public void runServer() throws IOException {
        //TODO slide 21 utilizza executor per gestire i thread, in questo caso
        //TODO socketClientHandler implements Runnable e run() diventerebbe l'attuale manageCommand()
        Socket clientSocket = null;
        while((clientSocket = this.mySocket.accept()) != null){
            System.out.println("Received client connection");
            SocketClientHandler handler = new SocketClientHandler(GamesManager.getGamesManager(), clientSocket);
            synchronized (this.clients){
                clients.add(handler);
            }
        }
    }
}