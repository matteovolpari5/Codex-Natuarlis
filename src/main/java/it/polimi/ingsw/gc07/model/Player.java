package it.polimi.ingsw.gc07.model;

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
     * Boolean value representing if the player is the first one.
     * true: the player is first
     * false: the player is not first
     */
    private boolean isFirst;
    /**
     * List of cards the player currently has.
     */
    private List<NonStarterCard> currentHand;
    /**
     * Player's game field.
     */
    private GameField gameField;
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
     * @param isFirst true for first player
     * @param currentHand current hand
     * @param gameField player's game field
     * @param secretObjective player's secret objective card
     * @param connectionType player's connection type
     */
    // TODO: prendo riferimento a GameField
    public Player(String nickname, TokenColor tokenColor, boolean isFirst,
                  List<NonStarterCard> currentHand, GameField gameField,
                  ObjectiveCard secretObjective, boolean connectionType) {
        this.nickname = nickname;
        this.tokenColor = tokenColor;
        this.isFirst = isFirst;
        this.currentHand = new ArrayList<>(currentHand);
        this.gameField = new GameField(gameField);
        this.secretObjective = secretObjective;
        this.connectionType = connectionType;
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

    /**
     * Getter for attribute isFirst
     * @return true if the player is first
     */
    public boolean isFirst() {
        return isFirst;
    }

    /**
     * Setter for cards in player's hand.
     * @param currentHand current card in player's hand
     */
    public void setCurrentHand(List<NonStarterCard> currentHand){}

    /**
     * Getter for cards in player's hand.
     * @return currentHand current card in player's hand
     */
    public List<NonStarterCard> getCurrentHand() {
        return new ArrayList<>(currentHand);
    }

    /**
     * Getter for game field.
     * @return game field
     */
    // TODO: sfugge riferimento a GameField
    public GameField getGameField() {
        return gameField;
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
    public boolean getConnectionType(){}
}
