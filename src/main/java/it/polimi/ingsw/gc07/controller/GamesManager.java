package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.*;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GamesManager {
    /**
     * List of games.
     */
    private List<Game> games;

    /**
     * GamesManger is created once the server is started.
     */
    public GamesManager() {
        games = new ArrayList<>();
    }

    /**
     * Method that creates a new Game and adds it to the list games.
     * @param playersNumber number of player of the new game, decided by the first player to join.
     * @throws WrongNumberOfPlayersException
     */
    private void createGame(int playersNumber) throws WrongNumberOfPlayersException {
        boolean flag = false;
        int id = 0;

        // TODO: compute id

        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardDeck.shuffle();
        Deck<PlaceableCard> starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        starterCardsDeck.shuffle();

        Game game = new Game(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardDeck, starterCardsDeck);
        games.add(game);
    }

    /**
     * Accepts new player's data and adds him to a game.
     * Places in games are filled in order.
     */
    public void addPlayer(String nickname, TokenColor tokenColor, boolean connectionType, boolean interfaceType) {

        // per la FA, il player può scegliere se creare una partita oppure
        // se partecipare ad una esistente

        // TODO


        // Altrimenti crea un nuovo game:
        // gameId è uguale all'indice nella lista
        // players number è scelto dal giocatore
        // crea i deck (json) e li mescola col metodo shuffle di Deck

        // Se deve poter scegliere il colore, getter dei giocatori in Game
        // per sapere i colori già usati

        // Se deve creare un nuovo gioco, chiede il numero di giocatori

        // 2
        // Sul game creato / sul game trovato:
        // game.addPlayer(nickname, tokenColor, connectionType,way)

        // eventualmente creare dei metodi privati (/statici) per spezzettare
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

    // deleteGame()
    // VORREI PRENDER COME PARAMETRO IL GAME, NON POSSO, ID?
    // Altrimenti ?
    // TODO
    // se un Game finisce, lo elimino dalla lista e li faccio scalare?
    // potrebbe essere chiamato e scorrere ed eliminare quelli finiti (stato)
    // oppure quando preleva il vincitore, elimina il Game
}
