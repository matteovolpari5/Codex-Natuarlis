package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.utils.SafePrinter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * GamesManager server used in socket based communication, this class manage the ServerSocket opened on the port used by clients to communicate
 * When a new connection with a client is created, a new SocketClientHandler is associated with the Socket used for the communication with the client
 */
public class SocketServer {
    private static SocketServer mySocketServer;
    private  ServerSocket mySocket;

    private SocketServer(){}

    public static synchronized SocketServer getSocketServer(){
        if(mySocketServer == null){
            mySocketServer = new SocketServer();
        }
        return mySocketServer;
    }

    public void initializeSocketServer(ServerSocket mySocket) {
        this.mySocket = mySocket;
    }

    public void runServer() throws IOException {
        SafePrinter.println("Socket server running");
        Socket clientSocket;
        while((clientSocket = this.mySocket.accept()) != null){
            SafePrinter.println("Received client socket connection");
            new SocketClientHandler(clientSocket); //TODO se errore nella creazione viene fatto system exit su server main
        }
    }
}

