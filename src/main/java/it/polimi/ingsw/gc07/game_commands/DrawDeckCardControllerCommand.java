package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.enumerations.CardType;

public class DrawDeckCardControllerCommand implements GameControllerCommand {
    /**
     *  Nickname of the player.
     */
    private final String nickname;

    /**
     * Type of the card a user wants to draw.
     */
    private final CardType type;

    /**
     * Constructor of the concrete command DrawDeckCardControllerCommand.
     * @param nickname nickname of the player
     * @param type deck's type
     */
    public DrawDeckCardControllerCommand(String nickname, CardType type) {
        this.type = type;
        this.nickname = nickname;
    }

    /**
     * Method that allows a player to draw one card from a GoldCardDeck or a ResourceCardDeck.
     */
    @Override
    public void execute(GameController gameController) {
        gameController.drawDeckCard(nickname, type);
    }
}
