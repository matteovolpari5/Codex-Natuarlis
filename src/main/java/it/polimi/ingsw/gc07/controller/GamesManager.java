package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.updates.ExistingGamesUpdate;
import it.polimi.ingsw.gc07.utils.DecksBuilder;
import it.polimi.ingsw.gc07.game_commands.GamesManagerCommand;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.TokenColor;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;
import it.polimi.ingsw.gc07.utils.SafePrinter;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Class representing the lobby controller, manager creation of games,
 * adds players to games and deletes games.
 */
public class GamesManager {
    /**
     * Instance of GamesManager.
     */
    private static GamesManager myGamesManager;
    /**
     * List of game controllers.
     */
    private List<GameController> gameControllers;
    /**
     * List of players who have not chosen a game.
     */
    private List<Player> pendingPlayers;
    /**
     * Command result manager for games manager.
     */
    private CommandResult commandResult;
    /**
     * Map containing pending players' virtual views.
     */
    private Map<String, VirtualView> playerVirtualViews;
    /**
     * Map containing players' timers.
     */
    private Map<String, Timer> playersTimers;

    /**
     * GamesManger is created once the server is started.
     * GamesManager implements Singleton pattern.
     */
    private GamesManager() {
        this.gameControllers = new ArrayList<>();
        this.pendingPlayers = new ArrayList<>();
        this.commandResult = null;
        this.playerVirtualViews = new HashMap<>();
        this.playersTimers = new HashMap<>();
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
     * Method used to add a player's virtual view.
     * @param nickname nickname
     * @param virtualView virtual view
     */
    public synchronized void addVirtualView(String nickname, VirtualView virtualView) {
        assert(!playerVirtualViews.containsKey(nickname));
        assert(virtualView != null);
        playerVirtualViews.put(nickname, virtualView);
    }

    /**
     * Method used to the get the virtual view of a player with a certain nickname.
     * @param nickname nickname
     * @return virtual view
     */
    private synchronized VirtualView getVirtualView(String nickname) {
        assert(playerVirtualViews.containsKey(nickname));
        return playerVirtualViews.get(nickname);
    }

    /**
     * Only for test purposes, return a new instance of GamesManager, not using Singleton.
     */
    // used in tests
    void resetGamesManager() {
        gameControllers = new ArrayList<>();
        pendingPlayers = new ArrayList<>();
        commandResult = null;
        playerVirtualViews = new HashMap<>();
        playersTimers = new HashMap<>();
    }

    /**
     * Return the game with the provided id.
     * @return game with given id
     */
    public synchronized GameController getGameById(int id) {
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
    // used for tests
    CommandResult getCommandResult() {
        return commandResult;
    }

    /**
     * Setter for gameControllers manager command (command pattern).
     * @param gamesManagerCommand games manager command
     */
    public synchronized void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) {
        if(playersTimers.containsKey(gamesManagerCommand.getNickname())){
            playersTimers.get(gamesManagerCommand.getNickname()).cancel();
            playersTimers.get(gamesManagerCommand.getNickname()).purge();
            playersTimers.remove(gamesManagerCommand.getNickname());
            Timer timeout = new Timer();
            playersTimers.put(gamesManagerCommand.getNickname(), timeout);
            new Thread(()->{
                try {
                    timeout.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            synchronized (this){
                                removePlayer(gamesManagerCommand.getNickname());
                            }
                        }
                    }, 5 * 60 * 1000); //timer of 5 minute
                }catch(IllegalStateException e) {
                    // another timer already started
                }
            }).start();
        }
        gamesManagerCommand.execute(this);
    }

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
    // used for tests
    int getGameIdWithPlayer(String nickname) {
        for(GameController g: gameControllers) {
            if(g.hasPlayer(nickname)) {
                return g.getId();
            }
        }
        return -1;
    }

    /**
     * Method used to add a player to pending.
     * @param nickname nickname
     * @param connectionType connection type
     * @param interfaceType interface type
     */
    public void addPlayerToPending(String nickname, boolean connectionType, boolean interfaceType) {
        // this command can always be used
        assert(!checkReconnection(nickname));
        assert(checkNicknameUnique(nickname));

        Player newPlayer = new Player(nickname, connectionType, interfaceType);
        pendingPlayers.add(newPlayer);
        Timer timeout = new Timer();
        playersTimers.put(nickname, timeout);
        new Thread(() -> {
            try {
                timeout.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (this){
                            removePlayer(nickname);
                        }
                    }
                }, 5 * 60 * 1000); //timer of 5 minute
            }catch(IllegalStateException e) {
                // another timer already started
            }
        }).start();
        commandResult = CommandResult.SUCCESS;
    }

    /**
     * Method used to check if a nickname corresponds to a player who is reconnecting to a game.
     * @param nickname nickname
     * @return true if the nickname is of a player who is reconnecting
     */
    private boolean checkReconnection(String nickname) {
        boolean reconnection = false;
        for(GameController gameController: gameControllers) {
            // if a game has a player with the same nickname and disconnected
            for(Player p: gameController.getPlayers()) {
                if(p.getNickname().equals(nickname) && !p.isConnected()) {
                    reconnection = true;
                    break;
                }
            }
        }
        return reconnection;
    }

    /**
     * Method used to reconnect a player
     * @param client client reference
     * @param nickname nickname
     * @param connectionType connection type
     * @param interfaceType interface type
     */
    public void reconnectPlayer(VirtualView client, String nickname, boolean connectionType, boolean interfaceType) {
        for(GameController g: gameControllers) {
            if(g.hasPlayer(nickname)) {
                g.reconnectPlayer(client, nickname, connectionType, interfaceType);
            }
        }
    }

    /**
     * Method to check if a nickname is unique or another player has the same nickname.
     * @param nickname nickname to check
     * @return true if no other player has the same nickname
     */
    private boolean checkNicknameUnique(String nickname) {
        boolean unique = true;
        for(GameController g: gameControllers) {
            if(g.hasPlayer(nickname)) {
                unique = false;
            }
        }
        for(Player p: pendingPlayers) {
            if(p.getNickname().equals(nickname)) {
                unique = false;
                break;
            }
        }
        return unique;
    }

    /**
     * Method used to check if a nickname corresponds to a player who is reconnecting,
     * to a player who is already connected or to a new player.
     * @param nickname nickname
     * @return nickname check result
     */
    public synchronized NicknameCheck checkNickname(String nickname) {
        if(checkReconnection(nickname)) {
            return NicknameCheck.RECONNECTION;
        }else if(!checkNicknameUnique(nickname)) {
            return NicknameCheck.EXISTING_NICKNAME;
        }else {
            return NicknameCheck.NEW_NICKNAME;
        }
    }

    /**
     * Method used to join an existing game.
     * @param nickname nickname
     * @param tokenColor token color
     * @param gameId game id
     */
    public void joinExistingGame(String nickname, TokenColor tokenColor, int gameId) {
        // this command can always be used
        Player player = getPendingPlayer(nickname);
        assert(player != null);
        VirtualView playerVirtualView = getVirtualView(nickname);
        assert(playerVirtualView != null);

        boolean found = false;
        for(GameController gameController : gameControllers) {
            if(gameController.getId() == gameId) {
                found = true;
                // check gameController state WAITING_PLAYERS
                if(!gameController.getState().equals(GameState.GAME_STARTING)) {
                    commandResult = CommandResult.GAME_FULL;
                    try {
                        playerVirtualView.notifyJoinNotSuccessful();
                    } catch (RemoteException ex) {
                        // player disconnected, remove player from pending
                        removePlayer(nickname);
                    }
                    return;
                }
                // check token color unique
                if(gameController.hasPlayerWithTokenColor(tokenColor)) {
                    commandResult = CommandResult.TOKEN_COLOR_ALREADY_TAKEN;
                    try {
                        playerVirtualView.notifyJoinNotSuccessful();
                    } catch (RemoteException ex) {
                        // player disconnected, remove player from pending
                        removePlayer(nickname);
                    }
                    return;
                }
                player.setTokenColor(tokenColor);

                // add player to game
                try {
                    playerVirtualView.setServerGame(gameId);
                }catch (RemoteException ex) {
                    // player disconnected, remove player from pending
                    removePlayer(nickname);
                    //TODO
                }
                gameController.addPlayer(player, playerVirtualViews.get(nickname));
            }
        }
        if(!found){
            commandResult = CommandResult.GAME_NOT_PRESENT;
            try {
                playerVirtualView.notifyJoinNotSuccessful();
            } catch (RemoteException ex) {
                // player disconnected, remove player from pending
                removePlayer(nickname);
            }
            return;
        }
        // remove player
        synchronized (this){
            playersTimers.get(nickname).cancel();
            playersTimers.get(nickname).purge();
        }
        removePlayer(nickname);

        commandResult = CommandResult.SUCCESS;
    }

    /**
     * Method used to join a new game.
     * @param nickname nickname
     * @param tokenColor token color
     * @param playersNumber number of players in the new game, ranging from 2 to 4
     */
    public void joinNewGame(String nickname, TokenColor tokenColor, int playersNumber) {
        // this command can always be used
        Player player = getPendingPlayer(nickname);
        assert(player != null);
        // no need to check the token color for the first player of the gameController
        player.setTokenColor(tokenColor);
        VirtualView playerVirtualView = getVirtualView(nickname);
        assert(playerVirtualView != null);
        // create game controller
        int gameId;
        try {
            gameId = createGame(playersNumber);
        }catch(WrongNumberOfPlayersException e) {
            commandResult = CommandResult.WRONG_PLAYERS_NUMBER;
            try {
                playerVirtualView.notifyJoinNotSuccessful();
            } catch (RemoteException ex) {
                // player disconnected, remove player from pending
                removePlayer(nickname);
            }
            // don't remove player from pending
            return;
        }
        // create game server
        for(GameController gameController : gameControllers) {
            if(gameController.getId() == gameId) {
                try {
                    RmiServerGamesManager.getRmiServerGamesManager().createServerGame(gameId);
                    try {
                        playerVirtualView.setServerGame(gameId);
                    } catch (RemoteException ex) {
                        // player disconnected, remove player from pending
                        removePlayer(nickname);
                        //TODO
                    }
                    gameController.addPlayer(player, playerVirtualView);
                }catch(RemoteException e) {
                    // couldn't create game server
                    // notify client
                    try {
                        playerVirtualView.notifyJoinNotSuccessful();
                    }catch(RemoteException ex) {
                        // player disconnected, remove player from pending
                        removePlayer(nickname);
                    }

                    // delete game controller
                    gameControllers.remove(gameController);
                    // don't remove player from pending
                    return;
                }
            }
        }
        // remove player
        synchronized (this){
            playersTimers.get(nickname).cancel();
            playersTimers.get(nickname).purge();
        }
        removePlayer(nickname);
        commandResult = CommandResult.SUCCESS;
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
     * Method that finds the first free game id, i.e. the lowest free game id.
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

    /**
     * Method used to receive the list of existing and free games.
     * @param nickname nickname of the player who is requesting free games
     */
    public void displayExistingGames(String nickname) {
        Player player = getPendingPlayer(nickname);
        assert(player != null);
        VirtualView virtualView = getVirtualView(nickname);
        assert(virtualView != null);
        ExistingGamesUpdate update = new ExistingGamesUpdate(getFreeGamesPlayerNumber(), getFreeGamesTokenColor());
        try{
            virtualView.receiveExistingGamesUpdate(update);
        }catch(RemoteException e) {
            // player disconnected, remove player from pending
            removePlayer(nickname);
        }
    }

    /**
     * Method used to get the player number for existing and free games.
     * @return map containing player number for existing and free games
     */
    public Map<Integer, Integer> getFreeGamesPlayerNumber() {
        Map<Integer, Integer> gameDetails = new HashMap<>();
        for(GameController g: gameControllers) {
            if(g.getState().equals(GameState.GAME_STARTING)) {
                gameDetails.put(g.getId(), g.getPlayersNumber());
            }
        }
        return gameDetails;
    }

    /**
     * Method used to get the taken token colors for existing and free games.
     * @return taken token colors for existing and free games
     */
    public Map<Integer, List<TokenColor>> getFreeGamesTokenColor() {
        Map<Integer, List<TokenColor>> gameDetails = new HashMap<>();
        for(GameController g: gameControllers) {
            if(g.getState().equals(GameState.GAME_STARTING)) {
                gameDetails.put(g.getId(), g.getTakenTokenColors());
            }
        }
        return gameDetails;
    }

    /**
     * Method used to delete a game once it is ended.
     * @param gameId game id of the game to delete
     */
    public synchronized void deleteGame(int gameId) {
        GameController gameController = null;
        for(GameController g: gameControllers) {
            if(g.getId() == gameId) {
                gameController = g;
            }
        }
        if(gameController != null && gameController.getState().equals(GameState.GAME_ENDED)){
            gameControllers.remove(gameController);
        }
    }

    /**
     * Method used to delete a player from the game.
     * @param nickname nickname
     */
    private void removePlayer(String nickname) {
        Player player = getPendingPlayer(nickname);
        pendingPlayers.remove(player);
        playerVirtualViews.remove(nickname);
        playersTimers.remove(nickname);
        SafePrinter.println("Player removed from pending.");
    }
}

