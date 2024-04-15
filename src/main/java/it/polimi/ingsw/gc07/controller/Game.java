package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.CommandResultManager;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.ScoreTrackBoard;
import it.polimi.ingsw.gc07.model.cards.*;
import it.polimi.ingsw.gc07.model.chat.Chat;
import it.polimi.ingsw.gc07.model.chat.Message;
import it.polimi.ingsw.gc07.model.decks.*;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;

import java.util.*;

public class Game {
    /**
     * ID of the game.
     */
    private final int id;
    /**
     * State of the game.
     */
    private GameState state;
    /**
     * Number of players in the game, chose by the first player.
     */
    private final int playersNumber;
    /**
     * Map of players and their game field.
     */
    private final Map<String, GameField> playersGameField;
    /**
     * List of players.
     */
    private final List<Player> players;
    /**
     * List of winner/s of the game.
     */
    private final List<Player> winners;
    /**
     * Integer value representing the position of the current player.
     */
    private int currPlayer;
    /**
     * Score track board of the game.
     */
    private final ScoreTrackBoard scoreTrackBoard;
    /**
     * Deck of resource cards.
     */
    private final ResourceCardsDeck resourceCardsDeck;
    /**
     * Deck of gold cards.
     */
    private final GoldCardsDeck goldCardsDeck;
    /**
     * Deck of objective cards.
     */
    private final PlayingDeck<ObjectiveCard> objectiveCardsDeck;
    /**
     * Deck of starter cards.
     */
    private final Deck<PlaceableCard> starterCardsDeck;
    /**
     * Boolean attribute, true if a player has reached 20 points.
     */
    private boolean twentyPointsReached;
    /**
     * Boolean attribute, if it is the additional round of the game.
     */
    private boolean additionalRound;
    /**
     * Chat of the game.
     */
    private final Chat chat;
    /**
     * Command result manager of the game.
     */
    private final CommandResultManager commandResultManager;

    /** Constructor of a Game with only the first player.
     *
     * @param playersNumber number of players
     * @param resourceCardsDeck deck of resource cards
     * @param goldCardsDeck deck of gold cards
     * @param objectiveCardsDeck deck of objective cards
     * @param starterCardsDeck deck of starter cards
     */
    public Game(int id, int playersNumber, ResourceCardsDeck resourceCardsDeck,
                GoldCardsDeck goldCardsDeck, PlayingDeck<ObjectiveCard> objectiveCardsDeck,
                Deck<PlaceableCard> starterCardsDeck) {
        this.id = id;
        this.state = GameState.GAME_STARTING;
        assert(playersNumber>=2 && playersNumber<=4): "Wrong players number";
        this.playersNumber = playersNumber;
        this.players = new ArrayList<>();
        this.winners = new ArrayList<>();
        this.playersGameField = new HashMap<>();
        this.scoreTrackBoard = new ScoreTrackBoard();
        this.resourceCardsDeck = new ResourceCardsDeck(resourceCardsDeck);
        this.goldCardsDeck = new GoldCardsDeck(goldCardsDeck);
        this.objectiveCardsDeck = new PlayingDeck<>(objectiveCardsDeck);
        this.starterCardsDeck = new Deck<>(starterCardsDeck);
        this.currPlayer = 0;
        this.twentyPointsReached = false;
        this.additionalRound = false;
        this.chat = new Chat();
        this.commandResultManager = new CommandResultManager();
    }

    // ------------------------------
    // setters and getters
    // ------------------------------

    /**
     * Getter for the game id.
     * @return game id
     */
    int getId() {
        return id;
    }

    /**
     * Setter for the state of the game.
     */
    void setState(GameState state) {
        this.state = state;
    }

    /**
     * Getter for the state of the game.
     * @return the state of the game
     */
    GameState getState() {
        return state;
    }

    /**
     * Method telling if a player is in a game.
     * @param nickname nickname of the player
     * @return true if the player is in the game
     */
    boolean hasPlayer(String nickname) {
        boolean found = false;
        for(Player p: players){
            if(p.getNickname().equals(nickname)){
                found = true;
            }
        }
        return found;
    }

    int getPlayersNumber() {
        return playersNumber;
    }

    Map<String, GameField> getPlayersGameField() {
        return playersGameField;
    }

    List<Player> getPlayers() {
        return players;
    }
    List<Player> getWinners(){return winners;}
    void setCurrPlayer(int currPlayer) {
        this.currPlayer = currPlayer;
    }

    int getCurrPlayer() {
        return currPlayer;
    }

    ScoreTrackBoard getScoreTrackBoard() {
        return scoreTrackBoard;
    }

    ResourceCardsDeck getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    GoldCardsDeck getGoldCardsDeck() {
        return goldCardsDeck;
    }

    PlayingDeck<ObjectiveCard> getObjectiveCardsDeck() {
        return objectiveCardsDeck;
    }

    Deck<PlaceableCard> getStarterCardsDeck() {
        return starterCardsDeck;
    }

    void setTwentyPointsReached() {
        this.twentyPointsReached = true;
    }

    boolean getTwentyPointsReached() {
        return twentyPointsReached;
    }

    boolean getAdditionalRound() {
        return additionalRound;
    }

    //method for testing PlaceCardEasily
    void setCurrentPlayer(int num)
    {
        this.currPlayer=num;
    }

    Chat getChat() {
        return chat;
    }

    public CommandResultManager getCommandResultManager() {
        return commandResultManager;
    }

    public void setAndExecuteCommand(GameCommand gameCommand) {
        gameCommand.execute();
    }

    /**
     * Method that returns the position of the player in the List players.
     * @param nickname: nickname of the player whose position is being searched
     * @return position of the player in the List players
     * @throws PlayerNotPresentException: thrown if the nickname is not present in the list players.
     */
    int getPlayerByNickname(String nickname) throws PlayerNotPresentException {
        for (int i = 0; i < playersNumber; i++){
            if(players.get(i).getNickname().equals(nickname)){
                return i;
            }
        }
        throw new PlayerNotPresentException();
    }

    /**
     * Method that change the current player, if it's the last turn and all the players
     * played the same amount of turn it computes the winner;
     * if a player is disconnect from the game he loose the turn,
     * if a player is stalled he will be skipped.
     */
    void changeCurrPlayer () {
        assert(state.equals(GameState.PLAYING)): "Method changeCurrentPlayer called in a wrong state";
        if(currPlayer == players.size()-1)
            currPlayer = 0;
        else
            currPlayer++;
        if(twentyPointsReached) {
            if(players.get(currPlayer).isFirst() && additionalRound) {
                state = GameState.GAME_ENDED;
                winners.addAll(computeWinner());
                // the game is ended

                //TODO faccio partire un timer di qualche minuto
                // per permettere ai giocatori di scrivere in chat

                // when the timer is ended
                //GamesManager.getGamesManager().deleteGame(this.id);
                //return;
            }
            else if(players.get(currPlayer).isFirst()) {
                additionalRound=true;
            }
        }
        if(!players.get(currPlayer).isConnected()) {
            changeCurrPlayer();
        }
        if(players.get(currPlayer).getIsStalled()) {
            boolean found = false;
            for(Player p: players) {
                if(!p.getIsStalled())
                    found = true;
            }
            if(found)
                changeCurrPlayer();
            else {
                this.state = GameState.GAME_ENDED;
                winners.addAll(computeWinner());
            }
        }
    }

    /**
     * Method that compute the winner/s of the game.
     * @return the list of players who won the game
     */
    private List<Player> computeWinner() {
        assert(state.equals(GameState.GAME_ENDED)) : "The game state is not correct";
        List<Player> winners = new ArrayList<>();
        int deltaPoints;
        int max = 0;
        int realizedObjectives;
        int maxRealizedObjective = 0;
        List<Player> playersCopy = new ArrayList<>(players);
        for (int i=0; i>=0 && i< players.size(); i++){
            ObjectiveCard objectiveCard = null;
            objectiveCard = objectiveCardsDeck.revealFaceUpCard(0);
            assert(objectiveCard != null): "The common objective must be present";
            realizedObjectives = objectiveCard.numTimesScoringConditionMet(playersGameField.get(players.get(i).getNickname()));
            //points counter for the 1st common objective
            deltaPoints = objectiveCard.getObjectiveScore(playersGameField.get(players.get(i).getNickname()));

            objectiveCard = objectiveCardsDeck.revealFaceUpCard(1);
            assert(objectiveCard != null): "The common objective must be present";
            realizedObjectives += objectiveCard.numTimesScoringConditionMet(playersGameField.get(players.get(i).getNickname()));
            //points counter for the 2nd common objective
            deltaPoints += objectiveCard.getObjectiveScore(playersGameField.get(players.get(i).getNickname()));

            realizedObjectives += players.get(i).getSecretObjective().numTimesScoringConditionMet(playersGameField.get(players.get(i).getNickname()));
            //points counter for the secret objective
            deltaPoints += players.get(i).getSecretObjective().getObjectiveScore(playersGameField.get(players.get(i).getNickname()));
            scoreTrackBoard.incrementScore(players.get(i).getNickname(), deltaPoints);
            if (max <= scoreTrackBoard.getScore(playersCopy.get(i).getNickname())) {
                max = scoreTrackBoard.getScore(playersCopy.get(i).getNickname());
                if (realizedObjectives >= maxRealizedObjective) {
                    if (realizedObjectives == maxRealizedObjective) {
                        winners.add(playersCopy.get(i));
                    } else {
                        winners.clear();
                        winners.add(playersCopy.get(i));
                        maxRealizedObjective = realizedObjectives;
                    }
                }
            }
        }
        return winners;
    }





    // TODO discutere


    // ----------------------------
    // Metodi che probabilmente non vengono invocati direttamente dal client,
    // ma servono per aggiornare la view
    // ----------------------------

    /**
     * method that reveal the face up card in position pos
     * @param type type of the card
     * @param pos position of the card
     * @return the card that we want to reveal
     * @throws WrongCardTypeException if the card is a starter card
     */
    public Card revealFaceUpCard(CardType type, int pos) throws WrongCardTypeException {       // TODO rimuovere wrongcardtype exception
        if(type.equals(CardType.STARTER_CARD)) {
            throw new WrongCardTypeException();
        }
        Card faceUpCard;
        if(type.equals(CardType.GOLD_CARD))
            faceUpCard = this.goldCardsDeck.revealFaceUpCard(pos);
        else if(type.equals(CardType.RESOURCE_CARD))
            faceUpCard = this.resourceCardsDeck.revealFaceUpCard(pos);
        else
            faceUpCard = this.objectiveCardsDeck.revealFaceUpCard(pos);
        if(faceUpCard != null) {
            return faceUpCard;
        }
        else {
            return null;
            // TODO
        }
    }

    /**
     * method that reveal the back of the card on top of the deck
     * @param type : type of the card
     * @return the GameResource that represent the back of the card
     * @throws WrongCardTypeException   if we reveal the back of a starter card or the back of an objective card
     */
    public GameResource revealBackDeckCard(CardType type) throws WrongCardTypeException {       // TODO togliere eccezione
        if(type.equals(CardType.STARTER_CARD) || type.equals(CardType.OBJECTIVE_CARD))
        {
            throw new WrongCardTypeException();
        }
        GameResource backResource;
        if(type.equals(CardType.GOLD_CARD)){
            backResource = this.goldCardsDeck.revealBackDeckCard();
        }else{
            backResource = this.resourceCardsDeck.revealBackDeckCard();
        }
        if(backResource != null)
            return backResource;
        else {
            return null;    // TODO cosa ritorno ?
        }
    }

    /**
     * Returns the last message of the chat for a certain player.
     * @return the last message of the chat
     */
    public Message getLastChatMessage(String receiver)  {
        Message message = chat.getLastMessage(receiver);
        if(message == null) {
            // TODO no return null!
            return null;
        }
        else
            return message;
    }

    /**
     * Returns the content of the chat for a certain player.
     * @return the list of the message in the chat
     */
    public List<Message> getChatContent(String receiver) {
        return chat.getContent(receiver);
    }
}