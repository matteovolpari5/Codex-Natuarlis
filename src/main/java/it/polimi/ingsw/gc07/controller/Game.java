package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
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
     * List of players and an integer for their order.
     */
    private final List<Player> players;
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
     * GameCommand for command pattern.
     */
    private GameCommand gameCommand;
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
     * @throws WrongNumberOfPlayersException exception thrown when the number of players is wrong
     */
    public Game(int id, int playersNumber, ResourceCardsDeck resourceCardsDeck,
                GoldCardsDeck goldCardsDeck, PlayingDeck<ObjectiveCard> objectiveCardsDeck,
                Deck<PlaceableCard> starterCardsDeck) throws WrongNumberOfPlayersException {
        this.id = id;
        this.state = GameState.WAITING_PLAYERS;
        if (playersNumber<2 || playersNumber>4)  {
            throw new WrongNumberOfPlayersException();
        }
        else{
            this.playersNumber = playersNumber;
        }
        this.players = new ArrayList<>();
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
        this.gameCommand = null;
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

    void setTwentyPointsReached(boolean twentyPointsReached)
    {
        this.twentyPointsReached=twentyPointsReached;
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

    CommandResultManager getCommandResultManager() {
        return commandResultManager;
    }

    public void setCommand(GameCommand gameCommand) {
        this.gameCommand = gameCommand;
    }

    public void execute() {
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


    // -----------------------------------
    // public methods - that will be removed (command pattern)
    // TODO transform in a concrete command
    // -----------------------------------

    /**
     * method that allows a player to draw one card from a GoldCardDeck or a ResourceCardDeck.
     * @param nickname: nickname of a player.
     * @param type: type of the card a user wants to draw.
     * @throws WrongCardTypeException: if the Type given is not correct
     * @throws CardNotPresentException: if the List of faceUpCards doesn't have a card in the given position
     * @throws WrongPlayerException: if the player is not the current player
     */
    public void drawDeckCard(String nickname, CardType type) throws WrongCardTypeException, CardNotPresentException, WrongPlayerException {
        if(!players.get(currPlayer).getNickname().equals(nickname)){
            throw new WrongPlayerException();
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            throw new WrongCardTypeException();
        }
        players.get(currPlayer).addCardHand(resourceCardsDeck.drawCard());
        //List<DrawableCard> newHand = new ArrayList<>();
        //newHand.addAll(this.players.get(this.currPlayer).getCurrentHand());
        if(type.equals(CardType.RESOURCE_CARD)){
            players.get(currPlayer).addCardHand(resourceCardsDeck.drawCard());
            //newHand.add(this.resourceCardsDeck.drawCard());
        }
        if(type.equals(CardType.GOLD_CARD)){

            players.get(currPlayer).addCardHand(goldCardsDeck.drawCard());
            //newHand.add(this.goldCardsDeck.drawCard());
        }
        //this.players.get(this.currPlayer).setCurrentHand(newHand);
        try {
            changeCurrPlayer();
        }
        catch (WrongStateException | PlayerNotPresentException e)
        {
            e.printStackTrace();
            // TODO noooo
        }
    }

    /**
     * method that allows a player to draw one of two faceUp cards of a given type.
     * @param nickname: nickname of a player.
     * @param type: type of the card a user wants to draw.
     * @param pos: position in the List of faceUpCards
     * @throws WrongCardTypeException: if the Type given is not correct
     * @throws CardNotPresentException: if the List of faceUpCards doesn't have a card in the given position
     * @throws WrongPlayerException: if the player is not the current player
     */
    public void drawFaceUpCard(String nickname, CardType type, int pos) throws WrongCardTypeException, CardNotPresentException, WrongPlayerException, PlayerNotPresentException {
        if(!players.get(currPlayer).getNickname().equals(nickname)){
            throw new WrongPlayerException();
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            throw new WrongCardTypeException();
        }
        //List<DrawableCard> newHand = new ArrayList<>();
        //newHand.addAll(this.players.get(this.currPlayer).getCurrentHand());
        if(type.equals(CardType.RESOURCE_CARD)){
            players.get(currPlayer).addCardHand(resourceCardsDeck.drawFaceUpCard(pos));
            //newHand.add(this.resourceCardsDeck.drawFaceUpCard(pos));
        }
        if(type.equals(CardType.GOLD_CARD)){
            players.get(currPlayer).addCardHand(goldCardsDeck.drawFaceUpCard(pos));
            //newHand.add(this.goldCardsDeck.drawFaceUpCard(pos));
        }
        //this.players.get(this.currPlayer).setCurrentHand(newHand);
        try {
            changeCurrPlayer();
        }
        catch (WrongStateException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * method that reveal the face up card in position pos
     * @param type type of the card
     * @param pos position of the card
     * @return the card that we want to reveal
     * @throws WrongCardTypeException if the card is a starter card
     * @throws CardNotPresentException if there aren't face up cards
     */
    public Card revealFaceUpCard(CardType type, int pos) throws WrongCardTypeException, CardNotPresentException {
        if(type.equals(CardType.STARTER_CARD))
        {
            throw new WrongCardTypeException();
        }
        if(type.equals(CardType.GOLD_CARD))
            return this.goldCardsDeck.revealFaceUpCard(pos);
        else if(type.equals(CardType.RESOURCE_CARD))
            return this.resourceCardsDeck.revealFaceUpCard(pos);
        else
            return this.objectiveCardsDeck.revealFaceUpCard(pos);
    }

    /**
     * method that reveal the back of the card on top of the deck
     * @param type : type of the card
     * @return the GameResource that represent the back of the card
     * @throws WrongCardTypeException   if we reveal the back of a starter card or the back of an objective card
     * @throws CardNotPresentException if the deck is empty
     */
    public GameResource revealBackDeckCard(CardType type) throws WrongCardTypeException, CardNotPresentException {
        if(type.equals(CardType.STARTER_CARD) || type.equals(CardType.OBJECTIVE_CARD))
        {
            throw new WrongCardTypeException();
        }
        if(type.equals(CardType.GOLD_CARD))
            return this.goldCardsDeck.revealBackDeckCard();
        else
            return this.resourceCardsDeck.revealBackDeckCard();
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


    // -----------------------------------
    // private methods - will be moved in a concrete command
    // TODO move in a concrete command
    // -----------------------------------

    /**
     * method that change the current player, if it's the last turn and all the players
     * played the same amount of turn it computes the winner
     * if a player is disconnect from the game he loose the turn
     * if a player is stalled he will be skipped
     * @throws WrongStateException if the state of the game is wrong
     */
     void changeCurrPlayer () throws WrongStateException, CardNotPresentException, PlayerNotPresentException {
        if(!state.equals(GameState.PLAYING)) {
            throw new WrongStateException();
        }
        if(this.currPlayer==this.players.size()-1)
            this.currPlayer=0;
        else
            this.currPlayer++;
        if(this.twentyPointsReached)
        {
            if(players.get(currPlayer).isFirst()&&this.additionalRound)
            {
                this.state = GameState.GAME_ENDED;
                List<Player> winners = new ArrayList<>(computeWinner());
                //TODO: fare qualcosa con questo winner
            }
            else if(players.get(currPlayer).isFirst())
            {
                this.additionalRound=true;
            }
        }
        if(!players.get(currPlayer).isConnected())
        {
            changeCurrPlayer();
        }
        if(players.get(currPlayer).getIsStalled())
        {
            changeCurrPlayer();
        }
    }

    /**
     * method that compute the winner/s of the game.
     * @return the list of players who won the game.
     * @throws CardNotPresentException : if there isn't an objective faceUpCard on the board.
     * @throws PlayerNotPresentException : if the player is not present in the List players.
     */
    // TODO RIVEDERE
    private List<Player> computeWinner() throws CardNotPresentException, PlayerNotPresentException, WrongStateException {
        if (state.equals(GameState.GAME_ENDED)){
            throw new WrongStateException();
        }
        List<Player> winners = new ArrayList<>();
        int deltapoints;
        int max = 0;
        int realizedObjectives;
        int maxRealizedObjective = 0;
        for (int i=0; i>=0 && i< players.size(); i++){
            realizedObjectives = objectiveCardsDeck.revealFaceUpCard(0).numTimesScoringConditionMet(playersGameField.get(players.get(i).getNickname()));
            //points counter for the 1st common objective
            deltapoints = objectiveCardsDeck.revealFaceUpCard(0).getObjectiveScore(playersGameField.get(players.get(i).getNickname()));
            realizedObjectives += objectiveCardsDeck.revealFaceUpCard(1).numTimesScoringConditionMet(playersGameField.get(players.get(i).getNickname()));
            //points counter for the 2nd common objective
            deltapoints += objectiveCardsDeck.revealFaceUpCard(1).getObjectiveScore(playersGameField.get(players.get(i).getNickname()));
            realizedObjectives += players.get(i).getSecretObjective().numTimesScoringConditionMet(playersGameField.get(players.get(i).getNickname()));
            //points counter for the secret objective
            deltapoints += players.get(i).getSecretObjective().getObjectiveScore(playersGameField.get(players.get(i).getNickname()));
            scoreTrackBoard.incrementScore(players.get(i).getNickname(), deltapoints);
            List<Player> playersCopy = new ArrayList<>(players);
            if (max <= scoreTrackBoard.getScore(playersCopy.get(i).getNickname())){
                max = scoreTrackBoard.getScore(playersCopy.get(i).getNickname());
                if (realizedObjectives >= maxRealizedObjective){
                    if (realizedObjectives == maxRealizedObjective){
                        winners.add(playersCopy.get(i));
                    }
                    else{
                        winners.clear();
                        winners.add(playersCopy.get(i));
                        maxRealizedObjective = realizedObjectives;
                    }
                }
            }
        }
        return winners;
    }
}