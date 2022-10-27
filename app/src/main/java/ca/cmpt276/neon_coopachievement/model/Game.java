package ca.cmpt276.neon_coopachievement.model;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Game {

    private LocalDateTime time;
    private int numPlayers;
    private int finalTotalScore;
    private Rank rank;

    public Game(int numPlayers, int finalTotalScore) {
        this.numPlayers = numPlayers;
        this.finalTotalScore = finalTotalScore;
        this.time  = LocalDateTime.now();
        setRank();
    }

    //TODO : Create function to determine the rank level based on the number of players and
    // expected good and bad player scores.
    private void setRank() {
        int rank = 5;

        this.rank = new Rank(rank);
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

    public String getAchievementLevelName() {
        return rank.getAchievementName();
    }

    @NonNull
    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MMM dd @ HH:mm a -");
        return time.format(f) + numPlayers +
                " player(s) achieved a total score of " + finalTotalScore + " and their " +
                "achievement level is " + rank.getAchievementName();
    }
}
