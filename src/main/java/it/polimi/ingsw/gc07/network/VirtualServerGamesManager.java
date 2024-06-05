package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.controller.NicknameCheck;
import it.polimi.ingsw.gc07.game_commands.GamesManagerCommand;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Virtual server of the lobby server.
 */
public interface VirtualServerGamesManager extends Remote {
    /**
     * Method to connect a new client.
     * @param client client to connect
     * @throws RemoteException remote exception
     */
    void connect(String nickname, VirtualView client) throws RemoteException;

    /**
     * Method used to check a nickname.
     * @param nickname nickname
     * @return result of the nickname check
     * @throws RemoteException remote exception
     */
    NicknameCheck checkNickname(String nickname) throws RemoteException;

    /**
     * Method used to get the game server of a game with given id.
     * @param gameId game id
     * @return server reference
     * @throws RemoteException remote exception
     */
    VirtualServerGame getGameServer(int gameId) throws RemoteException;

    /**
     * Method to set a command and execute games manager command.
     * @param gamesManagerCommand games manager command to set and execute
     * @throws RemoteException remote exception
     */
    void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) throws RemoteException;
}
