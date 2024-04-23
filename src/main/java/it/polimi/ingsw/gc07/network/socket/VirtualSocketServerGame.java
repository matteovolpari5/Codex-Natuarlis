package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.game_commands.GameCommand;
import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.rmi.RemoteException;

public class VirtualSocketServerGame implements VirtualServerGame {
    @Override
    public void connect(VirtualView client) throws RemoteException {

    }

    @Override
    public void setAndExecuteCommand(GameCommand gameCommand) throws RemoteException {

    }
}
