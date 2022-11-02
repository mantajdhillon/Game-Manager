package ca.cmpt276.neon_coopachievement.model;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Game {

    private final LocalDateTime time;
    private int numPlayers;
    private int finalTotalScore;
    private final Achievement achievements;
    private final int rank;

    public Game(int numPlayers, int finalTotalScore, int poorScore, int greatScore) {
        this.numPlayers = numPlayers;
        this.finalTotalScore = finalTotalScore;
        this.time  = LocalDateTime.now();
        this.achievements = new Achievement(poorScore, greatScore, numPlayers);
        this.rank = achievements.getRank(finalTotalScore);
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
        return achievements.getAchievementName(rank);
    }

    @NonNull
    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MMM dd @ HH:mm a -");
        return time.format(f) + numPlayers +
                " player(s) achieved a total score of " + finalTotalScore + " and their " +
                "achievement level is " + achievements.getAchievementName(rank);
    }
}
