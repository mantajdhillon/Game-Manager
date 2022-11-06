package ca.cmpt276.neon_coopachievement.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

// GameManager class: Holds the games for a single game type, such as the games for Poker or Blackjack

public class GameManager {
    private ArrayList<Game> games = new ArrayList<>();
    private String name;
    private int greatScoreIndividual;
    private int poorScoreIndividual;

    public GameManager(String name, int gs, int ps){
        if(name == null || name.equals("")){
            throw new InvalidParameterException("Name cannot be empty");
        }
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
        return games.size();
    }

    public void setName(String gameName){
        this.name = gameName;
    }

    public void setGreatScoreIndividual(int score){
        if(score <= poorScoreIndividual){
            throw new InvalidParameterException("Invalid score entry");
        }
        this.greatScoreIndividual = score;
    }

    public void setPoorScoreIndividual(int score){
        if(score >= greatScoreIndividual){
            throw new InvalidParameterException("Invalid score entry");
        }
        this.poorScoreIndividual = score;
    }

    public Game getGame(Game toGet){
        boolean found = this.games.contains(toGet);
        if(found){
            for(int i = 0; i < games.size(); i++){
                if(games.get(i) == toGet){
                    return toGet;
                }
            }
        }
        throw new InvalidParameterException("Game does not exist!");
    }

    public Game getGame(int i) {
        return games.get(i);
    }

    public String getGameString(int i) {
        return games.get(i).toString();
    }

    // addGame: adds a game to the games array
    public void addGame(Game game){
        this.games.add(game);
    }

    // deleteGame: deletes a game from the games array and deincrements the total number of games
    public void removeGame(Game toDelete){
        // If game can be removed
        if(this.games.remove(toDelete)) {
        }
    }

    @Override
    public String toString() {
        return name + ": " + getGamesStored() + " games recorded.";
    }
}
