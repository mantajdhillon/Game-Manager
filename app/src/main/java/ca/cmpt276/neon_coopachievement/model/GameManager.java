package ca.cmpt276.neon_coopachievement.model;

import androidx.annotation.NonNull;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * GameManager Class
 * <p>
 * Description: Holds the games for a single game type, such as the games for Poker or Blackjack
 * - Takes the name, and good/poor individual scores for a given game, if the parameters are valid
 * - Holds games in an ArrayList
 * - Can only be accessed through the GameCategory instance
 */

public class GameManager {
    private final ArrayList<Game> games = new ArrayList<>();
    private String name;
    private int greatScoreIndividual;
    private int poorScoreIndividual;

    private ArrayList<Integer> achievementTally;

    public GameManager(String name, int goodScore, int poorScore) {
        isValidName(name);
        isValidScore(goodScore, poorScore);
        this.name = name;
        this.greatScoreIndividual = goodScore;
        this.poorScoreIndividual = poorScore;
        this.achievementTally = new ArrayList<>();
        for(int i=0; i<10; i++){
            achievementTally.add(0);
        }
    }

    public String getName() {
        return name;
    }

    public Game getGame(int i) {
        return games.get(i);
    }

    public int getGreatScoreIndividual() {
        return greatScoreIndividual;
    }

    public int getPoorScoreIndividual() {
        return poorScoreIndividual;
    }

    public int getSize() {
        return games.size();
    }

    public void setName(String gameName) {
        this.name = gameName;
    }

    public void setGreatScoreIndividual(int score) {
        this.greatScoreIndividual = score;
    }

    // Checks whether a given great score is valid
    public void setPoorScoreIndividual(int score) {
        this.poorScoreIndividual = score;
    }

    // Checks whether a given name is valid
    public void isValidName(String gameName) {
        if (gameName == null || gameName.isEmpty()) {
            throw new RuntimeException("Invalid name: " + gameName);
        }
    }

    // Checks whether a given great and poor score are valid
    public void isValidScore(int goodScore, int poorScore) {
        if (goodScore <= poorScore) {
            throw new RuntimeException("Invalid score entry: ");
        }
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void removeGame(int index) {
        games.remove(index);
    }

    public void updateEdits(int poorScoreNew, int greatScoreNew) {
        for (int i = 0; i < games.size(); i++) {
            Game oldGame = games.get(i);
            Game newGame = new Game(oldGame.getNumPlayers(), oldGame.getFinalTotalScore(),
                    poorScoreNew, greatScoreNew, oldGame.getScores(), oldGame.getDifficulty());
            newGame.setTime(oldGame.getTime());
            games.set(i, newGame);
        }
    }

    public void addTally(int index){
        try{
            int oldVal = achievementTally.get(index);
            achievementTally.set(index,oldVal + 1);
        }
        catch (Exception e){
            throw new InvalidParameterException("Index invalid!");
        }
    }

    public void decreaseTally(int index){
        try {
            int oldVal = achievementTally.get(index);
            achievementTally.set(index, oldVal - 1);
        }
        catch (Exception e) {
            throw new InvalidParameterException("Old index invalid!");
        }
        if (achievementTally.get(index) < 0) {
            achievementTally.set(index, 0);
        }
    }

    public int getTally(int index){
        try{
            return achievementTally.get(index);
        }
        catch (Exception e){
            throw new InvalidParameterException("Index invalid!");
        }
    }

    public String tallyToString(int index){
        try{
            return "\n\nTimes Achieved: " + getTally(index);
        }
        catch (Exception e){
            throw new InvalidParameterException("Index invalid!");
        }
    }

    @NonNull
    @Override
    public String toString() {
        if (games.size() == 1) {
            return name + ": " + games.size() + " game recorded";
        } else {
            return name + ": " + games.size() + " games recorded";
        }
    }
}
