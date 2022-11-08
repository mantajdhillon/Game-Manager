package ca.cmpt276.neon_coopachievement.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**  GameCategory
    Description: Holds the Game Managers for several different game types, such as Poker or Blackjack
                - Singleton design: returns an instance of the GameCategory
                - GameManagers are held in an ArrayList
*/
public class GameCategory {
    private static GameCategory instance;
    private final ArrayList<GameManager> gameManagerList;

    private GameCategory() {
        gameManagerList = new ArrayList<>();
    }

    public int getSize() {
        return gameManagerList.size();
    }

    public static GameCategory getInstance() {
        if (instance == null) {
            instance = new GameCategory();
        }
        return instance;
    }

    // Update the current instance to "this" object
    public void setInstance() {
        instance = this;
    }

    // getGameManager: searches through GameManagersStored, if it contains the object, for the object in question
    public GameManager getGameManager(GameManager toGet) {
        boolean found = this.gameManagerList.contains(toGet);
        if (found) {
            for (int i = 0; i < gameManagerList.size(); i++) {
                if (this.gameManagerList.get(i) == toGet) {
                    return this.gameManagerList.get(i);
                }
            }
        }
        throw new InvalidParameterException("Game not found!");
    }

    public GameManager getGameManager(int i) {
        return gameManagerList.get(i);
    }

    public String getGameManagerString(int i) {
        return gameManagerList.get(i).toString();
    }

    public void addGameManager(GameManager toAdd) {
        this.gameManagerList.add(toAdd);
    }

    public void removeGameManager(int toRemove) {
        this.gameManagerList.remove(toRemove);
    }
}
