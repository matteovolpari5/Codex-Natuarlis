package it.polimi.ingsw.gc07.view;

import it.polimi.ingsw.gc07.model_view_listeners.*;
import it.polimi.ingsw.gc07.network.Client;

public interface Ui extends GameViewListener, ChatViewListener, DeckViewListener, GameFieldViewListener, PlayerViewListener, BoardViewListener {
    /**
     * Method used to set nickname.
     * @param nickname nickname
     */
    void setNickname(String nickname);

    /**
     * Method to set client ref.
     * @param client client
     */
    void setClient(Client client);

    /**
     * Method used to run the lobby interface.
     */
    void runJoinGameInterface();

    /**
     * Method used to run game interface.
     */
    void runGameInterface();
}
