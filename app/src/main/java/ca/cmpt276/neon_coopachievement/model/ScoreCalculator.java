package ca.cmpt276.neon_coopachievement.model;

import java.util.ArrayList;

public class ScoreCalculator {

    private int numPlayers = 0;
    private int sumScores;
    private ArrayList<Integer> scores = new ArrayList<>();


    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void addScore(int score) {
        scores.add(score);
        numPlayers++;
        calculateSum();
    }

    public void updateScore(int player, int score) {
        scores.remove(player - 1);
        scores.add(player - 1, score);
        calculateSum();
    }

    public void removeScore(int player) {
        scores.remove(player - 1);
        numPlayers--;
        calculateSum();
    }

    public int getScore(int player) {
        return scores.get(player-1);
    }

    private void calculateSum() {
        sumScores = 0;
        for (int i = 0; i < scores.size(); i++) {
            sumScores += scores.get(i);
        }
    }

    public int getSumScores() {
        return sumScores;
    }

    public void clearAll() {
        numPlayers = 0;
        scores = new ArrayList<>();
    }

    // takes in player - 1
    public String toString(int index) {
        return "Player " + (index + 1) + ": " + scores.get(index);
    }
}
