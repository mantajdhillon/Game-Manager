package ca.cmpt276.neon_coopachievement.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

// GameCategory class: Holds an the Game Managers for several different game types
public class GameCategory {
    private static GameCategory instance;
    private final ArrayList<GameManager> gameManagerList;

    private GameCategory() {
        gameManagerList = new ArrayList<>();
    }

    // Getters and Setters
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

    // addGameManager: adds a game manager to the array and increments the total number of managers
    public void addGameManager(GameManager toAdd) {
        this.gameManagerList.add(toAdd);
    }

    // removeGameManager: removes a game manager and decincrements the total number of managers
    public void removeGameManager(int toRemove) {
        this.gameManagerList.remove(toRemove);
    }
}
