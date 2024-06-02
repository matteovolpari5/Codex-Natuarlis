package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model_view.GameView;

import java.util.List;
import java.util.Map;

public class ExistingGamesUpdate implements Update {
    /**
     * Map containing existing games.
     * Key = game id
     * Value = number of players
     */
    private final Map<Integer, Integer> existingGamesPlayerNumber;
    /**
     * Map containing taken token colors for existing games.
     * Key = game id
     * Value = list of taken token color for game
     */
    private final Map<Integer, List<TokenColor>> existingGamesTokenColor;
    /**
     * Constructor of ExistingGamesUpdate.
     * @param existingGamesPlayerNumber existing games ids
     * @param existingGamesTokenColor existing games taken token colors
     */
    public ExistingGamesUpdate(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {
        this.existingGamesPlayerNumber = existingGamesPlayerNumber;
        this.existingGamesTokenColor = existingGamesTokenColor;
    }

    /**
     * Execute method of the concrete update: reveal existing games.
     * @param gameView game view
     */
    @Override
    public void execute(GameView gameView) {
        gameView.displayExistingGames(existingGamesPlayerNumber, existingGamesTokenColor);
    }
}
