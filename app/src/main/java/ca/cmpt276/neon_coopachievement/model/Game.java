package ca.cmpt276.neon_coopachievement.model;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Game Class:
 * - Used to store one instance of a game.
 * - Each Game has a time which is constructed once.
 * - Takes a good score and a bad score and construct an appropriate achievement object
 */
public class Game {
    private static final String DATE_FORMAT = "MMM dd @ HH:mm a";

    private int numPlayers;
    private int finalTotalScore;
    private final Achievement achievements;
    private final int rank;
    private String time;

    public Game(int numPlayers, int finalTotalScore, int poorScore, int greatScore) {
        this.numPlayers = numPlayers;
        this.finalTotalScore = finalTotalScore;
        this.achievements = new Achievement(poorScore, greatScore, numPlayers);
        this.rank = achievements.getHighestRank(finalTotalScore);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        this.time = LocalDateTime.now().format(formatter);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getFinalTotalScore() {
        return finalTotalScore;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void setFinalTotalScore(int finalTotalScore) {
        this.finalTotalScore = finalTotalScore;
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

    @NonNull
    @Override
    public String toString() {
        return time + " - " + numPlayers + " player(s) " + "\n"
                + "Total score: " + finalTotalScore + "\n"
                + "Rank " + rank + ": " + achievements.getAchievementName(rank);
    }
}
