package ca.cmpt276.neon_coopachievement.model;

import android.content.Context;
import android.content.Intent;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/*  GameCategory
    Description: Holds the Game Managers for several different game types, such as Poker or Blackjack
                - Singleton design: returns an instance of the GameCategory
                - GameManagers are held in an ArrayList

*/

public class GameCategory {
    private static GameCategory instance;
    private final ArrayList<GameManager> gameManagers;

    private GameCategory() {
        gameManagers = new ArrayList<>();
    }

    public int getGameManagersStored() {
        return gameManagers.size();
    }

    public static GameCategory getInstance() {
        if (instance == null) {
            instance = new GameCategory();
        }
        return instance;
    }

    public GameManager getGameManager(GameManager toGet) {
        boolean found = this.gameManagers.contains(toGet);
        if (found) {
            for (int i = 0; i < gameManagers.size(); i++) {
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

    public void addGameManager(GameManager toAdd) {
        this.gameManagers.add(toAdd);
    }

    public void removeGameManager(int toRemove) {
        this.gameManagers.remove(toRemove);
    }
}
