package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * GamesManager server used in socket based communication, this class manage the ServerSocket opened on the port used by clients to communicate
 * When a new connection with a client is created, a new SocketClientHandler is associated with the Socket used for the communication with the client
 */
public class SocketServer {
    private static SocketServer mySocketServer;
    private  ServerSocket mySocket;
    private final List<VirtualView> clients;


    private SocketServer(){
        this.clients = new ArrayList<>();
    }

    public static synchronized SocketServer getSocketServer(){
        if(mySocketServer == null){
            mySocketServer = new SocketServer();
        }
        return mySocketServer;
    }

    public void initializeSocketServer(ServerSocket mySocket){
        this.mySocket = mySocket;
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

    public VirtualView getVirtualView(String nickname) throws RemoteException {
        for(VirtualView client : clients) {
            if(client.getNickname().equals(nickname)) {
                return client;
            }
        }
        return null;
    }

    public void removeVirtualView(String nickname)throws RemoteException {
        VirtualView virtualView = getVirtualView(nickname);
        clients.remove(virtualView);
    }
}