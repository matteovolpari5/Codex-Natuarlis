package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.model.cards.NonStarterCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.List;
import java.util.ArrayList;

/**
 * Class representing a player.
 */
public class Player {
    /**
     * Player's nickname.
     */
    private String nickname;
    /**
     * Color of the player's token.
     */
    private TokenColor tokenColor;
    /**
     * Boolean value representing if the player is first.
     */
    private boolean isFirst;
    /**
     * Boolean value representing if the player is connected.
     */
    private boolean isConnected;
    /**
     * List of cards the player currently has.
     */
    private List<NonStarterCard> currentHand;
    /**
     * Player's secret objective, it is an objective card.
     */
    private ObjectiveCard secretObjective;
    /**
     * Player's connection type.
     * true: RMI
     * false: socket
     */
    private boolean connectionType;

    /**
     * Constructor of class player
     * @param nickname player's nickname
     * @param tokenColor player's token color
     * @param currentHand current hand
     * @param secretObjective player's secret objective card
     * @param connectionType player's connection type
     */
    public Player(String nickname, TokenColor tokenColor, List<NonStarterCard> currentHand,
                  ObjectiveCard secretObjective, boolean connectionType) {
        this.nickname = nickname;
        this.tokenColor = tokenColor;
        this.isConnected = true;
        this.currentHand = new ArrayList<>(currentHand);
        this.secretObjective = secretObjective;
        this.connectionType = connectionType;
        this.isFirst = false;
    }

    public Player(Player existingPlayer) {
        this.nickname = existingPlayer.nickname;
        this.tokenColor = existingPlayer.tokenColor;
        this.isFirst = existingPlayer.isFirst;
        this.isConnected = existingPlayer.isConnected;
        this.currentHand = new ArrayList<>(existingPlayer.currentHand);
        this.secretObjective = existingPlayer.secretObjective;
        this.connectionType = existingPlayer.connectionType;
    }

    /**
     * Getter for nickname.
     * @return this.nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter for token color.
     * @return token color
     */
    public TokenColor getTokenColor() {
        return tokenColor;
    }

    public void setFirst(){
        this.isFirst = true;
    }

    /**
     * Getter for attribute isFirst
     * @return true if the player is first
     */
    public boolean isFirst() {
        return isFirst;
    }

    /**
     * Getter method for the player's connection state.
     * @return
     */
    public boolean isConnected(){
        return this.isConnected;
    }

    public void setIsConnected(boolean isConnected){
        this.isConnected = isConnected;
    }

    /**
     * Setter for cards in player's hand.
     * @param currentHand current card in player's hand
     */
    public void setCurrentHand(List<NonStarterCard> currentHand){
        // TODO
    }

    /**
     * Getter for cards in player's hand.
     * @return currentHand current card in player's hand
     */
    public List<NonStarterCard> getCurrentHand() {
        return new ArrayList<>(currentHand);
    }

    /**
     * Getter for the secret objective card.
     * @return secret objective card
     */
    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    /**
     * Getter for the connection type
     * @return connection type
     */
    public boolean getConnectionType(){
        return this.connectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof Player)) {
            return false;
        }
    }

    @Override
    public int hashCode(){

    }
}
