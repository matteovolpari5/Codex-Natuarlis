package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.model_listeners.PlayerListener;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.controller.CommandResult;
import it.polimi.ingsw.gc07.updates.CardHandUpdate;
import it.polimi.ingsw.gc07.updates.ConnectionUpdate;
import it.polimi.ingsw.gc07.updates.SecretObjectivesUpdate;
import it.polimi.ingsw.gc07.updates.StallUpdate;

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
    private boolean connectionType;
    /**
     * Player's connection type.
     * true: GUI
     * false: Tui
     */
    private boolean interfaceType;
    /**
     * Boolean value representing if the player is connected.
     */
    private boolean isConnected;
    /**
     * List of cards the player currently has.
     */
    private final List<DrawableCard> currentHand;
    /**
     * Player's secret objective, it is an objective card.
     */
    private List<ObjectiveCard> secretObjectives;
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
        this.secretObjectives = new ArrayList<>();
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

    public void removeListener(PlayerListener playerListener) {
        playerListeners.remove(playerListener);
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

    public void setConnectionType(boolean connectionType) {
        this.connectionType = connectionType;
    }

    /**
     * Getter for the connection type.
     * @return true = RMI, false = SOCKET
     */
    public boolean getConnectionType(){
        return this.connectionType;
    }

    public void setInterfaceType(boolean interfaceType) {
        this.interfaceType = interfaceType;
    }

    /**
     * Getter method for the interface type.
     * @return true = GUI, false = Tui
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
                // will be detected by PingPongManager
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
                // will be detected by PingPongManager
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
        // send update
        sendCardHandUpdate();
    }

    /**
     * Method that adds a card from the hand of a player.
     * @param card: card that the player draw
     */
    public void addCardHand(DrawableCard card) {
        currentHand.add(card);
        // send update
        sendCardHandUpdate();
    }

    private void sendCardHandUpdate() {
        CardHandUpdate update = new CardHandUpdate(nickname, new ArrayList<>(currentHand), new ArrayList<>(secretObjectives));
        for(PlayerListener l: playerListeners) {
            try {
                l.receiveCardHandUpdate(update);
            }catch(RemoteException e) {
                // will be detected by PingPongManager
            }
        }
    }

    /**
     * Setter method for secretObjective attribute
     * @param secretObjectives cards that have to be set
     */
    public void setSecretObjectives(List<ObjectiveCard> secretObjectives) {
        this.secretObjectives = secretObjectives;

        // send update
        SecretObjectivesUpdate update = new SecretObjectivesUpdate(nickname, new ArrayList<>(secretObjectives));
        for(PlayerListener l: playerListeners) {
            try {
                l.receiveSecretObjectivesUpdate(update);
            } catch (RemoteException e) {
                // will be detected by PingPongManager
            }
        }
    }

    /**
     * Method used to choose a secret objective.
     * False - the player has chosen the first secret objective
     * True - the player has chosen the second secret objective
     * @param chosenSecretObjective chosen secret objective
     */
    public void chooseSecretObjective(boolean chosenSecretObjective) {
        assert(secretObjectives.size() == 2);

        // remove not chosen secret objective
        if(!chosenSecretObjective) {
            secretObjectives.remove(1);
        }else {
            secretObjectives.removeFirst();
        }

        sendCardHandUpdate();
    }

    /**
     * Getter for the secret objective card.
     * @return secret objective card
     */
    public List<ObjectiveCard> getSecretObjectives() {
        // card is immutable, I can return it
        return new ArrayList<>(secretObjectives);
    }

    /**
     * Getter method for the start card.
     * @return starter card
     */
    public PlaceableCard getStarterCard() {
        return gameField.getStarterCard();
    }

    /**
     * Method used to place a card.
     * @param card card
     * @param x x position in the game field
     * @param y y position in the game field
     * @param way way in the game field
     * @return result of the placement
     */
    public CommandResult placeCard(PlaceableCard card, int x, int y, boolean way) {
        return gameField.placeCard(nickname, card, x, y, way);
    }
}
