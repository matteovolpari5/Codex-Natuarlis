package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.game_commands.GameCommand;
import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class RmiServerGame extends UnicastRemoteObject implements VirtualServerGame {
    /**
     * Game controller of the server.
     */
    private final GameController gameController;
    /**
     * Queue containing commands to execute.
     */
    private final BlockingDeque<GameCommand> commandsQueue;

    public RmiServerGame(GameController gameController) throws RemoteException {
        this.gameController = gameController;
        this.commandsQueue = new LinkedBlockingDeque<>();
        startCommandExecutor();
    }

    private void startCommandExecutor() {
        new Thread(() -> {
            while(true) {
                try {
                    GameCommand command = commandsQueue.take();
                    gameController.setAndExecuteCommand(command);

                    // only for test purpose
                    System.out.println(gameController.getCommandResult());
                }catch(InterruptedException e) {
                    System.err.println("Channel closed");
                    break;
                }
            }
        }).start();
    }

    /**
     * Method that allows a client to connect with the RMI server.
     * @param client client to connect
     * @throws RemoteException remote exception
     */
    @Override
    public synchronized void connect(VirtualView client) throws RemoteException {
        gameController.addListener(client);
        System.err.println("New client connected");
    }

    /**
     * Method that allows the client to execute a game command.
     * @param gameCommand game command to set and execute
     * @throws RemoteException remote exception
     */
    @Override
    public synchronized void setAndExecuteCommand(GameCommand gameCommand) throws RemoteException {
        // TODO serve sincronizzato? non penso
        try {
            // blocking queues are thread safe
            commandsQueue.put(gameCommand);
        }catch(InterruptedException e) {
            // TODO
            e.printStackTrace();
            throw new RemoteException();
        }
    }
}
