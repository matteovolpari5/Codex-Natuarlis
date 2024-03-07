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

    // TODO: prendo riferimento a GameField e secretObjective
    public Player(String nickname, TokenColor tokenColor, boolean isFirst,
                  List<NonStarterCard> currentHand, GameField gameField,
                  ObjectiveCard secretObjective) {
        this.nickname = nickname;
        this.tokenColor = tokenColor;
        this.isFirst = isFirst;
        this.currentHand = new ArrayList<>(currentHand);
        this.gameField = gameField;
        this.secretObjective = secretObjective;
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
    public List<NonStarterCard> getCurrentHand() {
        return new ArrayList<>(currentHand);
    }
    // TODO: sfugge riferimento a GameField
    public GameField getMyGameField() {
        return gameField;
    }
    // TODO: sfugge riferimento a secretObjective
    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }
}
