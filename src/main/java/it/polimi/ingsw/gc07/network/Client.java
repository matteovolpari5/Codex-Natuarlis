package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.game_commands.GameControllerCommand;
import it.polimi.ingsw.gc07.game_commands.GamesManagerCommand;

import java.rmi.RemoteException;

public interface Client {
    void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand);
    void setAndExecuteCommand(GameControllerCommand gameControllerCommand);
    void setClientAlive(boolean isAlive);
    boolean isClientAlive();
}
