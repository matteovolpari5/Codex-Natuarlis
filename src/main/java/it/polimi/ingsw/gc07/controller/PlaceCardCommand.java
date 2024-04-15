package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;

/**
 * Concrete command to place a card.
 */
public class PlaceCardCommand extends GameCommand {
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
        setGame(game);
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
     */
    @Override
    public void execute() {
        Game game = getGame();

        if(!game.getState().equals(GameState.PLAYING)){
            game.getCommandResultManager().setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(!game.getPlayers().get(game.getCurrPlayer()).getNickname().equals(nickname)) {
            game.getCommandResultManager().setCommandResult(CommandResult.WRONG_PLAYER);
            return;
        }
        if(!(game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()).contains(card)){
            game.getCommandResultManager().setCommandResult(CommandResult.CARD_NOT_PRESENT);
            return;
        }
        CommandResult result = game.getPlayersGameField().get(nickname).placeCard(card,x,y,way);
        if(result.equals(CommandResult.SUCCESS)) {
            game.getPlayers().get(game.getCurrPlayer()).removeCardHand(card);
            addPoints(nickname,x,y);    // the card has just been placed

            // check if the player is stalled
            boolean isStalled = true;
            CommandResult resultStall;
            for(int i = 0; i < GameField.getDim() && isStalled; i++) {
                for (int j = 0; j < GameField.getDim() && isStalled; j++) {
                    // check if the firs card (a casual card), is placeable on the back,
                    // i.e. check only the indexes
                    try {
                        resultStall = game.getPlayers().get(game.getPlayerByNickname(nickname)).getCurrentHand().getFirst()
                                .isPlaceable(new GameField(game.getPlayersGameField().get(nickname)), i, j, true);
                        if (resultStall.equals(CommandResult.SUCCESS)) {
                            isStalled = false;
                        }
                    } catch (PlayerNotPresentException e) {
                        // the current player must be present
                        throw new RuntimeException();
                    }
                }
            }
            game.getPlayers().get(game.getCurrPlayer()).setIsStalled(isStalled);
        }
        game.getCommandResultManager().setCommandResult(result);
    }

    /**
     * Method that adds points to a player and checks if a player had reached 20 points.
     * @param nickname: nickname of the player
     * @param x: where the card is placed in the matrix
     * @param y: where the card is placed in the matrix
     */
    private void addPoints(String nickname, int x, int y) {
        Game game = getGame();

        assert(game.getState().equals(GameState.PLAYING)): "Wrong game state";
        assert(game.getPlayers().get(game.getCurrPlayer()).getNickname().equals(nickname)): "Not the current player";
        assert (game.getPlayersGameField().get(nickname).isCardPresent(x, y)) : "No card present in the provided position";
        int deltaPoints;
        deltaPoints = game.getPlayersGameField().get(nickname).getPlacedCard(x, y).getPlacementScore(game.getPlayersGameField().get(nickname), x, y);
        if(deltaPoints + game.getScoreTrackBoard().getScore(nickname) >= 20){
            game.setTwentyPointsReached();
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
