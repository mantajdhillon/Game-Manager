package ca.cmpt276.neon_coopachievement.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

// GameCategory class: Holds an the Game Managers for several different game types
public class GameCategory {
    private GameCategory instance;
    private ArrayList<GameManager> gameManagers = new ArrayList<>();
    private int gameManagersStored;

    GameCategory(){
        this.gameManagersStored = 0;
    }

    // Getters and Setters
    public int getGameManagersStored(){
        return this.gameManagersStored;
    }

    public GameCategory getInstance(){
        if(instance == null){
            return new GameCategory();
        }
        return this.instance;
    }

    // getGameManager: searches through GameManagersStored, if it contains the object, for the object in question
    public GameManager getGameManager(GameManager toGet){
        boolean found = this.gameManagers.contains(toGet);
        if(found) {
            for (int i = 0; i < this.gameManagersStored; i++) {
                if (this.gameManagers.get(i) == toGet) {
                    return this.gameManagers.get(i);
                }
            }
        }
        throw new InvalidParameterException("Game not found!");
    }

    // addGameManager
    public void addGameManager(GameManager toAdd){
        this.gameManagers.add(toAdd);
        this.gameManagersStored++;
    }

    // removeGameManager
    public void removeGameManager(GameManager toRemove){
        boolean found = this.gameManagers.contains(toRemove);
        if(found){
            for(int i=0; i<this.gameManagersStored; i++){
                if(this.gameManagers.get(i) == toRemove){
                    this.gameManagers.remove(i);
                    break;
                }
            }
        }
        else{
            throw new InvalidParameterException("Game not found!");
        }
    }
}
