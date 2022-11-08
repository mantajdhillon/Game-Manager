package ca.cmpt276.neon_coopachievement.model;

import java.util.ArrayList;

/**
 * GameCategory Class
 * <p>
 * Description: Holds the Game Managers for several different game types, such as Poker or Blackjack
 * - Singleton design: returns an instance of the GameCategory
 * - GameManagers are held in an ArrayList
 * - GameManagers can only be accessed through the GameCategory instance
 */
public class GameCategory {
    private static GameCategory instance;
    private final ArrayList<GameManager> gameManagerList;

    private GameCategory() {
        gameManagerList = new ArrayList<>();
    }

    public static GameCategory getInstance() {
        if (instance == null) {
            instance = new GameCategory();
        }
        return instance;
    }

    // Set the instance to the calling object
    public void setInstance() {
        instance = this;
    }

    public int size() {
        return gameManagerList.size();
    }

    public GameManager getGameManager(int i) {
        return gameManagerList.get(i);
    }

    public void addGameManager(GameManager toAdd) {
        this.gameManagerList.add(toAdd);
    }

    public void removeGameManager(int toRemove) {
        this.gameManagerList.remove(toRemove);
    }
}
