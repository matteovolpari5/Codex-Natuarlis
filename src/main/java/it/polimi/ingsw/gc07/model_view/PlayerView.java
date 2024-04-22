package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.ArrayList;
import java.util.List;

public class PlayerView {
    /**
     * Player's nickname.
     */
    private final String nickname;
    /**
     * Color of the player's token.
     */
    private TokenColor tokenColor;
    /**
     * Boolean value representing if the player is connected.
     */
    private boolean isConnected;
    /**
     * List of cards the player currently has. update needs to be sent only to the single player
     */
    private List<DrawableCard> currentHand;
    /**
     * Player's secret objective, it is an objective card.
     */
    private ObjectiveCard secretObjective;
    /**
     * Attribute telling if the player is stalled.
     */
    private boolean isStalled;

    /**
     * Constructor of class player
     * @param nickname player's nickname
     */
    public PlayerView(String nickname) {
        this.nickname = nickname;
        this.tokenColor = null;
        this.isConnected = true;
        this.currentHand = new ArrayList<>();
        this.secretObjective = null;
        this.isStalled = false;
    }
    /**
     * Setter for tokenColor.
     * @param tokenColor token color
     */
    public void setTokenColor(TokenColor tokenColor) {
        this.tokenColor = tokenColor;
    }

    /**
     *  setter for attribute isStalled
     */
    public void setIsStalled(boolean isStalled)
    {
        this.isStalled=isStalled;
    }

    /**
     * Setter for the method isConnected.
     * @param isConnected: true if the player is connected
     */
    public void setIsConnected(boolean isConnected){
        this.isConnected = isConnected;
    }

    /**
     * Method that removes a card from the hand of a player.
     * @param card: card that the player play
     */
    public void removeCardHand(DrawableCard card) {
        currentHand.remove(card);
        this.currentHand = new ArrayList<>(currentHand);
    }

    /**
     * Method that adds a card from the hand of a player.
     * @param card: card that the player draw
     */
    public void addCardHand(DrawableCard card) {
        currentHand.add(card);
        this.currentHand = new ArrayList<>(currentHand);
    }

    /**
     * setter method for secretObjective attribute
     * @param secretObjective card that has to be set
     */
    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = secretObjective;
    }

}
