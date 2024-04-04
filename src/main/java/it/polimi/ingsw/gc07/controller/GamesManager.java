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
     * GameCommand for command pattern.
     */
    private GameCommand gameCommand;

    /**
     * GamesManger is created once the server is started.
     */
    public GamesManager() {
        games = new ArrayList<>();
        pendingPlayers = new ArrayList<>();
        gameCommand = null;
    }

    /**
     * Friendly getter method for attribute games, used for Command pattern.
     * @return games
     */
    List<Game> getGames() {
        return games;
    }

    /**
     * Friendly getter method for attribute players, used for Command pattern.
     * @return players
     */
    List<Player> getPendingPlayerspending() {
        return pendingPlayers;
    }

    /**
     * Setter for game command, used to set the game command of Command pattern.
     * @param gameCommand game command to set
     */
    public void setGameCommand(GameCommand gameCommand) {
        this.gameCommand = gameCommand;
    }

    /**
     * Execute method, used to execute the command of Command pattern.
     */
    public void execute() {
        gameCommand.execute();
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


    // TODO gestire questi

    // probabilmente observer (?), probabilmente non void
    // quindi no game command
    public void displayExistingGames() {
        // TODO
        // probabilmente non void
    }

    // TODO: chi lo chiama?
    // probabilmente chiamato da gamesmanager stesso periodicamente,
    // non creo command (per ora)
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
