package ca.cmpt276.neon_coopachievement.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

// GameManager class: Holds the games for a single game type, such as the games for Poker or Blackjack

public class GameManager {
    private ArrayList<Game> games = new ArrayList<>();
    private String name;
    private int greatScore;
    private int poorScore;
    private int gamesStored;

    GameManager(){
        greatScore = 0;
        poorScore = 0;
        gamesStored = 0;
    }

    // Getters and setters
    public String getName(){
        return name;
    }

    public int getGreatScore(){
        return greatScore;
    }

    public int getPoorScore(){
        return poorScore;
    }

    public int getGamesStored(){
        return gamesStored;
    }

    public Game getGame(int index){
        return games.get(index);
    }

    public void setName(String gameName){
        this.name = gameName;
    }

    public void setGreatScore(int score){
        this.greatScore = score;
    }

    public void setPoorScore(int score){
        this.poorScore = score;
    }

    // addGame: adds a game to the games array
    public void addGame(Game game){
        this.games.add(game);
        this.gamesStored++;
    }

    // deleteGame: deletes a game from the games array. Returns true upon success, false otherwise.
    public void deleteGame(Game toDelete){
        boolean found = this.games.contains(toDelete);
        if(found){
            int remove = 0;
            for(int i=0; i<this.gamesStored; i++){
                if(this.games.get(i) == toDelete){
                    this.games.remove(remove);
                    this.gamesStored--;
                }
            }
        }
        else{
            throw new InvalidParameterException("Game not found!");
        }
    }

}
