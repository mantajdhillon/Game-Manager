package ca.cmpt276.neon_coopachievement.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

// GameManager class: Holds the games for a single game type, such as the games for Poker or Blackjack

public class GameManager {
    private ArrayList<Game> games = new ArrayList<>();
    private String name;
    private int greatScoreIndividual;
    private int poorScoreIndividual;
    private int gamesStored;

    GameManager(String name, int gs, int ps){
        this.name = name;
        if(ps >= gs){
            throw new InvalidParameterException("Poor score cannot be greater than great score!");
        }
        this.greatScoreIndividual = gs;
        this.poorScoreIndividual = ps;

    }

    // Getters and setters
    public String getName(){
        return name;
    }

    public int getGreatScoreIndividual(){
        return greatScoreIndividual;
    }

    public int getPoorScoreIndividual(){
        return poorScoreIndividual;
    }

    public int getGamesStored(){
        return gamesStored;
    }

    public void setName(String gameName){
        this.name = gameName;
    }

    public void setGreatScoreIndividual(int score){
        this.greatScoreIndividual = score;
    }

    public void setPoorScoreIndividual(int score){
        this.poorScoreIndividual = score;
    }

    public Game getGame(Game toGet){
        boolean found = this.games.contains(toGet);
        if(found){
            for(int i=0; i<this.gamesStored; i++){
                if(games.get(i) == toGet){
                    return toGet;
                }
            }
        }
        throw new InvalidParameterException("Game does not exist!");
    }
    // addGame: adds a game to the games array
    public void addGame(Game game){
        this.games.add(game);
        this.gamesStored++;
    }

    // deleteGame: deletes a game from the games array and deincrements the total number of games
    public void removeGame(Game toDelete){
        // If game can be removed
        if(this.games.remove(toDelete)) {
            this.gamesStored--;
        }
    }

    @Override
    public String toString() {
        return name + ": " + getGamesStored() + " games recorded.";
    }
}
