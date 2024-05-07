package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model_view_listeners.*;

import java.util.List;
import java.util.Map;

public class Tui implements ChatTui, DeckTui, GameFieldTui, PlayerTui, ScoreTrackBoardTui,
        ChatViewListener, DeckViewListener, GameFieldViewListener, PlayerViewListener, BoardViewListener {
    @Override
    public void receiveMessageUpdate(ChatMessage chatMessage) {
        ChatTui.printMessage(chatMessage);
    }

    @Override
    public void receiveGameFieldUpdate(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {
        GameFieldTui.printGameField(cardsContent, cardsFace, cardsOrder);
    }

    @Override
    public void receiveCardHandUpdate(List<DrawableCard> hand, ObjectiveCard personalObjective) {
        PlayerTui.printPlayerHand(hand, personalObjective);
    }

    @Override
    public void receiveScoreUpdate(Map<String, Integer> playerScores, Map<String, TokenColor> playerTokenColors) {
        ScoreTrackBoardTui.printScoreTrackBoard(playerScores, playerTokenColors);
    }

    @Override
    public void receiveDecksUpdate(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective) {
        DeckTui.printDeck(commonObjective, faceUpGoldCard, faceUpResourceCard, topGoldDeck, topResourceDeck);
    }
}