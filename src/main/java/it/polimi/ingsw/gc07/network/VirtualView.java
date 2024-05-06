package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.model_listeners.*;
import it.polimi.ingsw.gc07.updates.ExistingGamesUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote, ChatListener, GameFieldListener, GameListener, PlayerListener, ScoreTrackBoardListener {
    String getNickname() throws RemoteException;
    void setServerGame(VirtualServerGame serverGame) throws RemoteException;
    void receiveExistingGamesUpdate(ExistingGamesUpdate existingGamesUpdate) throws RemoteException;
    void notifyJoinNotSuccessful() throws RemoteException;
    void setGameController(int gameId) throws RemoteException;
}
