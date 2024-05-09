package it.polimi.ingsw.gc07.view;

import it.polimi.ingsw.gc07.model_view_listeners.*;
import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualServerGamesManager;
import it.polimi.ingsw.gc07.network.VirtualView;

public interface Ui extends ChatViewListener, DeckViewListener, GameFieldViewListener, PlayerViewListener, BoardViewListener {
    void runCliJoinGame();
    void runCliGame();
}
