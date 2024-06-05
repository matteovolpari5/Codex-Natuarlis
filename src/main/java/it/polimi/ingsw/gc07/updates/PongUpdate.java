package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model_view.GameView;

/**
 * Update used to send a pong to the client.
 */
public class PongUpdate implements Update{
    /**
     * Execute method of the concrete update: used by the client to check connection state.
     * @param gameView game view
     */
    @Override
    public void execute(GameView gameView) {}
}
