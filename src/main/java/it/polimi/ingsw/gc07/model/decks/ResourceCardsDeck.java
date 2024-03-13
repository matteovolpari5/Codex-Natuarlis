package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.model.cards.*;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.enumerations.*;

public class ResourceCardsDeck extends DrawableDeck<NonStarterCard> {
    public ResourceCardsDeck() {
        super();
    }
    public GameResource revealBackDeckCard() throws CardNotPresentException {
        return revealDeckCard().getPermanentResource();
    }
}
