package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddPlayerCommandTest {
    private GameController gameController;
    private Player newPlayer;

    @BeforeEach
    void setUp() {
        // create a gameController
        int id = 0;
        int playersNumber = 2;
        DrawableDeck<DrawableCard> resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        DrawableDeck<GoldCard> goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardsDeck.shuffle();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();
        starterCardsDecks.shuffle();
        gameController = new GameController(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);

        // create a new player
        String nickname = "New player";
        boolean connectionType = true;
        boolean interfaceType = true;
        newPlayer = new Player(nickname, connectionType, interfaceType);
    }

    @Test
    void addPlayerSuccess() {
        gameController.addPlayer(newPlayer);
    }

    @Test
    void addPlayerWrongState() {
        Player firstPlayer = new Player("Player1", true, false);
        gameController.addPlayer(firstPlayer);
        Player secondPlayer = new Player("Player2", false, false);
        gameController.addPlayer(secondPlayer);
    }
}