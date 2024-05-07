package it.polimi.ingsw.gc07.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}