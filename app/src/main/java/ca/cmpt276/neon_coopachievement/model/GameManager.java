package ca.cmpt276.neon_coopachievement.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class GameManager {
    private ArrayList<Integer> games = new ArrayList<>();
    private String name;
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
    public String getName(){
        return name;
    }

    public int getGoodScore(){
        return goodScore;
    }

    public int getBadScore(){
        return badScore;
    }

    public int getGamesStored(){
        return gamesStored;
    }

    public int getGame(int index){
        return games.get(index);
    }

    public String getAchievement(int index){
        return achievements.get(index);
    }
    public void setName(String gameName){
        name = gameName;
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
    public boolean deleteGame(int toDelete){
        boolean found = games.contains(toDelete);
        if(found){
            int remove = 0;
            for(int i=0; i<gamesStored; i++){
                if(games.get(i) == toDelete){
                    remove = i;
                    break;
                }
            }
            games.remove(remove);
            gamesStored--;
        }

        return found;

    }

}
