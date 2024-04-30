package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GamesManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * GamesManager server used in socket based communication, this class manage the ServerSocket opened on the port used by clients to communicate
 * When a new connection with a client is created, a new SocketClientHandler is associated with the Socket used for the communication with the client
 */
public class SocketServer {
    private final ServerSocket mySocket;
    private final List<SocketClientHandler> clients;


    public SocketServer(ServerSocket mySocket){
        this.mySocket = mySocket;
        this.clients = new ArrayList<>();
    }

    public void runServer() throws IOException {
        System.out.println("SocketServer> Socket server running");
        //TODO slide 21 utilizza executor per gestire i thread, in questo caso
        //TODO socketClientHandler implements Runnable e run() diventerebbe l'attuale manageCommand()
        Socket clientSocket = null;
        while((clientSocket = this.mySocket.accept()) != null){
            System.out.println("SocketServer> Received client connection");
            SocketClientHandler handler = new SocketClientHandler(GamesManager.getGamesManager(), clientSocket);
            synchronized (this.clients){
                clients.add(handler);
            }
        }
    }
}