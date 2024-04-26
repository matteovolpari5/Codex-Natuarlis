package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model_view.GameView;

public class StallUpdate implements Update {
    /**
     * Player's nickname.
     */
    private final String nickname;
    /**
     * Boolean value of isStalled.
     */
    private final boolean isStalled;

    /**
     * Constructor of StallUpdate.
     * @param nickname nickname
     * @param isStalled isStalle value
     */
    public StallUpdate(String nickname, boolean isStalled) {
        this.nickname = nickname;
        this.isStalled = isStalled;
    }

    /**
     * Execute method of the concrete update: allows to notify if a player is stalled.
     * @param gameView game view to update
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setIsStalled(nickname, isStalled);
    }
}
