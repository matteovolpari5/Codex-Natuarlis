package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.model.Player;

/**
 * Concrete command to add a new player to the game.
 */
public class AddPlayerCommand extends GameCommand {
    /**
     * Player to add.
     */
    private final Player newPlayer;

    /**
     * Constructor of the concrete command.
     * This constructor takes parameter game, used by the server.
     * @param newPlayer player to add
     */
    public AddPlayerCommand(Player newPlayer) {
        this.newPlayer = newPlayer;
    }

    /**
     * Override of method execute to add a new player to the game.
     */
    @Override
    public void execute(Game game) {
        game.addPlayer(newPlayer);
    }
}
