package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
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
    private final String nickname;
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
    private final boolean connectionType;
    /**
     * Player's connection type.
     * true: GUI
     * false: TUI
     */
    private final boolean interfaceType;
    /**
     * Boolean value representing if the player is connected.
     */
    private boolean isConnected;
    /**
     * List of cards the player currently has.
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
     * @param connectionType player's connection type
     */
    public Player(String nickname, boolean connectionType, boolean interfaceType) {
        this.nickname = nickname;
        this.tokenColor = null;
        this.isFirst = false;   // will be set true only for the first player
        this.connectionType = connectionType;
        this.interfaceType = interfaceType;
        this.isConnected = true;
        this.currentHand = new ArrayList<>();
        this.secretObjective = null;
        this.isStalled = false;
    }

    /**
     * Copy constructor of class Player.
     * @param existingPlayer player to copy
     */
    public Player(Player existingPlayer) {
        this.nickname = existingPlayer.nickname;
        this.tokenColor = existingPlayer.tokenColor;
        this.isFirst = existingPlayer.isFirst;
        this.connectionType = existingPlayer.connectionType;
        this.interfaceType = existingPlayer.interfaceType;
        this.isConnected = existingPlayer.isConnected;
        this.currentHand = new ArrayList<>(existingPlayer.currentHand);
        this.secretObjective = existingPlayer.secretObjective;
        this.isStalled = existingPlayer.isStalled;
    }

    /**
     * Getter for nickname.
     * @return this.nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Setter for tokenColor.
     * @param tokenColor token color
     */
    public void setTokenColor(TokenColor tokenColor) {
        this.tokenColor = tokenColor;
    }

    /**
     * Getter for token color.
     * @return token color
     */
    public TokenColor getTokenColor() {
        return tokenColor;
    }

    /**
     * Setter for attribute isFirst.
     */
    public void setFirst(){
        this.isFirst = true;
    }

    /**
     * Getter for attribute isFirst.
     * @return true if the player is first
     */
    public boolean isFirst() {
        return isFirst;
    }

    /**
     * Getter for the connection type.
     * @return true = RMI, false = SOCKET
     */
    public boolean getConnectionType(){
        return this.connectionType;
    }

    /**
     * Getter method for the interface type.
     * @return true = GUI, false = TUI
     */
    public boolean getInterfaceType(){
        return this.interfaceType;
    }

    /**
     *  getter for attribute isStalled
     */
    public boolean getIsStalled()
    {
        return this.isStalled;
    }

    /**
     *  setter for attribute isStalled
     */
    public void setIsStalled(boolean isStalled)
    {
        this.isStalled=isStalled;
    }

    /**
     * Getter method for the player's connection state.
     * @return true if the player is connected
     */
    public boolean isConnected(){
        return this.isConnected;
    }

    /**
     * Setter for the method isConnected.
     * @param isConnected: true if the player is connected
     */
    public void setIsConnected(boolean isConnected){
        this.isConnected = isConnected;
    }
    /**
     * Getter for cards in player's hand.
     * @return currentHand current card in player's hand
     */
    public List<DrawableCard> getCurrentHand() {
        return new ArrayList<>(currentHand);
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

    /**
     * Getter for the secret objective card.
     * @return secret objective card
     */
    public ObjectiveCard getSecretObjective() {
        // card is immutable, I can return it
        return secretObjective;
    }
}
