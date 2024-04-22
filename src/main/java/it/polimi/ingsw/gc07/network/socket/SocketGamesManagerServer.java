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
    private final Map<Integer, Integer> gameServers;


    public SocketGamesManagerServer(ServerSocket mySocket){
        this.mySocket = mySocket;
        this.clients = new ArrayList<>();
        this.gameServers = new HashMap<>();
    }

    public void runServer() throws IOException {
        //TODO slide 21 utilizza executor per gestire i thread, in questo caso
        //TODO socketClientHandler implements Runnable e run() diventerebbe l'attuale manageCommand()
        Socket clientSocket = null;
        while((clientSocket = this.mySocket.accept()) != null){
            System.out.println("Received client connection");
            SocketClientHandler handler = new SocketClientHandler(GamesManager.getGamesManager(), this, clientSocket);
            synchronized (this.clients){
                clients.add(handler);
            }
            new Thread(() -> {
                //try{
                    handler.manageGamesManagerCommand();
                //}catch (IOException e){
                    //TODO decidere come gestire eccezioni
                    //throw new RuntimeException(e);
                //}
            }).start();
        }
    }

    public Integer manageConnectionToGame(CommandResult result, Integer gameId){
        Integer res = null;
        if(result.equals(CommandResult.CREATE_SERVER_GAME)){
            //TODO definire porta, 0 significa scelta automaticamente
            ServerSocket sc = null;
            try{
                sc = new ServerSocket(0);
            } catch (IOException e){
                System.out.println("Unable to start a new game server: unavailable port");
                throw new RuntimeException();
            }
            System.out.println("Game server ready");
            res = sc.getLocalPort();
            synchronized (gameServers){
                gameServers.put(gameId, res);
            }
            new SocketServerGame(sc).runServer();
        }else{
            //the result can only be "SET_SERVER_GAME"
            res = gameServers.get(gameId);
            if(res == null){
                throw new RuntimeException();
            }
        }
        return res;
    }
}