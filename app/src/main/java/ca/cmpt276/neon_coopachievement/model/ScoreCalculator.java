package ca.cmpt276.neon_coopachievement.model;

import java.util.ArrayList;

public class ScoreCalculator {

    int numPlayers;
    int finalScore;
    ArrayList<Integer> scores = new ArrayList<>();


    public ScoreCalculator(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    int getNumPlayers() {
        return numPlayers;
    }

    void addScore(int score) {
        scores.add(score);
        numPlayers++;
    }

    void removeScore(int player) {
        scores.remove(player-1);
        numPlayers--;
    }

    int getScore(int player) {
        return scores.get(player-1);
    }

    void calculateFinal() {
        finalScore = 0;
        for (int i=0; i <= scores.size(); i++) {
            finalScore += scores.get(i);
        }
    }

    int getFinalScore() {
        return finalScore;
    }

    void clearAll() {
        numPlayers = 0;
        scores = new ArrayList<>();
    }
}
