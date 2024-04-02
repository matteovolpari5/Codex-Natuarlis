package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.enumerations.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Concrete command to add a new player to the game.
 */
public class AddPlayerCommand implements GameCommand {
    /**
     * Game in which the command has to be executed.
     */
    private final Game game;
    /**
     * Player to add.
     */
    private final Player newPlayer;

    /**
     * Constructor of the concrete command.
     * @param game game
     * @param newPlayer player to add
     */
    public AddPlayerCommand(Game game, Player newPlayer) {
        this.game = game;
        this.newPlayer = newPlayer;
    }

    /**
     * Override of method execute to add a new player to the game.
     * @return command result
     */
    @Override
    public CommandResult execute() {
        try{
            if(!game.getState().equals(GameState.WAITING_PLAYERS)) {
                return AddPlayerResult.WRONG_STATE;
            }

            newPlayer.addCardHand(game.getResourceCardsDeck().drawCard());
            newPlayer.addCardHand(game.getResourceCardsDeck().drawCard());
            newPlayer.addCardHand(game.getGoldCardsDeck().drawCard());
            newPlayer.setSecretObjective(game.getObjectiveCardsDeck().drawCard());

            PlaceableCard starterCard = game.getStarterCardsDeck().drawCard();
            GameField gameField = new GameField(starterCard);
            game.getPlayers().add(newPlayer);
            game.getPlayersGameField().put(newPlayer.getNickname(), gameField);
            game.getScoreTrackBoard().addPlayer(newPlayer.getNickname());
            if (isFull()) {
                setup();
                game.setState(GameState.PLAYING);
            }
        } catch (CardNotPresentException e) {
            // the exception can't occur since the game is not started yet
            throw new RuntimeException();
        }
        return AddPlayerResult.SUCCESS;
    }

    /**
     * Method telling if there are available places in the game.
     * @return true if no other player can connect to the game
     */
    private boolean isFull(){
        return game.getPlayers().size() == game.getPlayersNumber();
    }

    /**
     * Method to set up the game: the first player is chosen and 4 cards (2 gold and 2 resource) are revealed.
     */
    private void setup() {
        assert(game.getState().equals(GameState.WAITING_PLAYERS)): "The state is not waiting players";
        // choose randomly the first player
        Random random= new Random();
        game.setCurrPlayer(random.nextInt(game.getPlayersNumber()));
        game.getPlayers().get(game.getCurrPlayer()).setFirst();
        try {
            //place 2 gold cards
            List<GoldCard> setUpGoldCardsFaceUp = new ArrayList<>();
            setUpGoldCardsFaceUp.add(game.getGoldCardsDeck().drawCard());
            setUpGoldCardsFaceUp.add(game.getGoldCardsDeck().drawCard());
            game.getGoldCardsDeck().setFaceUpCards(setUpGoldCardsFaceUp);

            //place 2 resource card
            List<DrawableCard> setUpResourceCardsFaceUp = new ArrayList<>();
            setUpResourceCardsFaceUp.add(game.getResourceCardsDeck().drawCard());
            setUpResourceCardsFaceUp.add(game.getResourceCardsDeck().drawCard());
            game.getResourceCardsDeck().setFaceUpCards(setUpResourceCardsFaceUp);
        } catch (CardNotPresentException e) {
            // the exception can't occur since the game is not started yet
            throw new RuntimeException();
        }
    }
}
