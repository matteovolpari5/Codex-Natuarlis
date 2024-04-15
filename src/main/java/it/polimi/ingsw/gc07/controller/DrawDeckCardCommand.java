package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.enumerations.CardType;

public class DrawDeckCardCommand extends GameCommand {
    /**
     *  Nickname of the player.
     */
    private final String nickname;

    /**
     * Type of the card a user wants to draw.
     */
    private final CardType type;

    /**
     * Constructor of the concrete command DrawDeckCardCommand.
     * This constructor takes parameter game, used by the server.
     * @param game game
     * @param nickname nickname of the player
     * @param type deck's type
     */
    public DrawDeckCardCommand(Game game, String nickname, CardType type) {
        setGame(game);
        this.type = type;
        this.nickname = nickname;
    }

    /**
     * Constructor of the concrete command DrawDeckCardCommand.
     * This constructor doesn't take a game as parameter, used by the client.
     * @param nickname nickname of the player
     * @param type deck's type
     */
    public DrawDeckCardCommand(String nickname, CardType type) {
        setGame(null);
        this.type = type;
        this.nickname = nickname;
    }

    /**
     * Method that allows a player to draw one card from a GoldCardDeck or a ResourceCardDeck.
     */
    @Override
    public void execute() {
        Game game = getGame();

        if(!game.getState().equals(GameState.PLAYING)) {
            game.getCommandResultManager().setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        // if the state is PLAYING ...
        if(!game.getPlayers().get(game.getCurrPlayer()).getNickname().equals(nickname)){
            game.getCommandResultManager().setCommandResult(CommandResult.WRONG_PLAYER);
            return;
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            game.getCommandResultManager().setCommandResult(CommandResult.WRONG_CARD_TYPE);
            return;
        }
        DrawableCard card;
        if (type.equals(CardType.RESOURCE_CARD)) {
            card = game.getResourceCardsDeck().drawCard();
            if(card == null) {
                game.getCommandResultManager().setCommandResult(CommandResult.CARD_NOT_PRESENT);
                return;
            }
            game.getPlayers().get(game.getCurrPlayer()).addCardHand(card);
        }
        if (type.equals(CardType.GOLD_CARD)) {
            card = game.getGoldCardsDeck().drawCard();
            if(card == null){
                game.getCommandResultManager().setCommandResult(CommandResult.CARD_NOT_PRESENT);
                return;
            }
            game.getPlayers().get(game.getCurrPlayer()).addCardHand(card);
        }
        game.changeCurrPlayer();
        game.getCommandResultManager().setCommandResult(CommandResult.SUCCESS);
    }
}
