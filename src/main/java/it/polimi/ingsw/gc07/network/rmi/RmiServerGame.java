package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.game_commands.GameControllerCommand;
import it.polimi.ingsw.gc07.network.VirtualServerGame;

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
    private final BlockingDeque<GameControllerCommand> commandsQueue;

    /**
     * Constructor of RmiServerGame.
     * @param gameController game controller of the game
     * @throws RemoteException remote exception
     */
    public RmiServerGame(GameController gameController) throws RemoteException {
        this.gameController = gameController;
        this.commandsQueue = new LinkedBlockingDeque<>();
        startCommandExecutor();
    }

    /**
     * Method that starts the command executor, a thread that takes tasks
     * from the blocking queue and executes them.
     * Used to make RMI asynchronous.
     */
    private void startCommandExecutor() {
        new Thread(() -> {
            while(true) {
                GameControllerCommand command = null;
                try {
                    command = commandsQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
                gameController.setAndExecuteCommand(command);
            }
        }).start();
    }

    /**
     * Method that allows the client to execute a game command.
     * @param gameControllerCommand game command to set and execute
     * @throws RemoteException remote exception
     */
    @Override
    public void setAndExecuteCommand(GameControllerCommand gameControllerCommand) throws RemoteException {
        try {
            // blocking queues are thread safe
            commandsQueue.put(gameControllerCommand);
        }catch(InterruptedException e) {
            throw new RemoteException();
        }
    }
}
