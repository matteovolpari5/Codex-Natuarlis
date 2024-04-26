package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;

import java.util.List;

public interface GameListener {
    void notifyId(int id);

    void notifyPlayersNumber(int playersNumber);

    void updateState(GameState gameState);

    void notifyWinners(List<String> winners);

    void updateCurrPlayer(int currPlayer);

    void notifyTwentyPointsReached(boolean twentyPointsReached);

    void notifyAdditionalRound(boolean additionalRound);

    void updateCommandResult(CommandResult commandResult);

    // TODO penso che potrei accorparle in playerJoined(nickname)
    void addPlayerView();
    void addGameFieldView();
}
