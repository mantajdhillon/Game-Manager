package ca.cmpt276.neon_coopachievement.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class GameManager {
    private ArrayList<Integer> games = new ArrayList<>();
    private int goodScore;
    private int badScore;
    private int gamesStored;
    private ArrayList<String> achievements = new ArrayList<>();

    GameManager(){
        goodScore = 0;
        badScore = 0;
        gamesStored = 0;
        for(int i=0; i<8; i++){
            achievements.add("Tier " + i);
        }
    }

    // Getters and setters
    public int getGoodScore(){
        return goodScore;
    }

    public int getBadScore(){
        return badScore;
    }

    public int getGamesStored(){
        return gamesStored;
    }

    public String getAchievement(int index){
        return achievements.get(index);
    }

    public void setGoodScore(int score){
        goodScore = score;
    }

    public void setBadScore(int score){
        badScore = score;
    }

    // addGame: adds a game to the games array
    public void addGame(int game){
        games.add(game);
        gamesStored++;
    }

    // deleteGame: deletes a game from the games array
    public void deleteGame(int game){
        boolean found = false;
        int remove = 0;
        for(int i=0; i<game; i++){
            if(i == game){
                remove = i;
                break;
            }
        }
        if(found){
            games.remove(remove);
        }
        else{
            throw new InvalidParameterException("Game not found!");
        }

    }

}
