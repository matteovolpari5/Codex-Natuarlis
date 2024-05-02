package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.listeners.*;
import it.polimi.ingsw.gc07.updates.ExistingGamesUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote, ChatListener, GameFieldListener, GameListener, PlayerListener, ScoreTrackBoardListener {
    void setServerGame(VirtualServerGame serverGame) throws RemoteException;
    String getNickname() throws RemoteException;
    void receiveExistingGamesUpdate(ExistingGamesUpdate existingGamesUpdate) throws RemoteException;
    void notifyJoinNotSuccessful() throws RemoteException;
}
