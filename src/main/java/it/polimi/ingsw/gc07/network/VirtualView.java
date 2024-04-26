package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.listeners.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote, ChatListener, DeckListener, GameFieldListener, GameListener, PlayerListener, ScoreTrackBoardListener {
    void setServerGame(VirtualServerGame serverGame) throws RemoteException;
    String getNickname() throws RemoteException;
}
