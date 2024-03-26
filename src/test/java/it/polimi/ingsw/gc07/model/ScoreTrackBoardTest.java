package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.exceptions.PlayerAlreadyPresentException;
import it.polimi.ingsw.gc07.exceptions.PlayerNotPresentException;
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
        assertThrows(PlayerNotPresentException.class,
                () -> scoreTrackBoard.getScore(nickname));
        try {
            scoreTrackBoard.addPlayer(nickname);
            assertEquals(0, scoreTrackBoard.getScore(nickname));
        }catch(PlayerAlreadyPresentException | PlayerNotPresentException e){
            throw new RuntimeException();
        }
    }

    @Test
    public void setScoreTest() {
        String nickname = "MyPlayer";
        int newScore = 5;
        assertThrows(PlayerNotPresentException.class,
                () -> scoreTrackBoard.setScore(nickname, newScore));
        try {
            scoreTrackBoard.addPlayer(nickname);
            scoreTrackBoard.setScore(nickname, newScore);
            assertEquals(newScore, scoreTrackBoard.getScore(nickname));
        } catch (PlayerAlreadyPresentException | PlayerNotPresentException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void incrementScoreTest() {
        String nickname = "MyPlayer";
        int deltaScore = 5;
        assertThrows(PlayerNotPresentException.class,
                () -> scoreTrackBoard.incrementScore(nickname, deltaScore));
        try{
            scoreTrackBoard.addPlayer(nickname);
            scoreTrackBoard.incrementScore(nickname, deltaScore);
            assertEquals(deltaScore, scoreTrackBoard.getScore(nickname));
            scoreTrackBoard.incrementScore(nickname, deltaScore);
            scoreTrackBoard.incrementScore(nickname, deltaScore);
            assertEquals(3*deltaScore, scoreTrackBoard.getScore(nickname));
        }catch(PlayerAlreadyPresentException | PlayerNotPresentException e){
            throw new RuntimeException();
        }
    }

    @Test
    public void noDuplicatePlayers() {
        String nickname = "MyPlayer";
        assertThrows(PlayerNotPresentException.class,
                () -> scoreTrackBoard.getScore(nickname));
        try {
            scoreTrackBoard.addPlayer(nickname);
        }catch(PlayerAlreadyPresentException e){
            throw new RuntimeException();
        }
        assertThrows(PlayerAlreadyPresentException.class,
                () -> scoreTrackBoard.addPlayer(nickname));
    }
}