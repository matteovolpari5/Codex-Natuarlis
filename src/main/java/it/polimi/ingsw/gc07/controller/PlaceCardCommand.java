package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;

/**
 * Concrete command to place a card.
 */
public class PlaceCardCommand implements GameCommand {
    /**
     * Game in which the command has to be executed.
     */
    private final Game game;
    /**
     * Nickname of the player that will place the card.
     */
    private final String nickname;
    /**
     * Card that will be placed.
     */
    private final DrawableCard card;
    /**
     * Row in the matrix.
     */
    private final int x;
    /**
     * Column in the matrix.
     */
    private final int y;
    /**
     * Way of the card.
     */
    private final boolean way;

    /**
     * Constructor of the concrete command PlaceCardCommand.
     * @param game game
     * @param nickname nickname
     * @param card card
     * @param x row
     * @param y column
     * @param way way
     */
    public PlaceCardCommand(Game game, String nickname, DrawableCard card, int x, int y, boolean way) {
        this.game = game;
        this.nickname = nickname;
        this.card = card;
        this.x = x;
        this.y = y;
        this.way = way;
    }

    /**
     * Method to place a card in the game field of the current player.
     * This method also removes the card placed from the hand of the current player and calls
     * the method that computes the points scored by placing the card.
     * @return : command result
     */
    @Override
    public CommandResult execute() {
        if(!game.getPlayers().get(game.getCurrPlayer()).getNickname().equals(nickname)) {
            return CommandResult.WRONG_PLAYER;
        }
        if(!(game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()).contains(card)){
            return CommandResult.CARD_NOT_PRESENT;
        }
        if(!game.getState().equals(GameState.PLAYING)){
            return CommandResult.WRONG_STATE;
        }
        CommandResult result = game.getPlayersGameField().get(nickname).placeCard(card,x,y,way);
        if(result.equals(CommandResult.SUCCESS))
        {
            game.getPlayers().get(game.getCurrPlayer()).removeCardHand(card);
            try {
                addPoints(nickname,x,y);
            } catch (PlayerNotPresentException e) {
                return CommandResult.PLAYER_NOT_PRESENT;
            } catch (WrongStateException e) {
                return CommandResult.WRONG_STATE;
            } catch (WrongPlayerException e) {
                return CommandResult.WRONG_PLAYER;
            } catch (CardNotPresentException e) {
                return CommandResult.CARD_NOT_PRESENT;
            }
        }
        return result;
    }

    /**
     * Method that adds points to a player and checks if a player had reached 20 points.
     * @param nickname: nickname of the player
     * @param x: where the card is placed in the matrix
     * @param y: where the card is placed in the matrix
     * @throws WrongPlayerException : if the player is not the current player.
     * @throws PlayerNotPresentException : if the player is not present in the List players.
     * @throws CardNotPresentException: if there isn't a card in the specified position of the game field.
     */
    private void addPoints(String nickname, int x, int y) throws WrongPlayerException, CardNotPresentException, PlayerNotPresentException, WrongStateException {
        if (!game.getState().equals(GameState.PLAYING)){
            throw new WrongStateException();
        }
        int deltaPoints;
        if(!game.getPlayers().get(game.getCurrPlayer()).getNickname().equals(nickname))
        {
            throw new WrongPlayerException();
        }
        assert (game.getPlayersGameField().get(nickname).isCardPresent(x, y)) : "there isn't a Card in the x,y position";
        deltaPoints = game.getPlayersGameField().get(nickname).getPlacedCard(x, y).getPlacementScore(game.getPlayersGameField().get(nickname), x, y);
        if(deltaPoints + game.getScoreTrackBoard().getScore(nickname) >= 20){
            game.setTwentyPointsReached(true);
            if((deltaPoints + game.getScoreTrackBoard().getScore(nickname)) > 29){
                game.getScoreTrackBoard().setScore(nickname, 29);
            }
            else{
                game.getScoreTrackBoard().incrementScore(nickname, deltaPoints);
            }
        }
        else
        {
            game.getScoreTrackBoard().incrementScore(nickname, deltaPoints);
        }
    }
}
