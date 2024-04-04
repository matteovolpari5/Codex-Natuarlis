package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.exceptions.PlayerAlreadyPresentException;
import it.polimi.ingsw.gc07.exceptions.PlayerNotPresentException;
import it.polimi.ingsw.gc07.exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.gc07.exceptions.WrongStateException;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.ArrayList;
import java.util.List;

public class GamesManager {
    /**
     * List of games.
     */
    private final List<Game> games;

    /**
     * List of players who have not chosen a game.
     */
    private final List<Player> pendingPlayers;

    /**
     * GamesManger is created once the server is started.
     */
    public GamesManager() {
        games = new ArrayList<>();
        pendingPlayers = new ArrayList<>();
    }

    /**
     * Method that creates a new Game and adds it to the list games.
     * @param playersNumber number of player of the new game, decided by the first player to join.
     */
    private int createGame(int playersNumber) throws WrongNumberOfPlayersException {

        // check players number
        if(playersNumber < 2 || playersNumber > 4){
            //TODO rientra nei controlli per cui non possono ancora usare la bandierina,
            // ma devo notificare il player
            throw new WrongNumberOfPlayersException();
        }

        // find first free id
        int id = findFirstFreeId();

        // create and shuffle decks
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardDeck.shuffle();
        Deck<PlaceableCard> starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        starterCardsDeck.shuffle();

        // create game
        Game game = null;
        game = new Game(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardDeck, starterCardsDeck);
        games.add(game);

        return id;
    }

    /**
     * Method that finds the first free id.
     * @return first free id
     */
    private int findFirstFreeId() {
        boolean foundId = false;
        boolean foundGame;
        int id = 0;
        while(!foundId){
            foundGame = false;
            for(Game g: games){
                if(g.getId() == id){
                    foundGame = true;
                }
            }
            if(!foundGame){
                foundId = true;
            }
            else{
                id++;
            }
        }
        return id;
    }

    /**
     * Accepts new player's data and creates a Player object if the nickname is unique.
     * Adds the player to a list until he will choose a game.
     * @param nickname nickname
     * @param connectionType connection type
     * @param interfaceType interface type
     * @throws PlayerAlreadyPresentException
     */
    public void addPlayer(String nickname, boolean connectionType, boolean interfaceType) throws PlayerAlreadyPresentException {
        if(checkNicknameUnique(nickname)){
            Player newPlayer = new Player(nickname, connectionType, interfaceType);
            pendingPlayers.add(newPlayer);
        }
        else {
            throw new PlayerAlreadyPresentException();
            //TODO nooooo non posso lanciare eccezione da controller a view
            // notificare nickname duplicato
        }
    }

    /**
     * Method to check if a nickname is unique or another player has the same nickname.
     * @param nickname nickname to check
     * @return true if no other player has the same nickname
     */
    private boolean checkNicknameUnique(String nickname) {
        boolean unique = true;
        for(Game g: games) {
            if(g.hasPlayer(nickname)){
                unique = false;
            }
        }
        for(Player p: pendingPlayers) {
            if(p.getNickname().equals(nickname)) {
                unique = false;
            }
        }
        return unique;
    }

    public void displayExistingGames() {
        // TODO
        // probabilmente non void
    }

    private Player getPendingPlayer(String nickname) {
        Player player = null;
        for(Player p: pendingPlayers) {
            if(p.getNickname().equals(nickname)){
                player = p;
            }
        }
        return player;
    }

    public void joinExistingGame(String nickname, TokenColor tokenColor, int gameId) throws PlayerNotPresentException, WrongStateException {
        Player player = getPendingPlayer(nickname);
        if(player == null){
            throw new PlayerNotPresentException();
            //TODO: no throws !!!
            // situazione tipo: il giocatore è già entrato in un gioco,
            // poi lancia di nuovo il comando per entrare in un gioco
            // cosa fare?
        }
        for(Game game: games) {
            if(game.getId() == gameId) {
                // check game state WAITING_PLAYERS
                if(!game.getState().equals(GameState.WAITING_PLAYERS)) {
                    throw new WrongStateException();
                    //TODO: no throws !!!
                    // devo notificare, ma non posso mandare eccezione
                }
                // check token color unique
                if(!checkTokenColorUnique(game, tokenColor)) {
                    throw new RuntimeException();   // non abbiamo TokenColorException, ma tanto va tolta l'eccezione
                    //TODO: no throws !!!
                    // devo notificare, ma non posso mandare eccezione
                }
                player.setTokenColor(tokenColor);
                game.setCommand(new AddPlayerCommand(game, player));
                game.execute();
            }
        }
    }

    /**
     * Method to check if a given token color is unique in a game.
     * @param game game
     * @param tokenColor token color
     * @return true if the token color is unique in the game
     */
    private boolean checkTokenColorUnique(Game game, TokenColor tokenColor) {
        boolean unique = true;
        for(Player p: game.getPlayers()) {
            if(p.getTokenColor().equals(tokenColor))
                unique = false;
        }
        return unique;
    }

    public void joinNewGame(String nickname, TokenColor tokenColor, int playersNumber) throws PlayerNotPresentException, WrongNumberOfPlayersException {
        Player player = getPendingPlayer(nickname);
        if(player == null){
            throw new PlayerNotPresentException();
            //TODO: no throws !!!
            // situazione tipo: il giocatore è già entrato in un gioco,
            // poi lancia di nuovo il comando per entrare in un gioco
            // cosa fare?
        }
        int gameId = createGame(playersNumber);
        for(Game game: games) {
            if(game.getId() == gameId) {
                // no need to check the token color for the first player of the game
                player.setTokenColor(tokenColor);
                game.setCommand(new AddPlayerCommand(game, player));
                game.execute();
            }
        }
    }

    // TODO: chi lo chiama?
    public void deleteGame(int id) {
        Game game = null;
        for(Game g: games) {
            if(g.getId() == id) {
                game = g;
            }
        }
        if(game != null && game.getState().equals(GameState.GAME_ENDED)){
            games.remove(game);
        }
    }
}
