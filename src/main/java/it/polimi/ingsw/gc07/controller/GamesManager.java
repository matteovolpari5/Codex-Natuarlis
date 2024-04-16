package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.model.CommandResultManager;
import it.polimi.ingsw.gc07.model.Player;

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
     * Command result manager for games manager.
     */
    private final CommandResultManager commandResultManager;


    /**
     * GamesManger is created once the server is started.
     */
    public GamesManager() {
        games = new ArrayList<>();
        pendingPlayers = new ArrayList<>();
        commandResultManager = new CommandResultManager();
    }

    public static GamesManager getGamesManager() {
        return null;
    } // TODO no null, ritorno singleton

    /**
     * Friendly getter method for attribute games, used for Command pattern.
     * @return games
     */
    List<Game> getGames() {
        return games;
    }

    /**
     * Return the game with the provided id.
     * @return game with given id
     */
    public Game getGameById(int id) {
        for(Game g: games) {
            if(g.getId() == id) {
                return g;
            }
        }
        return null;
    }

    /**
     * Friendly getter method for attribute players, used for Command pattern.
     * @return players
     */
    List<Player> getPendingPlayers() {
        return pendingPlayers;
    }

    /**
     * Getter for the command result manager.
     * @return command result manager of the games manager
     */
    public CommandResultManager getCommandResultManager() {
        return commandResultManager;
    }

    /**
     * Setter for games manager command (command pattern).
     * @param gamesManagerCommand games manager command to set
     */
    public void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) {
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
        for(Game g: games) {
            if(g.hasPlayer(nickname)) {
                return g.getId();
            }
        }
        return -1;
    }


    // -----------------------------
    // TODO gestire questi
    // -----------------------------

    // probabilmente observer (?), probabilmente non void
    // quindi no game command
    public void displayExistingGames() {
        // TODO
        // probabilmente non void
    }

    // TODO: chi lo chiama?
    // probabilmente chiamato da gamesmanager stesso periodicamente,
    // non creo command (per ora)
    // oppure chiamato dal metodo che setta game state a GAME_ENDED
    // se invio l'info sul vincitore al client, posso eliminare il game
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
