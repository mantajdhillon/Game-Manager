package ca.cmpt276.neon_coopachievement.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class GameManagerTest {
    @Test
    public void createTest(){
        GameManager myManager = new GameManager("Test",5,1);
        assertEquals("Test",myManager.getName());
        assertEquals(5,myManager.getGreatScoreIndividual());
        assertEquals(1,myManager.getPoorScoreIndividual());
        assertEquals(0,myManager.size());

        assertThrows(InvalidParameterException.class,
                ()->myManager.setPoorScoreIndividual(5));
        assertThrows(InvalidParameterException.class,
                ()->myManager.setPoorScoreIndividual(10));
    }

    @Test
    public void gameTest(){
        GameManager myManager = new GameManager("Test",5,1);
        ArrayList<Integer> scores = new ArrayList<>();
        Game myGame = new Game(2,10,1,5, scores);
        myManager.addGame(myGame);
    }

}