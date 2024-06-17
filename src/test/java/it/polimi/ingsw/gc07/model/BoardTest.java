package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.ModelListener;
import it.polimi.ingsw.gc07.model_view.BoardView;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    public void checkInitScore() {
        String nickname = "MyPlayer";
        board.addPlayer(nickname);
        assertEquals(0, board.getScore(nickname));
    }

    @Test
    public void setScoreTest() {
        String nickname = "MyPlayer";
        int newScore = 5;
        board.addPlayer(nickname);
        board.setScore(nickname, newScore);
        assertEquals(newScore, board.getScore(nickname));
    }

    @Test
    public void incrementScoreTest() {
        String nickname = "MyPlayer";
        int deltaScore = 5;
        board.addPlayer(nickname);
        board.incrementScore(nickname, deltaScore);
        assertEquals(deltaScore, board.getScore(nickname));
        board.incrementScore(nickname, deltaScore);
        board.incrementScore(nickname, deltaScore);
        assertEquals(3*deltaScore, board.getScore(nickname));
    }
    @Test
    public void testListener() {
        ModelListener listener1;
        try {
            listener1 = new RmiClient("MyPlayer", false,null);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ModelListener listener2;
        try {
            listener2 = new RmiClient("P2", false, null);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        board.addListener(listener1);
        board.addListener(listener2);
        BoardView bv = new BoardView();
        bv.addPlayerToBoard("P1", TokenColor.GREEN);
        board.addPlayer("P1");
        //board.addPublicMessage("content", "Player1");
        //board.addPrivateMessage("content", "Player1", "Player2");
        board.removeListener(listener1);
    }
}