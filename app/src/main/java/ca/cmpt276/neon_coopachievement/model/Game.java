package ca.cmpt276.neon_coopachievement.model;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Game Class:
 * <p>
 * - Used to store one instance of a game.
 * - Each Game has a time which is constructed once.
 * - Takes a good score and a bad score and construct an appropriate achievement object
 */
public class Game {

    // Represents the difficulty for a game
    public enum Difficulty {
        EASY, NORMAL, HARD
    }

    private static final String DATE_FORMAT = "MMM dd @ HH:mm a";

    private int numPlayers;
    private int finalTotalScore;
    private final Achievement achievements;
    private final int rank;
    private String time;
    private Difficulty difficulty;

    private ArrayList<Integer> scores;

    public Game(int numPlayers, int finalTotalScore, int poorScore, int greatScore, ArrayList<Integer> scoresList, Difficulty difficulty) {
        this.numPlayers = numPlayers;
        this.finalTotalScore = finalTotalScore;
        this.achievements = new Achievement(poorScore, greatScore, numPlayers, difficulty);     // TODO TEST
        this.rank = achievements.getHighestRank(finalTotalScore);
        this.difficulty = difficulty;
        this.scores = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            this.scores.add(scoresList.get(i));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        this.time = LocalDateTime.now().format(formatter);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getFinalTotalScore() {
        return finalTotalScore;
    }

    public ArrayList<Integer> getScores() {
        return scores;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void setFinalTotalScore(int finalTotalScore) {
        this.finalTotalScore = finalTotalScore;
    }

    public void setScores(ArrayList<Integer> scoresList) {
        for (int i = 0; i < numPlayers; i ++) {
            this.scores.add(i, scoresList.get(i));
        }
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }

    public int getRank() {
        return this.rank;
    }

    public int getGameScore(int index){
        if(index >= 0 && index < numPlayers){
            return scores.get(index);
        }
        else{
            throw new IllegalArgumentException("Invalid index!");
        }
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getAchievementTheme() {
        return this.achievements.getThemeString();
    }

    public void updateAchievements() {
        achievements.changeAchievementNames();
    }

    @NonNull
    @Override
    public String toString() {
        return numPlayers + " player(s) - " + time + "\n"
                + "Total score: " + finalTotalScore + "\n"
                + "Rank #" + rank + ": " + achievements.getAchievementName(rank);
    }
}
