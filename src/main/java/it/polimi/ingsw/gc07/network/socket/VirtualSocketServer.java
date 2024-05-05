package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.NicknameCheck;
import it.polimi.ingsw.gc07.game_commands.GameCommand;
import it.polimi.ingsw.gc07.game_commands.GamesManagerCommand;
import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualServerGamesManager;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
/**
 * Class used to abstract the network from the client side, this class manages the communication from client to server
 */
public class VirtualSocketServer implements VirtualServerGamesManager, VirtualServerGame {
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
    @Override
    public void connect(VirtualView client) throws RemoteException {
        //TODO non utilizzato in socket
    }

    @Override
    public void setAndExecuteCommand(GameCommand gameCommand) throws RemoteException {
        System.out.println("VSS> SetAndExecuteGC");
        try{
            output.writeObject(gameCommand);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e){ //TODO forse cogola intede che il metodo nella signature ha throws NetworkException e a questo punto fa catch e solleva IO oppure Remote ?
            throw new RuntimeException();
        }
    }

    @Override
    public void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) throws RemoteException {
        System.out.println("VSS> SetAndExecuteGMC");
        try{
            output.writeObject(gamesManagerCommand);
            output.flush();
            output.reset();
            output.flush();
        } catch (IOException e){
            throw new RemoteException();
        }
    }

    @Override
    public NicknameCheck checkNickname(String nickname) throws RemoteException {
        return null;
        // TODO
    }

    //TODO metodo disconnect()?
}
