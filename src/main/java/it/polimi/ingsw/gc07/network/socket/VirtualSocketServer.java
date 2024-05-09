package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.game_commands.GameCommand;
import it.polimi.ingsw.gc07.game_commands.SendPingControllerCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
/**
 * Class used to abstract the network from the client side, this class manages the communication from client to server
 */
public class VirtualSocketServer {
    private final ObjectOutputStream output;

    public VirtualSocketServer(ObjectOutputStream output, String nickname, String status, boolean interfaceType){
        System.out.println("VSS> Costruttore");
        this.output = output;
        try {
            output.writeObject(nickname);
            output.reset();
            output.flush();
            output.writeObject(status);
            output.reset();
            output.flush();
            if(status.equals("reconnected")){
                output.writeBoolean(interfaceType);
                output.reset();
                output.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void setAndExecuteCommand(GameCommand gameCommand) throws RemoteException {
        try{
            output.writeObject(gameCommand);
            output.reset();
            output.flush();
        } catch (IOException e){ //TODO forse cogola intede che il metodo nella signature ha throws NetworkException e a questo punto fa catch e solleva IO oppure Remote ?
            throw new RuntimeException();
        }
    }

    //TODO metodo disconnect()?
}
