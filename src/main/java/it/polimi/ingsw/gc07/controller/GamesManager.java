package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.game_commands.GamesManagerCommand;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamesManager {
    /**
     * Instance of GamesManager.
     */
    private static volatile GamesManager myGamesManager = null;
    /**
     * List of game controllers.
     */
    private final List<GameController> gameControllers;
    /**
     * List of players who have not chosen a game.
     */
    private final List<Player> pendingPlayers;
    /**
     * Command result manager for games manager.
     */
    private CommandResult commandResult;

    /**
     * GamesManger is created once the server is started.
     * GamesManager implements Singleton pattern.
     */
    private GamesManager() {
        gameControllers = new ArrayList<>();
        pendingPlayers = new ArrayList<>();
        commandResult = null;
    }

    /**
     * Method to get the only available instance of GamesManager (Singleton pattern).
     * @return instance of games manager
     */
    public static synchronized GamesManager getGamesManager() {
        if(myGamesManager == null) {
            myGamesManager = new GamesManager();
        }
        return myGamesManager;
    }

    /**
     * Only for test purposes, return a new instance of GamesManager, not using Singleton.
     * @return new games manager
     */
    static GamesManager getNewGamesManager() {
        return new GamesManager();
    }

    /**
     * Friendly getter method for attribute gameControllers, used for Command pattern.
     * @return gameControllers
     */
    List<GameController> getGames() {
        return gameControllers;
    }

    public Map<Integer, Integer> getFreeGamesDetails() {
        Map<Integer, Integer> gameDetails = new HashMap<>();
        for(GameController g: gameControllers) {
            if(g.getState().equals(GameState.GAME_STARTING)) {
                gameDetails.put(g.getId(), g.getPlayersNumber());
            }
        }
        return gameDetails;
    }

    /**
     * Return the game with the provided id.
     * @return game with given id
     */
    public GameController getGameById(int id) {
        for(GameController g: gameControllers) {
            if(g.getId() == id) {
                return g;
            }
        }
        return null;
    }

    /**
     * Getter for the command result manager.
     * @return command result manager of the games manager
     */
    public CommandResult getCommandResult() {
        return commandResult;
    }

    /**
     * Setter for gameControllers manager command (command pattern).
     * @param gamesManagerCommand games manager command
     */
    public synchronized void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) {
        gamesManagerCommand.execute(this);
    }

    // used by more game commands
    /**
     * Method that returns the object corresponding to the pending player with a certain name.
     * @param nickname nickname of the pending player to search.
     * @return pending player with the given nicknames
     */
    Player getPendingPlayer(String nickname) {
        Player player = null;
        for(Player p: pendingPlayers) {
            if(p.getNickname().equals(nickname)){
                player = p;
            }
        }
        return player;
    }

    /**
     * Finds the game id of the game in which is playing the player with provided nickname.
     * @param nickname nickname of the player
     * @return game id
     */
    public int getGameIdWithPlayer(String nickname) {
        for(GameController g: gameControllers) {
            if(g.hasPlayer(nickname)) {
                return g.getId();
            }
        }
        return -1;
    }

     public void addPlayerToPending(String nickname, boolean connectionType, boolean interfaceType) {
         // this command can always be used
         if(checkReconnection(nickname)) {
             for(GameController gameController: gameControllers) {
                 for(Player p: gameController.getPlayers()) {
                     if(p.getNickname().equals(nickname)) {
                         assert(!p.isConnected());
                         gameController.reconnectPlayer(nickname);
                     }
                 }
             }
         }else if(checkNicknameUnique(nickname)){
            Player newPlayer = new Player(nickname, connectionType, interfaceType);
            pendingPlayers.add(newPlayer);
         }else {
             commandResult = CommandResult.PLAYER_ALREADY_PRESENT;
             return;
         }
         commandResult = CommandResult.SUCCESS;
    }

    private boolean checkReconnection(String nickname) {
        boolean reconnection = false;
        for(GameController gameController: gameControllers) {
            // if a game has a player with the same nickname and disconnected
            for(Player p: gameController.getPlayers()) {
                if(p.getNickname().equals(nickname) && !p.isConnected()) {
                    reconnection = true;
                }
            }
        }
        return reconnection;
    }

    /**
     * Method to check if a nickname is unique or another player has the same nickname.
     * @param nickname nickname to check
     * @return true if no other player has the same nickname
     */
    private boolean checkNicknameUnique(String nickname) {
        boolean unique = true;
        for(GameController g: gameControllers) {
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

    public void joinExistingGame(String nickname, TokenColor tokenColor, int gameId) {
        // this command can always be used
        Player player = getPendingPlayer(nickname);
        if(player == null){
            commandResult = CommandResult.PLAYER_NOT_PRESENT;
            return;
        }
        boolean found = false;
        for(GameController gameController : gameControllers) {
            if(gameController.getId() == gameId) {
                found = true;
                // check gameController state WAITING_PLAYERS
                if(!gameController.getState().equals(GameState.GAME_STARTING)) {
                    commandResult = CommandResult.GAME_FULL;
                    return;
                }
                // check token color unique
                if(gameController.hasPlayerWithTokenColor(tokenColor)) {
                    commandResult = CommandResult.TOKEN_COLOR_ALREADY_TAKEN;
                    return;
                }
                player.setTokenColor(tokenColor);
                gameController.addPlayer(player);
                pendingPlayers.remove(player);
            }
        }
        if(!found){
            commandResult = CommandResult.GAME_NOT_PRESENT;
            return;
        }
        // join successful, but it is necessary to set the game for the client
        commandResult = CommandResult.SET_SERVER_GAME;
    }

     public void joinNewGame(String nickname, TokenColor tokenColor, int playersNumber) {
        // this command can always be used
        Player player = getPendingPlayer(nickname);
        if(player == null) {
            commandResult = CommandResult.PLAYER_NOT_PRESENT;
            return;
        }
        int gameId;
        try{
            gameId = createGame(playersNumber);
        }
        catch(WrongNumberOfPlayersException e){
            commandResult = CommandResult.WRONG_PLAYERS_NUMBER;
            return;
        }
        for(GameController gameController : gameControllers) {
            if(gameController.getId() == gameId) {
                // no need to check the token color for the first player of the gameController
                player.setTokenColor(tokenColor);
                gameController.addPlayer(player);
            }
            pendingPlayers.remove(player);
        }
        // join successful, but it is necessary to set the game for the client
        commandResult = CommandResult.CREATE_SERVER_GAME;
    }

    /**
     * Method that creates a new GameController and adds it to the list gameControllers.
     * @param playersNumber number of player of the new game, decided by the first player to join.
     */
    private int createGame(int playersNumber) throws WrongNumberOfPlayersException {
        // check players number
        if(playersNumber < 2 || playersNumber > 4){
            throw new WrongNumberOfPlayersException();
        }

        // find first free id
        int id = findFirstFreeId();

        // create and shuffle decks
        DrawableDeck<DrawableCard> resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        DrawableDeck<GoldCard> goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardDeck.shuffle();
        Deck<PlaceableCard> starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        starterCardsDeck.shuffle();

        // create gameController
        GameController gameController = new GameController(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardDeck, starterCardsDeck);
        gameControllers.add(gameController);

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
            for(GameController g: gameControllers) {
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

    public void displayExistingGames() {
        commandResult = CommandResult.DISPLAY_GAMES;
    }






    // -----------------------------
    // TODO gestire questi
    // -----------------------------


    // TODO: chi lo chiama?
    // probabilmente chiamato da gamesmanager stesso periodicamente,
    // non creo command (per ora)
    // oppure chiamato dal metodo che setta game state a GAME_ENDED
    // se invio l'info sul vincitore al client, posso eliminare il game
    public void deleteGame(int id) {
        GameController gameController = null;
        for(GameController g: gameControllers) {
            if(g.getId() == id) {
                gameController = g;
            }
        }
        if(gameController != null && gameController.getState().equals(GameState.GAME_ENDED)){
            gameControllers.remove(gameController);
        }
    }

}
