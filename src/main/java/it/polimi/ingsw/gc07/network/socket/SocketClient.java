package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.updates.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

public class SocketClient implements VirtualView {
    private final ObjectInputStream input;
    private VirtualSocketServerGamesManager gamesManagerServer;
    private VirtualServerGame gameServer; //TODO dovrebbe essere VirtualSocketServerGame e non VirtualServerGame, tuttavia nell'interfaccia utilizza il secondo,
                                              //TODO dato che VirtualView è usata anche in RMI crea errori, per non causare problemi in RMI qui viene posta a VirtualServerGame

    public SocketClient(ObjectInputStream input, ObjectOutputStream output){
        this.input = input;
        this.gamesManagerServer = new VirtualSocketServerGamesManager(output);
        this.gameServer = null;
    }

    public void run(){  //TODO dovrebbe essere private
    }
    @Override
    public void setServerGame(VirtualServerGame gameServer) throws RemoteException {
        this.gameServer = gameServer;
        this.gamesManagerServer = null; //TODO in questo modo quando entra in una partita non può più comunicare con gamesManager
    }
    @Override
    public String getNickname() throws RemoteException {
        return null;
    }


















    // TODO aggiunti perchè altrimenti non compila

    @Override
    public void receiveChatMessageUpdate(ChatMessageUpdate chatMessageUpdate) {

    }

    @Override
    public void receiveCommonObjectiveUpdate(CommonObjectiveUpdate commonObjectiveUpdate) {

    }

    @Override
    public void receiveDeckUpdate(DeckUpdate deckUpdate) {

    }

    @Override
    public void receiveStarterCardUpdate(StarterCardUpdate starterCardUpdate) {

    }

    @Override
    public void receivePlacedCardUpdate(PlacedCardUpdate placedCardUpdate) {

    }

    @Override
    public void receiveGameModelUpdate(GameModelUpdate gameModelUpdate) {

    }

    @Override
    public void receivePlayerJoinedUpdate(PlayerJoinedUpdate playerJoinedUpdate) {

    }

    @Override
    public void receiveCommandResultUpdate(CommandResultUpdate commandResultUpdate) {

    }

    @Override
    public void receiveStallUpdate(StallUpdate stallUpdate) {

    }

    @Override
    public void receiveConnectionUpdate(ConnectionUpdate connectionUpdate) {

    }

    @Override
    public void receiveCardHandUpdate(CardHandUpdate cardHandUpdate) {

    }

    @Override
    public void receiveScoreUpdate(ScoreUpdate scoreUpdate) {

    }
}
