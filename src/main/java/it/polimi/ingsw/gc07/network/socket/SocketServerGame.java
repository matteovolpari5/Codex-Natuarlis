package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GameCommand;
import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.net.ServerSocket;
import java.rmi.RemoteException;

public class SocketServerGame{
  private final ServerSocket mySocket;
  public SocketServerGame(ServerSocket mySocket){
    this.mySocket = mySocket;
  }

  public void runServer(){

  }
}
