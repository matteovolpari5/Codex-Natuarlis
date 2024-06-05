package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.utils.SafePrinter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class representing the GamesManager server used in socket based communication, this class manages the ServerSocket.
 * When a new connection with a client is created, a new SocketClientHandler is associated with the Socket used for the communication with the client.
 */
public class SocketServer {
    /**
     * Instance of SocketServer.
     */
    private static SocketServer mySocketServer;
    /**
     * Socket used to accept new connections.
     */
    private  ServerSocket mySocket;

    /**
     * Constructor of SocketServer
     */
    private SocketServer(){}

    /**
     * Method used to get the only existing instance of SocketServer (Singleton)
     * @return SocketServer instance
     */
    public static synchronized SocketServer getSocketServer(){
        if(mySocketServer == null){
            mySocketServer = new SocketServer();
        }
        return mySocketServer;
    }

    /**
     * Method used to initialize the SocketServer
     * @param mySocket server socket
     */
    public void initializeSocketServer(ServerSocket mySocket) {
        this.mySocket = mySocket;
    }

    /**
     * Method used to accept new connections
     */
    public void runServer() {
        SafePrinter.println("Socket server running");
        Socket clientSocket;
        while(true){
            try {
                clientSocket = this.mySocket.accept();
                SafePrinter.println("Received client socket connection");
                new SocketClientHandler(clientSocket);
            } catch (IOException e) {
                SafePrinter.println("An error occured while creating the connection with a client");
            }
        }
    }
}

