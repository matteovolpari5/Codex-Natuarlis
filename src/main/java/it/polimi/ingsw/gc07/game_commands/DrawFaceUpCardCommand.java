package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.enumerations.CardType;

/**
 * Concrete command to draw one of two faceUp cards of a given type.
 */
public class DrawFaceUpCardCommand implements GameControllerCommand {
    /**
     * Nickname of the player.
     */
    private final String nickname;
    /**
     * Type of the card a user wants to draw.
     */
    private final CardType type;
    /**
     * Position in the List of faceUpCards.
     */
    private final int pos;

    /**
     * Constructor of the concrete command DrawFaceUpCardCommand.
     * @param nickname nickname
     * @param type type
     * @param pos pos
     */
    public DrawFaceUpCardCommand(String nickname, CardType type, int pos) {
        this.nickname = nickname;
        this.type = type;
        this.pos = pos;
    }

    /**
     * Method that allows a player to draw one of two faceUp cards of a given type.
     */
    @Override
    public void execute(GameController gameController) {
        gameController.drawFaceUpCard(nickname, type, pos);
    }
}
