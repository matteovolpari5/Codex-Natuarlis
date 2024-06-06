package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.TokenColor;
import it.polimi.ingsw.gc07.model_view_listeners.GameFieldViewListener;
import it.polimi.ingsw.gc07.model_view_listeners.PlayerViewListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Client's copy of model Player.
 */
public class PlayerView implements Serializable {
    /**
     * Player's nickname.
     */
    private final String nickname;
    /**
     * Color of the player's token.
     */
    private final TokenColor tokenColor;
    /**
     * Player's secret objective, it is an objective card.
     */
    private List<ObjectiveCard> secretObjectives;
    /**
     * Boolean value representing if the player is connected.
     */
    private boolean isConnected;
    /**
     * Attribute telling if the player is stalled.
     */
    private boolean isStalled;
    /**
     * Game field view.
     */
    private final GameFieldView gameField;
    /**
     * List of cards the player currently has. update needs to be sent only to the single player
     */
    private List<DrawableCard> currentHand;
    /**
     * List of player view listeners.
     */
    private final List<PlayerViewListener> playerViewListeners;

    /**
     * Constructor of class player
     * @param nickname player's nickname
     */
    public PlayerView(String nickname, TokenColor tokenColor) {
        this.nickname = nickname;
        this.gameField = new GameFieldView();
        this.tokenColor = tokenColor;
        this.secretObjectives = new ArrayList<>();
        this.isConnected = true;
        this.currentHand = null;
        this.isStalled = false;
        this.playerViewListeners = new ArrayList<>();
    }

    /**
     * Getter method for connection state.
     * @return true if the player is connected
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Getter method for stall state.
     * @return true if the player is stalled
     */
    public boolean isStalled() {
        return isStalled;
    }

    /**
     * Method used to register a new listener.
     * @param playerViewListener player view listener
     */
    public synchronized void addListener(PlayerViewListener playerViewListener) {
        playerViewListeners.add(playerViewListener);
    }

    /**
     * Method used to register a new listener to the game field.
     * @param gameFieldViewListener game field view listener
     */
    public void addGameFieldListener(GameFieldViewListener gameFieldViewListener) {
        gameField.addListener(gameFieldViewListener);
    }

    /**
     * Getter for player's nickname.
     * @return player's nickname
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Getter for token color.
     * @return player's token color
     */
    public TokenColor getTokenColor() {
        return this.tokenColor;
    }

    /**
     * Setter for attribute isStalled.
     * @param isStalled boolean value for isStalled
     */
    public synchronized void setIsStalled(boolean isStalled) {
        this.isStalled = isStalled;
        for(PlayerViewListener p: playerViewListeners) {
            p.receiveStallUpdate(nickname, this.isStalled);
        }
    }

    /**
     * Setter for the method isConnected.
     * @param isConnected: true if the player is connected
     */
    public synchronized void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
        for(PlayerViewListener p: playerViewListeners) {
            p.receiveConnectionUpdate(nickname, this.isConnected);
        }
    }

    /**
     * Method used to set player's secret objectives.
     * @param nickname nickname
     * @param secretObjectives secret objectives
     */
    public synchronized void setSecretObjectives(String nickname, List<ObjectiveCard> secretObjectives) {
        this.secretObjectives = secretObjectives;
        for(PlayerViewListener l: playerViewListeners) {
            l.receiveSecretObjectives(nickname, new ArrayList<>(secretObjectives));
        }
    }

    /**
     * Method that allows to set the currentHand.
     * @param currentHand new current hand
     */
    public synchronized void setCardHand(List<DrawableCard> currentHand, List<ObjectiveCard> secretObjectives) {
        this.currentHand = currentHand;
        this.secretObjectives = secretObjectives;
        // update listeners
        for(PlayerViewListener l: playerViewListeners) {
            l.receiveCardHandUpdate(nickname, new ArrayList<>(this.currentHand), new ArrayList<>(this.secretObjectives));
        }
    }

    /**
     * Setter method for player's starter card.
     * @param starterCard starter card
     */
    public synchronized void setStarterCard(PlaceableCard starterCard) {
        gameField.setStarterCard(starterCard);
        // update listeners
        for(PlayerViewListener l: playerViewListeners) {
            l.receiveStarterCardUpdate(nickname, starterCard);
        }
    }

    /**
     * Method used to add a card to the game field.
     * @param card card
     * @param x x pos
     * @param y y pos
     * @param way way
     * @param orderPosition order position
     */
    public void addCard(PlaceableCard card, int x, int y, boolean way, int orderPosition) {
        gameField.addCard(nickname, card, x, y, way, orderPosition);
    }

    /**
     * Getter for player's current hand size.
     * @return current hand size
     */
    public int getCurrHandSize() {
        return currentHand.size();
    }

    /**
     * Getter for player's game field.
     * @return game field
     */
    public GameFieldView getGameField() {
        return new GameFieldView(gameField);
    }

    /**
     * Setter method for player's game field.
     */
    public void setFullGameField(String nickname,PlaceableCard starterCard, PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {
        gameField.setFullGameField(nickname,starterCard, cardsContent, cardsFace, cardsOrder);
    }
}

