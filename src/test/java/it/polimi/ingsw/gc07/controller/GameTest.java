package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void checkWrongPlayersNumber() {
        int id = 0;
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardsDeck.shuffle();
        Deck<PlaceableCard> starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        starterCardsDeck.shuffle();
        assertThrows(WrongNumberOfPlayersException.class,
                () -> new Game(id, 0, resourceCardsDeck,
                goldCardsDeck, objectiveCardsDeck, starterCardsDeck));
        assertThrows(WrongNumberOfPlayersException.class,
                () -> new Game(id, 1, resourceCardsDeck,
                        goldCardsDeck, objectiveCardsDeck, starterCardsDeck));
        assertDoesNotThrow(() -> new Game(id, 2, resourceCardsDeck,
                goldCardsDeck, objectiveCardsDeck, starterCardsDeck));
        assertDoesNotThrow(() -> new Game(id, 4, resourceCardsDeck,
                goldCardsDeck, objectiveCardsDeck, starterCardsDeck));
        assertThrows(WrongNumberOfPlayersException.class,
                () -> new Game(id, 5, resourceCardsDeck,
                        goldCardsDeck, objectiveCardsDeck, starterCardsDeck));
    }
    @Test
    void computeWinnerOneWinner() {

    }

    @Test
    void computeWinnerDraw() {

    }

    @Test
    void changeCurrPlayer() {

    }
}