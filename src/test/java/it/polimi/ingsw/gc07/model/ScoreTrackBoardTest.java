package it.polimi.ingsw.gc07.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTrackBoardTest {
    ScoreTrackBoard scoreTrackBoard;

    @BeforeEach
    void setUp() {
        scoreTrackBoard = new ScoreTrackBoard();
    }

    @Test
    public void checkInitScore() {
        String nickname = "MyPlayer";
        scoreTrackBoard.addPlayer(nickname);
        assertEquals(0, scoreTrackBoard.getScore(nickname));
    }

    @Test
    public void setScoreTest() {
        String nickname = "MyPlayer";
        int newScore = 5;
        scoreTrackBoard.addPlayer(nickname);
        scoreTrackBoard.setScore(nickname, newScore);
        assertEquals(newScore, scoreTrackBoard.getScore(nickname));
    }

    @Test
    public void incrementScoreTest() {
        String nickname = "MyPlayer";
        int deltaScore = 5;
        scoreTrackBoard.addPlayer(nickname);
        scoreTrackBoard.incrementScore(nickname, deltaScore);
        assertEquals(deltaScore, scoreTrackBoard.getScore(nickname));
        scoreTrackBoard.incrementScore(nickname, deltaScore);
        scoreTrackBoard.incrementScore(nickname, deltaScore);
        assertEquals(3*deltaScore, scoreTrackBoard.getScore(nickname));
    }
}