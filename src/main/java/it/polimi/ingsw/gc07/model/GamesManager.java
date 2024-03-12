package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.ArrayList;
import java.util.List;

public class GamesManager {
    /**
     * List of games.
     */
    private List<Game> games;

    /**
     * Created once the server is started.
     */
    public GamesManager() {
        this.games = new ArrayList<>();
    }

    /**
     * Accepts new player's data and adds him to a game.
     * Places in games are filled in order.
     */
    public void addPlayer(String nickname, TokenColor tokenColor, boolean connectionType, boolean interfaceType) {

        // attenzione, quanto scritto sotto è sbagliato
        // per la FA, il player può scegliere se creare una partita oppure
        // se partecipare ad una esiste

        // probabilmente controllo qua che il nickname sia unico

        // TODO
        // 1
        // Controlla se esiste un game con posti disponibili, ovvero
        // controllare se l'ultimo game della lista ha spazio disponibile
        // usando il metodo isFull
        // Altrimenti crea un nuovo game:
        // gameId è uguale all'indice nella lista
        // players number è scelto dal giocatore
        // crea i deck (json) e li mescola (forse)

        // Se deve poter scegliere il colore, getter dei giocatori in Game
        // per sapere i colori già usati

        // Se deve creare un nuovo gioco, chiede il numero di giocatori

        // 2
        // Sul game creato / sul game trovato:
        // game.addPlayer(nickname, tokenColor, connectionType)

        // eventualmente creare dei metodi privati (/statici) per spezzettare
    }

    private boolean checkNickname(String nickname) {
        // TODO
        // Controllare che sia unico
        // ritorna true se il nickname è unico
        // basta controllare che sia unico tra i nickname dei player
        // di tutti i game ??
        // TODO return
        return true;
    }

    // TODO
    // se un Game finisce, lo elimino dalla lista e li faccio scalare?
    // potrebbe essere chiamato e scorrere ed eliminare quelli finiti (stato)
}
