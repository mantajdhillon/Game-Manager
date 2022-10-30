package ca.cmpt276.neon_coopachievement.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class GameCategory {
    private GameCategory instance;
    private ArrayList<Integer> gameManagers = new ArrayList<>();
    private int GameManagersStored;

    GameCategory(){
        GameManagersStored = 0;
    }

    // Getters and Setters
    public int getGameManagersStored(){
        return GameManagersStored;
    }

    public GameCategory getInstance(){
        if(gameManagers == null){
            return new GameCategory();
        }
        return instance;
    }

    public int getGameManager(int toGet){
        for(int i=0; i<GameManagersStored; i++){
            if(gameManagers.get(i) == toGet){
                return gameManagers.get(i);
            }
        }
        throw new InvalidParameterException("Game not found!");
    }

    // addGameManager
    public void addGameManager(int toAdd){
        gameManagers.add(toAdd);
        GameManagersStored++;
    }
}
