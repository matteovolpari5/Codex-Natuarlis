package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.controller.GamesManagerCommand;
import it.polimi.ingsw.gc07.network.VirtualServerGamesManager;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SocketGamesManagerServer implements VirtualServerGamesManager {
    final ServerSocket mySocket;
    final GamesManager gamesManager;
    final List<SocketClientHandler> clients = new ArrayList<>();


    public SocketGamesManagerServer(ServerSocket mySocket, GamesManager gamesManager){
        this.mySocket = mySocket;
        this.gamesManager = gamesManager;
    }

    public void runServer() throws IOException {
        //TODO slide 21 utilizza executor per gestire i thread, in questo caso
        //TODO socketClientHandler implements Runnable e run() diventerebbe l'attuale manageCommand()
        Socket clientSocket = null;
        while((clientSocket = this.mySocket.accept()) != null){
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
            SocketClientHandler handler = new SocketClientHandler(this.gamesManager, this, input, output);
            synchronized (this.clients){
                clients.add(handler);
            }
            new Thread(() -> {
                //try{
                    handler.manageCommand();
                //}catch (IOException e){
                    //TODO decidere come gestire eccezioni
                    //throw new RuntimeException(e);
                //}
            }).start();
        }
    }
    @Override
    public void connect(VirtualView client) throws RemoteException {

    }

    @Override
    public void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) throws RemoteException {

    }
}
