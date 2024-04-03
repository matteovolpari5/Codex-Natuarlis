package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.enumerations.CardType;

/**
 * Concrete command to draw one of two faceUp cards of a given type.
 */
public class DrawFaceUpCardCommand implements GameCommand{
    /**
     * Game in which the command has to be executed.
     */
    private final Game game;

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
    public DrawFaceUpCardCommand(Game game, String nickname, CardType type, int pos) {
        this.game = game;
        this.nickname = nickname;
        this.type = type;
        this.pos = pos;
    }

    /**
     * Method that allows a player to draw one of two faceUp cards of a given type.
     * @return command result
     */
    @Override
    public CommandResult execute() {
        if(!game.getPlayers().get(game.getCurrPlayer()).getNickname().equals(nickname)){
            return CommandResult.WRONG_PLAYER;
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            return CommandResult.WRONG_CARD_TYPE;
        }
        if(type.equals(CardType.RESOURCE_CARD)){
            try{
                game.getPlayers().get(game.getCurrPlayer()).addCardHand(game.getResourceCardsDeck().drawFaceUpCard(pos));
            } catch (CardNotPresentException e) {
                return CommandResult.CARD_NOT_PRESENT;
            }
        }
        if(type.equals(CardType.GOLD_CARD)){
            try{
                game.getPlayers().get(game.getCurrPlayer()).addCardHand(game.getGoldCardsDeck().drawFaceUpCard(pos));
            } catch (CardNotPresentException e) {
                return CommandResult.CARD_NOT_PRESENT;
            }
        }
        try {
            game.changeCurrPlayer();
        } catch (CardNotPresentException e){
            return CommandResult.CARD_NOT_PRESENT;
        } catch (WrongStateException e){
            return CommandResult.WRONG_STATE;
        } catch (PlayerNotPresentException e){
            return CommandResult.PLAYER_NOT_PRESENT;
        }
        return CommandResult.SUCCESS;
    }
}
