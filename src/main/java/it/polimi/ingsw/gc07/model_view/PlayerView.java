package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model_view_listeners.GameFieldViewListener;
import it.polimi.ingsw.gc07.model_view_listeners.PlayerViewListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerView implements Serializable {
    /**
     * Player's nickname.
     */
    private final String nickname;
    /**
     * Game field view.
     */
    private final GameFieldView gameField;
    /**
     * Color of the player's token.
     */
    private final TokenColor tokenColor;
    /**
     * Player's secret objective, it is an objective card.
     */
    private final ObjectiveCard secretObjective;
    /**
     * Boolean value representing if the player is connected.
     */
    private boolean isConnected;
    /**
     * Attribute telling if the player is stalled.
     */
    private boolean isStalled;
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
    public PlayerView(String nickname, TokenColor tokenColor, ObjectiveCard secretObjective) {
        this.nickname = nickname;
        this.gameField = new GameFieldView();
        this.tokenColor = tokenColor;
        this.secretObjective = secretObjective;
        this.isConnected = true;
        this.currentHand = null;
        this.isStalled = false;
        this.playerViewListeners = new ArrayList<>();
    }

    /**
     * Method used to register a new listener.
     * @param playerViewListener player view listener
     */
    public void addListener(PlayerViewListener playerViewListener) {
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
    public void setIsStalled(boolean isStalled) {
        this.isStalled = isStalled;
    }
    /**
     * Setter for the method isConnected.
     * @param isConnected: true if the player is connected
     */
    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
    /**
     * Method that allows to set the currentHand.
     * @param currentHand new current hand
     */
    public void setCardHand(List<DrawableCard> currentHand) {
        this.currentHand = currentHand;
        // update listeners
        for(PlayerViewListener l: playerViewListeners) {
            l.receiveCardHandUpdate(currentHand, secretObjective);
        }
    }

    public void setStarterCard(PlaceableCard starterCard) {
        gameField.setStarterCard(starterCard);
    }

    public void addCard(PlaceableCard card, int x, int y, boolean way, int orderPosition) {
        gameField.addCard(card, x, y, way, orderPosition);
    }
}
