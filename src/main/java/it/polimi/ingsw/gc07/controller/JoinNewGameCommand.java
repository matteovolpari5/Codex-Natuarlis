package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.PlayerNotPresentException;
import it.polimi.ingsw.gc07.exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

/**
 * Concrete command to add a pending player to a new game.
 */
public class JoinNewGameCommand implements GameCommand {
    /**
     * Reference to the games manager object.
     */
    private final GamesManager gamesManager;
    /**
     * Nickname of the player to add.
     */
    private final String nickname;
    /**
     * Token color chosen from the pending player.
     */
    private final TokenColor tokenColor;
    /**
     * Number of players for the new game.
     */
    private final int playersNumber;

    /**
     * Constructor of the concrete command JoinNewGameCommand.
     * @param gamesManager games manager
     * @param nickname nickname
     * @param tokenColor token color
     * @param playersNumber players number
     */
    public JoinNewGameCommand(GamesManager gamesManager, String nickname, TokenColor tokenColor, int playersNumber) {
        this.gamesManager = gamesManager;
        this.nickname = nickname;
        this.tokenColor = tokenColor;
        this.playersNumber = playersNumber;
    }

    /**
     * Execute method for the concrete command.
     * Creates a new game and adds the player to the newly created game.
     */
    @Override
    public void execute() {
        Player player = gamesManager.getPendingPlayer(nickname);
        if(player == null){
            // throw new PlayerNotPresentException();
            //TODO: no throws !!!
            // situazione tipo: il giocatore è già entrato in un gioco,
            // poi lancia di nuovo il comando per entrare in un gioco
            // cosa fare?
        }
        int gameId = -1;
        try{
            gameId = createGame(playersNumber);
        }
        catch(WrongNumberOfPlayersException e){
            //TODO rientra nei controlli per cui non possono ancora usare la bandierina,
            // ma devo notificare il player
        }
        for(Game game: gamesManager.getGames()) {
            if(game.getId() == gameId) {
                // no need to check the token color for the first player of the game
                assert(player != null): "The player was not found among pending ones";
                player.setTokenColor(tokenColor);
                game.setCommand(new AddPlayerCommand(game, player));
                game.execute();
            }
        }
    }

    /**
     * Method that creates a new Game and adds it to the list games.
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
        gamesManager.getGames().add(game);

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
            for(Game g: gamesManager.getGames()) {
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
}
