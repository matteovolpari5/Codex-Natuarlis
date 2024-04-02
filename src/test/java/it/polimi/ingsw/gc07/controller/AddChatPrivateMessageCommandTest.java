package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddChatPrivateMessageCommandTest {
    Game game;

    @BeforeEach
    void setUp() {
        // create a game
        int id = 0;
        int playersNumber = 3;
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardsDeck.shuffle();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();
        starterCardsDecks.shuffle();
        try{
            game = new Game(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);
        }catch(WrongNumberOfPlayersException e){
            throw new RuntimeException();
        }
        // add first player
        Player firstPlayer = new Player("Player1", TokenColor.BLUE, true, false);
        game.setCommand(new AddPlayerCommand(game, firstPlayer));
        AddPlayerResult result = (AddPlayerResult) game.execute();
        if(!result.equals(AddPlayerResult.SUCCESS))
            throw new RuntimeException();
        // add second player
        Player secondPlayer = new Player("Player2", TokenColor.GREEN, false, false);
        game.setCommand(new AddPlayerCommand(game, secondPlayer));
        result = (AddPlayerResult) game.execute();
        if(!result.equals(AddPlayerResult.SUCCESS))
            throw new RuntimeException();
        // add third player
        Player thirdPlayer = new Player("Player3", TokenColor.YELLOW, false, false);
        game.setCommand(new AddPlayerCommand(game, thirdPlayer));
        result = (AddPlayerResult) game.execute();
        if(!result.equals(AddPlayerResult.SUCCESS))
            throw new RuntimeException();
    }

    @Test
    void addMessageSuccess() {
        game.setCommand(new AddChatPrivateMessageCommand(game, "My content...", "Player1", "Player3"));
        ChatCommandResult result = (ChatCommandResult) game.execute();
        assertEquals(ChatCommandResult.SUCCESS, result);
    }

    @Test
    void addMessageWrongSender() {
        game.setCommand(new AddChatPrivateMessageCommand(game, "My content...", "WrongSender", "Player1"));
        ChatCommandResult result = (ChatCommandResult) game.execute();
        assertEquals(ChatCommandResult.WRONG_SENDER, result);
    }

    @Test
    void addMessageWrongReceiver() {
        game.setCommand(new AddChatPrivateMessageCommand(game, "My content...", "Player3", "WrongReceiver"));
        ChatCommandResult result = (ChatCommandResult) game.execute();
        assertEquals(ChatCommandResult.WRONG_RECEIVER, result);
    }
}