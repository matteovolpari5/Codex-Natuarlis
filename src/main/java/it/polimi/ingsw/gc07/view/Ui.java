package it.polimi.ingsw.gc07.view;

import it.polimi.ingsw.gc07.model_view_listeners.*;
import it.polimi.ingsw.gc07.network.Client;

public interface Ui extends GameViewListener, ChatViewListener, DeckViewListener, GameFieldViewListener, PlayerViewListener, BoardViewListener {
    void setNickname(String nickname);
    void setClient(Client client);
    void startInterface();
    void runJoinGameInterface();
    void runGameInterface();
}
