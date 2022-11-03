package ca.cmpt276.neon_coopachievement.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

public class GameCategoryTest {
    @Test
    public void createTest(){
        GameCategory myCategory = new GameCategory();
        assertEquals(0, myCategory.getGameManagersStored());
    }

    @Test public void getManagerTest(){
        GameCategory myCategory = new GameCategory();
        GameManager testManager = new GameManager();
        myCategory.addGameManager(testManager);
        assertEquals(testManager,myCategory.getGameManager(testManager));
    }

}