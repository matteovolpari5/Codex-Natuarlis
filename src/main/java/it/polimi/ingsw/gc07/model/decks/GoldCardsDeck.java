package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.model.cards.*;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.enumerations.*;

import java.util.Stack;

public class GoldCardsDeck extends DrawableDeck<GoldCard> {
    public GoldCardsDeck(CardType type, Stack<GoldCard> content) {
        super(type, content);
    }
    public GoldCardsDeck(GoldCardsDeck existingDeck){
        super(existingDeck);
    }
    public GameResource revealBackDeckCard() throws CardNotPresentException {
        return revealDeckCard().getPermanentResources().getFirst();
    }
}