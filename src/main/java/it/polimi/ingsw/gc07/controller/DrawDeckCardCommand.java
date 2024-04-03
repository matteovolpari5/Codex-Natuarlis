package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.enumerations.CardType;

public class DrawDeckCardCommand implements GameCommand{
    /**
     *  Nickname of the player.
     */
    private final String nickname;

    /**
     * Type of the card a user wants to draw.
     */
    private final CardType type;

    /**
     * Game in which the command has to be executed.
     */
    private final Game game;

    /**
     *  Constructor of the concrete command DrawDeckCardCommand.
     * @param game game
     * @param nickname nickname of the player
     * @param type deck's type
     */
    public DrawDeckCardCommand (Game game, String nickname, CardType type){
        this.game = game;
        this.type = type;
        this.nickname = nickname;
    }

    @Override
    public void execute() {
        if(!game.getPlayers().get(game.getCurrPlayer()).getNickname().equals(nickname)){
            throw new WrongPlayerException();
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            throw new WrongCardTypeException();
        }

        if(type.equals(CardType.RESOURCE_CARD)){
            game.getPlayers().get(game.getCurrPlayer()).addCardHand(game.getResourceCardsDeck().drawCard());
        }
        if(type.equals(CardType.GOLD_CARD)){

            game.getPlayers().get(game.getCurrPlayer()).addCardHand(game.getGoldCardsDeck().drawCard());
        }
        try {
            game.changeCurrPlayer();
        }
        catch (WrongStateException | PlayerNotPresentException e)
        {
            e.printStackTrace();
            // TODO noooo
        }
    }

}
