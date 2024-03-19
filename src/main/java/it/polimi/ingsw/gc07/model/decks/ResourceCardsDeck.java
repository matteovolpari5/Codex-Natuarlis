package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.model.cards.*;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.enumerations.*;

import java.util.Stack;

public class ResourceCardsDeck extends DrawableDeck<NonStarterCard> {
    public ResourceCardsDeck(CardType type, Stack<NonStarterCard> content) {
        super(type, content);
    }
    public ResourceCardsDeck(ResourceCardsDeck existingDeck){
        super(existingDeck);
    }
    public GameResource revealBackDeckCard() throws CardNotPresentException {
        return revealDeckCard().getPermanentResources().getFirst();
    }
}
