package ca.cmpt276.neon_coopachievement.model;

import android.content.Context;
import android.content.Intent;

import java.security.InvalidParameterException;
import java.util.ArrayList;

// GameCategory class: Holds an the Game Managers for several different game types
public class GameCategory {
    private static GameCategory instance;
    private final ArrayList<GameManager> gameManagers = new ArrayList<>();
    private int gameManagersStored;
    private ArrayList<String> gameNames = new ArrayList<>();

    private GameCategory() {
        this.gameManagersStored = 0;
    }

    // Getters and Setters
    public int getGameManagersStored() {
        return this.gameManagersStored;
    }

    public ArrayList<String> getGameNames() {
        return gameNames;
    }

    public static GameCategory getInstance() {
        if (instance == null) {
            instance = new GameCategory();
        }
        return instance;
    }

    // getGameManager: searches through GameManagersStored, if it contains the object, for the object in question
    public GameManager getGameManager(GameManager toGet) {
        boolean found = this.gameManagers.contains(toGet);
        if (found) {
            for (int i = 0; i < this.gameManagersStored; i++) {
                if (this.gameManagers.get(i) == toGet) {
                    return this.gameManagers.get(i);
                }
            }
        }
        throw new InvalidParameterException("Game not found!");
    }

    public GameManager getGameManager(int i) {
        return gameManagers.get(i);
    }

    public String getGameManagerString(int i) {
        return gameManagers.get(i).toString();
    }

    // addGameManager: adds a game manager to the array and increments the total number of managers
    public void addGameManager(GameManager toAdd) {
        this.gameManagers.add(toAdd);
        this.gameManagersStored++;
        this.gameNames.add(toAdd.getName());
    }

    // removeGameManager: removes a game manager and decincrements the total number of managers
    public void removeGameManager(GameManager toRemove) {
        // If game can be removed
        if (this.gameManagers.remove(toRemove)) {
            this.gameManagersStored--;
            this.gameNames.remove(toRemove.getName());
        }
    }
}
