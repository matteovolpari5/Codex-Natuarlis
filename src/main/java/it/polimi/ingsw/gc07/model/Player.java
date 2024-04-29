package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.listeners.PlayerListener;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.updates.CardHandUpdate;
import it.polimi.ingsw.gc07.updates.ConnectionUpdate;
import it.polimi.ingsw.gc07.updates.StallUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;
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
     * Player's game field;
     */
    private final GameField gameField;
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
     * List of player listeners.
     */
    private final List<PlayerListener> playerListeners;

    /**
     * Constructor of class player
     * @param nickname player's nickname
     * @param connectionType player's connection type
     */
    public Player(String nickname, boolean connectionType, boolean interfaceType) {
        this.nickname = nickname;
        this.gameField = new GameField();
        this.tokenColor = null;
        this.isFirst = false;   // will be set true only for the first player
        this.connectionType = connectionType;
        this.interfaceType = interfaceType;
        this.isConnected = true;
        this.currentHand = new ArrayList<>();
        this.secretObjective = null;
        this.isStalled = false;
        this.playerListeners = new ArrayList<>();
    }

    /**
     * Method to add a player listener.
     * @param playerListener new player listener
     */
    public void addListener(PlayerListener playerListener) {
        playerListeners.add(playerListener);
    }

    /**
     * Getter method for playerListeners.
     * @return list of player listeners
     */
    public List<PlayerListener> getListeners() {
        return this.playerListeners;
    }

    /**
     * Getter for nickname.
     * @return this.nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter for game field.
     * @return game field
     */
    public GameField getGameField() {
        return this.gameField;
    }

    /**
     * Setter method for player's starter card.
     * @param starterCard starter card
     */
    public void setStarterCard(PlaceableCard starterCard) {
        this.gameField.setStarterCard(nickname, starterCard);
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
     * Getter for attribute isStalled.
     */
    public boolean getIsStalled() {
        return this.isStalled;
    }

    /**
     * Setter for attribute isStalled.
     */
    public void setIsStalled(boolean isStalled) {
        this.isStalled = isStalled;

        // send update
        StallUpdate update = new StallUpdate(nickname, isStalled);
        for(PlayerListener l: playerListeners) {
            try {
                l.receiveStallUpdate(update);
            } catch (RemoteException e) {
                // TODO
                throw new RuntimeException(e);
            }
        }
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

        // send update
        ConnectionUpdate update = new ConnectionUpdate(nickname, isConnected);
        for(PlayerListener l: playerListeners) {
            try {
                l.receiveConnectionUpdate(update);
            }catch(RemoteException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
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

        // send update
        CardHandUpdate update = new CardHandUpdate(nickname, new ArrayList<>(currentHand));
        for(PlayerListener l: playerListeners) {
            try {
                l.receiveCardHandUpdate(update);
            }catch(RemoteException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    /**
     * Method that adds a card from the hand of a player.
     * @param card: card that the player draw
     */
    public void addCardHand(DrawableCard card) {
        currentHand.add(card);
        this.currentHand = new ArrayList<>(currentHand);

        // send update
        CardHandUpdate update = new CardHandUpdate(nickname, new ArrayList<>(currentHand));
        for(PlayerListener l: playerListeners) {
            try {
                l.receiveCardHandUpdate(update);
            }catch(RemoteException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
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

    public PlaceableCard getStarterCard() {
        return gameField.getStarterCard();
    }

    public CommandResult placeCard(PlaceableCard card, int x, int y, boolean way) {
        return gameField.placeCard(nickname, card, x, y, way);
    }
}
