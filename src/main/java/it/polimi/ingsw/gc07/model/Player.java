package it.polimi.ingsw.gc07.model;

import java.util.List;
import java.util.ArrayList;

public class Player {
    private String nickname;
    private TokenColor tokenColor;
    private boolean isFirst;
    private List<NonStarterCard> currentHand;
    private GameField gameField;
    private ObjectiveCard secretObjective;
    private boolean connectionType;

    // TODO: prendo riferimento a GameField
    public Player(String nickname, TokenColor tokenColor, boolean isFirst,
                  List<NonStarterCard> currentHand, GameField gameField,
                  ObjectiveCard secretObjective, boolean connectionType) {
        this.nickname = nickname;
        this.tokenColor = tokenColor;
        this.isFirst = isFirst;
        this.currentHand = new ArrayList<>(currentHand);
        this.gameField = gameField;
        this.secretObjective = secretObjective;
        this.connectionType = connectionType;
    }

    public String getNickname() {
        return nickname;
    }
    public TokenColor getTokenColor() {
        return tokenColor;
    }
    public boolean isFirst() {
        return isFirst;
    }
    public void setCurrentHand(List<NonStarterCard> currentHand){}
    public List<NonStarterCard> getCurrentHand() {
        return new ArrayList<>(currentHand);
    }
    // TODO: sfugge riferimento a GameField
    public GameField getGameField() {
        return gameField;
    }
    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }
    public boolean getConnectionType(){}
}
