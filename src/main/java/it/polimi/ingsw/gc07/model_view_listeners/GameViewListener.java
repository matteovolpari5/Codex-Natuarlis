package it.polimi.ingsw.gc07.model_view_listeners;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.controller.CommandResult;
import it.polimi.ingsw.gc07.model.TokenColor;

import java.util.List;
import java.util.Map;

public interface GameViewListener {
    /**
     * Method used to show the game id.
     * @param gameId game id
     */
    void receiveGameIdUpdate(int gameId);

    /**
     * Method used to receive general game info.
     * @param gameState game state
     * @param currPlayerNickname current player
     */
    void receiveGeneralModelUpdate(GameState gameState, String currPlayerNickname);

    /**
     * Method used to inform the player that the current round is the penultimate round.
     */
    void receivePenultimateRoundUpdate();

    /**
     * Method used to inform the player that the current round is the additional round.
     */
    void receiveAdditionalRoundUpdate();

    /**
     * Method used to receive the last command result.
     * @param commandResult command result
     */
    void receiveCommandResultUpdate(CommandResult commandResult);

    /**
     * Method used to display existing and free games.
     * @param existingGamesPlayerNumber players number for each game
     * @param existingGamesTokenColor taken token colors for each game
     */
    void receiveExistingGamesUpdate(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor);

    /**
     * Method used to display winners.
     * @param winners winners' nicknames
     */
    void receiveWinnersUpdate(List<String> winners);

    /**
     * Method used to display game players.
     * @param nicknames map containing players and their token colors
     * @param connectionValues map containing players and their connection values
     * @param stallValues map containing players and their stall values
     */
    void receivePlayersUpdate(Map<String, TokenColor> nicknames, Map<String, Boolean> connectionValues, Map<String, Boolean> stallValues);
}
