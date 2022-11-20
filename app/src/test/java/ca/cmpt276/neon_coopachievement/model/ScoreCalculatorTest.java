package ca.cmpt276.neon_coopachievement.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ScoreCalculatorTest {

    @Test
    void testObjectCreation() {
        ArrayList<Integer> scoresList = new ArrayList<>();
        ScoreCalculator sc = new ScoreCalculator();

        assertEquals(0, sc.getNumPlayers());
        assertEquals(0, sc.getSumScores());
        assertEquals(scoresList, sc.getScores());
    }

    @Test
    void testScoresSetterValidNonEmptyScoresList() {
        ArrayList<Integer> scoresList = new ArrayList<>();
        int sumScores = 0;
        for (int i = 0; i < 5; i++) {
            scoresList.add(i);
            sumScores += i;
        }

        ScoreCalculator sc = new ScoreCalculator();
        sc.setScores(scoresList);

        assertEquals(5, sc.getNumPlayers());
        assertEquals(sumScores, sc.getSumScores());
        assertEquals(scoresList, sc.getScores());
    }

    @Test
    void testScoresSetterValidEmptyScoresList() {
        ArrayList<Integer> scoresList = new ArrayList<>();

        ScoreCalculator sc = new ScoreCalculator();
        sc.setScores(scoresList);

        assertEquals(0, sc.getNumPlayers());
        assertEquals(0, sc.getSumScores());
        assertEquals(scoresList, sc.getScores());
    }

    @Test
    void testScoresSetterInvalidScoresList() {
        ArrayList<Integer> scoresList = new ArrayList<>();
        ScoreCalculator sc = new ScoreCalculator();

        assertThrows(IllegalArgumentException.class,
                () -> sc.setScores(null));
        assertEquals(0, sc.getNumPlayers());
        assertEquals(0, sc.getSumScores());
        assertEquals(scoresList, sc.getScores());
    }

    @Test
    void testAddScore() {
        ScoreCalculator sc = new ScoreCalculator();

        sc.addScore(0);
        sc.addScore(1);
        sc.addScore(2);
        assertEquals(0, sc.getScore(1));
        assertEquals(1, sc.getScore(2));
        assertEquals(3, sc.getSumScores());
        assertThrows(IllegalArgumentException.class,
                () -> sc.addScore(-1));
    }

    @Test
    void testUpdateScore() {
        ArrayList<Integer> scoresList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            scoresList.add(i);
        }

        ScoreCalculator sc = new ScoreCalculator();
        sc.setScores(scoresList);

        sc.updateScore(1, 5);
        sc.updateScore(3, 1);
        sc.updateScore(5, 3);

        assertEquals(5, sc.getScore(1));
        assertEquals(1, sc.getScore(3));
        assertEquals(3, sc.getScore(5));

        assertThrows(IllegalArgumentException.class,
                () -> sc.updateScore(0, 10));
        assertThrows(IllegalArgumentException.class,
                () -> sc.updateScore(-1, 10));
        assertThrows(IllegalArgumentException.class,
                () -> sc.updateScore(6, 10));
        assertThrows(IllegalArgumentException.class,
                () -> sc.updateScore(1, -1));
    }

    @Test
    void testRemoveScore() {
        ArrayList<Integer> scoresList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            scoresList.add(i);
        }

        ScoreCalculator sc = new ScoreCalculator();
        sc.setScores(scoresList);

        assertThrows(IllegalArgumentException.class,
                () -> sc.removeScore(0));
        assertThrows(IllegalArgumentException.class,
                () -> sc.removeScore(-1));
        assertThrows(IllegalArgumentException.class,
                () -> sc.removeScore(6));

        assertEquals(0, sc.getScore(1));
        sc.removeScore(1); // first score removed: 1, 2, 3, 4
        assertEquals(1, sc.getScore(1));
        sc.removeScore(2); // middle score removed: 1, 3, 4
        assertEquals(3, sc.getScore(2));
        sc.removeScore(sc.getNumPlayers()); // last score removed: 1, 3
        assertEquals(3, sc.getScore(sc.getNumPlayers()));

        assertEquals(4, sc.getSumScores());
    }

    @Test
    void testGetScore() {
        ArrayList<Integer> scoresList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            scoresList.add(i);
        }

        ScoreCalculator sc = new ScoreCalculator();
        sc.setScores(scoresList);

        assertEquals(0, sc.getScore(1));
        assertEquals(2, sc.getScore(3));
        assertEquals(4, sc.getScore(5));
        assertThrows(IllegalArgumentException.class,
                () -> sc.getScore(-1));
        assertThrows(IllegalArgumentException.class,
                () -> sc.getScore(0));
        assertThrows(IllegalArgumentException.class,
                () -> sc.getScore(6));
    }

    @Test
    void testClearAll() {
        ArrayList<Integer> scoresList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            scoresList.add(i);
        }

        ScoreCalculator sc = new ScoreCalculator();
        sc.setScores(scoresList);

        sc.clearAll();

        scoresList = new ArrayList<>();

        assertEquals(0, sc.getNumPlayers());
        assertEquals(0, sc.getSumScores());
        assertEquals(scoresList, sc.getScores());
    }

    @Test
    void testToString() {
        ArrayList<Integer> scoresList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            scoresList.add(i);
        }

        ScoreCalculator sc = new ScoreCalculator();
        sc.setScores(scoresList);

        assertEquals("Player 1: 0", sc.toString(0));
        assertEquals("Player 3: 2", sc.toString(2));
        assertEquals("Player 5: 4", sc.toString(4));
        assertThrows(IllegalArgumentException.class,
                () -> sc.toString(-1));
        assertThrows(IllegalArgumentException.class,
                () -> sc.toString(sc.getNumPlayers()));
        assertThrows(IllegalArgumentException.class,
                () -> sc.toString(sc.getNumPlayers() + 1));
    }
}