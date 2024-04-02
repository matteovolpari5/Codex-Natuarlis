package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.enumerations.GameState;

public class placeCardCommand implements GameCommand{
    /**
     * Game in which the command has to be executed.
     */
    private final Game game;
    /**
     * nickname of the player that will place the card
     */
    private final String nickname;
    /**
     * card that will be placed
     */
    private final DrawableCard card;
    /**
     * row of the matrix
     */
    private final int x;
    /**
     * column of the matrix
     */
    private final int y;
    /**
     * face of the card
     */
    private final boolean way;

    public placeCardCommand(Game game, String nickname, DrawableCard card, int x, int y, boolean way) {
        this.game = game;
        this.nickname = nickname;
        this.card = card;
        this.x = x;
        this.y = y;
        this.way = way;
    }
    /**
     * method that place a card in the game field of the current player
     * this method also remove the card placed from the hand of the current player and calls the method that compute the points scored by placing the card
     * @return : command result
     */
    @Override
    public CommandResult execute() {
        if(!game.getPlayers().get(game.getCurrPlayer()).getNickname().equals(nickname)) {
            return ManageCardsResult.PLAYER_NOT_PRESENT;
        }
        if(!(game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()).contains(card)){
            return ManageCardsResult.CARD_NOT_PRESENT;
        }
        if(!game.getState().equals(GameState.PLAYING)){
            return ManageCardsResult.WRONG_STATE;
        }
        PlacementResult result = game.getPlayersGameField().get(nickname).placeCard(card,x,y,way);
        game.getPlayers().get(game.getCurrPlayer()).removeCardHand(card);
        try {
            addPoints(nickname,x,y);
        } catch (PlayerNotPresentException e) {
            return ManageCardsResult.PLAYER_NOT_PRESENT;
        } catch (WrongStateException e) {
            return ManageCardsResult.WRONG_STATE;
        } catch (WrongPlayerException e) {
            return ManageCardsResult.WRONG_PLAYER;
        } catch (CardNotPresentException e) {
            return ManageCardsResult.CARD_NOT_PRESENT;
        }
        boolean isStalled = true;
        PlacementResult resultStall;
        // check if a card is placeable
        for(int i = 0; i < GameField.getDim() && isStalled; i++) {
            for (int j = 0; j < GameField.getDim() && isStalled; j++) {
                // check if the firs card (a casual card), is placeable on the back,
                // i.e. check only the indexes
                try {
                    resultStall = game.getPlayers().get(game.getPlayerByNickname(nickname)).getCurrentHand().getFirst()
                            .isPlaceable(new GameField(game.getPlayersGameField().get(nickname)), i, j, true);
                    if (resultStall.equals(PlacementResult.SUCCESS)) {
                        isStalled = false;
                    }
                } catch (PlayerNotPresentException e) {
                    return ManageCardsResult.PLAYER_NOT_PRESENT;
                }
            }
        }
        game.getPlayers().get(game.getCurrPlayer()).setIsStalled(isStalled);
        return result;
    }

    /**
     * method that add points to a player and check if a player is reaching 20 points.
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
    }
}
