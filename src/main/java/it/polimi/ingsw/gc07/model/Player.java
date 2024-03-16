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
     * Player's connection type.
     * true: RMI
     * false: socket
     */
    private boolean connectionType;

    /**
     * Player's connection type.
     * true: GUI
     * false: TUI
     */
    private boolean interfaceType;
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
     * Constructor of class player
     * @param nickname player's nickname
     * @param tokenColor player's token color
     * @param currentHand current hand
     * @param secretObjective player's secret objective card
     * @param connectionType player's connection type
     */
    public Player(String nickname, TokenColor tokenColor,
                  boolean connectionType, boolean interfaceType,
                  List<NonStarterCard> currentHand, ObjectiveCard secretObjective) {
        this.nickname = nickname;
        this.tokenColor = tokenColor;
        this.isFirst = false;   // will be set true only for the first player
        this.isConnected = true;
        this.connectionType = connectionType;
        this.interfaceType = interfaceType;
        this.currentHand = new ArrayList<>(currentHand);
        this.secretObjective = secretObjective;
    }

    public Player(Player existingPlayer) {
        this.nickname = existingPlayer.nickname;
        this.tokenColor = existingPlayer.tokenColor;
        this.isFirst = existingPlayer.isFirst;
        this.isConnected = existingPlayer.isConnected;
        this.connectionType = existingPlayer.connectionType;
        this.interfaceType = existingPlayer.interfaceType;
        this.currentHand = new ArrayList<>(existingPlayer.currentHand);
        this.secretObjective = existingPlayer.secretObjective;
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
        // create a copy of the list
        // cards are immutable, I can use them
        this.currentHand = new ArrayList<>(currentHand);
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
        // card is immutable, I can return it
        return secretObjective;
    }

    /**
     * Getter for the connection type
     * @return connection type
     */
    public boolean getConnectionType(){
        return this.connectionType;
    }

    public boolean getInterfaceType(){
        return this.interfaceType;
    }
}
