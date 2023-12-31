package ca.cmpt276.neon_coopachievement.model;

import android.media.Image;

import androidx.annotation.NonNull;

import java.io.File;
import java.net.URI;
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

    public static final String DATE_FORMAT = "MMM dd @ hh:mm a";

    private int numPlayers;
    private int finalTotalScore;
    private Achievement achievements;
    private int rank;
    private String time;
    private Difficulty difficulty;
    private String imagePath;

    private final ArrayList<Integer> scores;

    public Game(int numPlayers, int finalTotalScore, int lowScore, int highScore, ArrayList<Integer> scoresList, Difficulty difficulty, String imagePath) {
        this.numPlayers = numPlayers;
        this.finalTotalScore = finalTotalScore;
        this.achievements = new Achievement(lowScore, highScore, numPlayers, difficulty);
        this.rank = achievements.getHighestRank(finalTotalScore);
        this.difficulty = difficulty;
        this.imagePath = imagePath;

        // Populate list of scores
        this.scores = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            scores.add(scoresList.get(i));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        this.time = LocalDateTime.now().format(formatter);
    }

    public Game() {
        this.scores = new ArrayList<>();
    }

    public int getFinalTotalScore() {
        return finalTotalScore;
    }

    public void setFinalTotalScore(int finalTotalScore) {
        this.finalTotalScore = finalTotalScore;
    }

    public ArrayList<Integer> getScores() {
        return scores;
    }

    public void setScores(ArrayList<Integer> scoresList) {
        for (int i = 0; i < numPlayers; i++) {
            scores.add(i, scoresList.get(i));
        }
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRank() {
        return this.rank;
    }

    public void setAchievements(int numPlayers, int lowScore, int highScore, Difficulty difficulty) {
        this.achievements = new Achievement(lowScore, highScore, numPlayers, difficulty);
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    // Update the game achievement
    public void updateAchievements(Difficulty difficulty) {
        achievements.changeAchievementNames();
        achievements.changeDifficulty(difficulty);
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @NonNull
    @Override
    public String toString() {
        String result = "";

        if (numPlayers == 1) {
            result += numPlayers + " Player - ";
        } else {
            result += numPlayers + " Players - ";
        }

        result += time + "\n" +
                "Total score: " + finalTotalScore + "\n" +
                "Difficulty: " + difficulty.toString() + "\n" +
                "Rank #" + rank + ": " + achievements.getAchievementName(rank);

        return result;
    }
}
