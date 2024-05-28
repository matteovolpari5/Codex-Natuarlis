package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;

/**
 * Concrete command to place the starter card in a certain way.
 */
public class SetInitialCardsCommand implements GameControllerCommand {
    /**
     * Nickname of the player.
     */
    private final String nickname;
    /**
     * Way of the card.
     */
    private final boolean starterCardWay;
    /**
     * Chosen secret objective.
     * False = first
     * True = second
     */
    private final boolean choseSecretObjective;

    /**
     * Constructor of the concrete command SetInitialCardsCommand.
     * @param nickname nickname
     * @param starterCardWay starterCardWay
     * @param choseSecretObjective choseSecretObjective
     */
    public SetInitialCardsCommand(String nickname, boolean starterCardWay, boolean choseSecretObjective) {
        this.nickname = nickname;
        this.starterCardWay = starterCardWay;
        this.choseSecretObjective = choseSecretObjective;
    }

    /**
     * Method to place the starter card in a certain way.
     */
    @Override
    public void execute(GameController gameController) {
        gameController.placeStarterCard(nickname, starterCardWay, choseSecretObjective);
    }
}
