package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.game_commands.GameCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Class used to abstract the network from the client side, this class manages the communication from client to server.
 */
public class VirtualSocketServer {
    /**
     * Output channel used for communication.
     */
    private final ObjectOutputStream output;

    /**
     * Constructor of VirtualSocketServer
     * @param output output channel
     */
    public VirtualSocketServer(ObjectOutputStream output){
        this.output = output;
    }

    /**
     * Method to execute a game command.
     * @param gameCommand command to execute
     * @throws IOException I/O exception
     */
    public synchronized void setAndExecuteCommand(GameCommand gameCommand) throws IOException {
        output.writeObject(gameCommand);
        output.reset();
        output.flush();
    }

    /**
     * Method used to close the socket connection when a disconnection is detected
     * @throws IOException I/O exception
     */
    public void closeConnection() throws IOException {
        output.close();
    }
}
