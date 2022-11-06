package ca.cmpt276.neon_coopachievement.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

import java.security.InvalidParameterException;

public class GameCategoryTest {
    @Test
    public void createTest(){
        GameCategory myCategory = GameCategory.getInstance();
        assertEquals(0, myCategory.getGameManagersStored());
    }

    @Test public void methodsTest(){
        GameCategory myCategory = GameCategory.getInstance();
        GameManager testManager = new GameManager("Test",5,1);

        // Test add + get
        myCategory.addGameManager(testManager);
        assertEquals(1,myCategory.getGameManagersStored());
        assertEquals(testManager,myCategory.getGameManager(testManager));

        // Test remove + get
        myCategory.removeGameManager(testManager);
        assertThrows(InvalidParameterException.class,
                ()->myCategory.getGameManager(testManager));
        assertEquals(0,myCategory.getGameManagersStored());

        myCategory.removeGameManager(testManager);
        assertEquals(0,myCategory.getGameManagersStored());
    }

}