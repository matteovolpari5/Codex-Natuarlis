package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;

/**
 * Concrete command to place the starter card in a certain way.
 */
public class PlaceStarterCardCommand implements GameControllerCommand {
    /**
     * Nickname of the player.
     */
    private final String nickname;
    /**
     * Way of the card.
     */
    private final boolean way;

    /**
     * Constructor of the concrete command PlaceStarterCardCommand.
     * @param nickname nickname
     * @param way way
     */
    public PlaceStarterCardCommand(String nickname, boolean way) {
        this.nickname = nickname;
        this.way = way;
    }

    /**
     * Method to place the starter card in a certain way.
     */
    @Override
    public void execute(GameController gameController) {
        gameController.placeStarterCard(nickname, way);
    }
}
