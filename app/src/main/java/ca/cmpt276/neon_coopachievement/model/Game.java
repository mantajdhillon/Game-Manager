package ca.cmpt276.neon_coopachievement.model;

import androidx.annotation.NonNull;

import ca.cmpt276.neon_coopachievement.R;

public class Game {

    private int numPlayers;
    private int finalTotalScore;

    public Game(int numPlayers, int finalTotalScore) {
        this.numPlayers = numPlayers;
        this.finalTotalScore = finalTotalScore;
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

    @NonNull
    @Override
    public String toString() {
        return numPlayers + " player(s) achieved a total score of " + finalTotalScore;
    }
}
