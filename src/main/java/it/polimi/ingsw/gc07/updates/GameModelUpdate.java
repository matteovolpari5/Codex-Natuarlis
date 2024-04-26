package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.model_view.GameView;

import java.util.List;

// TODO id e playersNumber costanti, si possono inviare all'inizio?
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
     * Constructor of GameModelUpdate.
     * @param id id
     * @param playersNumber players number
     * @param state state
     * @param winners winners
     * @param currPlayer current player
     */
    public GameModelUpdate(int id, int playersNumber, GameState state, List<String> winners, int currPlayer) {
        this.id = id;
        this.playersNumber = playersNumber;
        this.state = state;
        this.winners = winners;
        this.currPlayer = currPlayer;
    }

    /**
     * Execute method of the concrete update: allows to notify a game model update.
     * @param gameView gameView game view to update
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setGameModel(id, playersNumber, state, winners, currPlayer);
    }
}

