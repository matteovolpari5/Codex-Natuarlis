package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.model_view.GameView;

import java.util.List;

public class GameModelUpdate implements Update {
    /**
     * Game id.
     */
    private final int id;
    /**
     * Game players number.
     */
    private final int playersNumber;
    /**
     * Game state.
     */
    private final GameState state;
    /**
     * List of winners.
     */
    private final List<String> winners;
    /**
     * Current player.
     */
    private final int currPlayer;
    /**
     * Boolean value for twentyPointsReached.
     */
    private final boolean twentyPointsReached;
    /**
     * Boolean value for additionalRound.
     */
    private final boolean additionalRound;

    /**
     * Constructor of GameModelUpdate.
     * @param id id
     * @param playersNumber players number
     * @param state state
     * @param winners winners
     * @param currPlayer current player
     */
    public GameModelUpdate(int id, int playersNumber, GameState state, List<String> winners, int currPlayer, boolean twentyPointsReached, boolean additionalRound) {
        this.id = id;
        this.playersNumber = playersNumber;
        this.state = state;
        this.winners = winners;
        this.currPlayer = currPlayer;
        this.twentyPointsReached = twentyPointsReached;
        this.additionalRound = additionalRound;
    }

    /**
     * Execute method of the concrete update: allows to notify a game model update.
     * @param gameView gameView game view to update
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setGameModel(id, playersNumber, state, winners, currPlayer, twentyPointsReached, additionalRound);
    }
}

