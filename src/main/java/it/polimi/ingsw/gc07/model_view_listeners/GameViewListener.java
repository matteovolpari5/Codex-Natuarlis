package it.polimi.ingsw.gc07.model_view_listeners;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;

public interface GameViewListener {
    void receiveGeneralModelUpdate(GameState gameState, String currPlayerNickname);
    void receivePenultimateRoundUpdate();
    void receiveAdditionalRoundUpdate();
    void receiveCommandResultUpdate(CommandResult commandResult);
}
