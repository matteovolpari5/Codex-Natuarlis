package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.controller.Game;
import it.polimi.ingsw.gc07.controller.GameCommand;
import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RmiServerGame extends UnicastRemoteObject implements VirtualServerGame {
    /**
     * Game controller of the game.
     */
    private final Game game;
    /**
     * Virtual views of clients in the game.
     */
    private final List<VirtualView> clients;

    public RmiServerGame(Game game) throws RemoteException {
        this.game = game;
        this.clients = new ArrayList<>();
    }

    /**
     * Method that allows a client to connect with the RMI server.
     * @param client client to connect
     * @throws RemoteException remote exception
     */
    @Override
    public synchronized void connect(VirtualView client) throws RemoteException {
        clients.add(client);
        game.addListener(client);
        System.err.println("New client connected");
    }

    /**
     * Method that allows the client to execute a game command.
     * @param gameCommand game command to set and execute
     * @throws RemoteException remote exception
     */
    @Override
    public synchronized void setAndExecuteCommand(GameCommand gameCommand) throws RemoteException {
        game.setAndExecuteCommand(gameCommand);
        System.out.println(game.getCommandResultManager().getCommandResult());
        if(game.getCommandResultManager().getCommandResult().equals(CommandResult.SUCCESS)) {
            // TODO stampa aggiornamento al client
        }else {
            System.out.println(game.getCommandResultManager().getCommandResult().getResultMessage());
        }
    }
}
