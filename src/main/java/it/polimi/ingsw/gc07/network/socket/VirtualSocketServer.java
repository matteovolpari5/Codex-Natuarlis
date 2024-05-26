package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.game_commands.GameCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Class used to abstract the network from the client side, this class manages the communication from client to server
 */
public class VirtualSocketServer {
    private final ObjectOutputStream output;

    public VirtualSocketServer(ObjectOutputStream output){
        this.output = output;
    }

    public synchronized void setAndExecuteCommand(GameCommand gameCommand) throws IOException {
        output.writeObject(gameCommand);
        output.reset();
        output.flush();
    }

    public void closeConnection() throws IOException {
        output.close();
    }
}
