package it.polimi.ingsw.gc07.controller;

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
import it.polimi.ingsw.gc07.model.enumerations.GameState;
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
        boolean foundId = false;
        boolean foundGame;
        int id = 0;

        if(playersNumber < 2 || playersNumber > 4){
            //TODO
            // bandierina
            // spostare il controllo nel model per alzare una bandierina
            throw new WrongNumberOfPlayersException();
        }

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

        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardDeck.shuffle();
        Deck<PlaceableCard> starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        starterCardsDeck.shuffle();
        Game game = null;
        try {
            game = new Game(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardDeck, starterCardsDeck);
            games.add(game);
        } catch (WrongNumberOfPlayersException e) {
            // TODO ????
        }

        return id;
    }

    /**
     * Accepts new player's data and creates a Player object if the nickname is unique.
     * Adds the player to a list until he will choose a game.
     */
    public void addPlayer(String nickname, TokenColor tokenColor, boolean connectionType, boolean interfaceType) throws PlayerAlreadyPresentException {
        if(checkNicknameUnique(nickname)){
            Player newPlayer = new Player(nickname, tokenColor, connectionType, interfaceType);
            pendingPlayers.add(newPlayer);
        }
        else{
            throw new PlayerAlreadyPresentException();
        }
    }

    /**
     * Method to check if a nickname is unique or another player has the same nickname.
     * @param nickname nickname to check
     * @return true if no other player has the same nickname
     */
    private boolean checkNicknameUnique(String nickname) {
        boolean unique = true;
        for(Game g: games){
            if(g.hasPlayer(nickname)){
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

    public void joinExistingGame(String nickname, int gameId) throws WrongStateException, PlayerNotPresentException {
        Player player = getPendingPlayer(nickname);
        if(player == null){
            throw new PlayerNotPresentException();
        }
        for(Game game: games) {
            if(game.getId() == gameId) {
                game.addPlayer(player);
            }
        }
    }

    public void joinNewGame(String nickname, int playersNumber) throws PlayerNotPresentException, WrongNumberOfPlayersException, WrongStateException {
        Player player = getPendingPlayer(nickname);
        if(player == null){
            throw new PlayerNotPresentException();
        }
        int gameId = createGame(playersNumber);
        for(Game game: games) {
            if(game.getId() == gameId) {
                game.addPlayer(player);
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
