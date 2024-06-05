package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.game_commands.GameControllerCommand;
import it.polimi.ingsw.gc07.game_commands.GamesManagerCommand;
import it.polimi.ingsw.gc07.model_view.GameView;

/**
 * Interface representing a client.
 */
public interface Client {
    /**
     * Method used to set and execute a GamesManagerCommand.
     * @param gamesManagerCommand games manager command to execute
     */
    void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand);

    /**
     * Method used to set and execute a GameControllerCommand.
     * @param gameControllerCommand game controller command to execute
     */
    void setAndExecuteCommand(GameControllerCommand gameControllerCommand);

    /**
     * Method used to set a value for isAlive, i.e. to tell a client is alive or not.
     * @param isAlive true if the client is alive
     */
    void setClientAlive(boolean isAlive);

    /**
     * Method used to know if the client is alive or not.
     * @return true if the client is alive
     */
    boolean isClientAlive();

    /**
     * Method used to get player's game view.
     * @return player's game view
     */
    GameView getGameView();

    /**
     * Method used to run the lobby ui.
     */
    void runJoinGameInterface();

    /**
     * Method used to run the game ui.
     */
    void runGameInterface();
}
