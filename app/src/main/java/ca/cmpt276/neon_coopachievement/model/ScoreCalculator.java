package ca.cmpt276.neon_coopachievement.model;

import java.util.ArrayList;

/**
 * Score Calculator Class:
 * <p>
 * - Used to calculate the sum of all scores of an individual player in a single game.
 * - Holds scores of each player, sum scores, and num players.
 */
public class ScoreCalculator {

    private int numPlayers;
    private int sumScores;
    private ArrayList<Integer> scores;

    public ScoreCalculator() {
        this.numPlayers = 0;
        this.sumScores = 0;
        this.scores = new ArrayList<>();
    }


    public void addScore(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Invalid score");
        } else {
            scores.add(score);
            numPlayers++;
            calculateSum();
        }
    }

    public void updateScore(int player, int score) {
        if (player <= 0 || player > numPlayers) {
            throw new IllegalArgumentException("Invalid player");
        } else if (score < 0) {
            throw new IllegalArgumentException("Invalid score");
        } else {
            scores.remove(player - 1);
            scores.add(player - 1, score);
            calculateSum();
        }
    }

    public void removeScore(int player) {
        if (player <= 0 || player > numPlayers) {
            throw new IllegalArgumentException("Invalid player");
        } else {
            scores.remove(player - 1);
            numPlayers--;
            calculateSum();
        }
    }

    public ArrayList<Integer> getScores() {
        return scores;
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

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getScore(int player) {
        if (player <= 0 || player > numPlayers) {
            throw new IllegalArgumentException("Invalid player");
        } else {
            return scores.get(player - 1);
        }
    }

    public void setScores(ArrayList<Integer> scoresList) {
        if (scoresList == null) {
            throw new IllegalArgumentException("Invalid list of scores");
        } else {
            this.numPlayers = scoresList.size();
            this.scores = new ArrayList<>();
            for (int i = 0; i < numPlayers; i++) {
                this.scores.add(scoresList.get(i));
            }
            calculateSum();
        }
    }

    public void clearAll() {
        numPlayers = 0;
        sumScores = 0;
        scores = new ArrayList<>();
    }

    // Prints a score as Takes in index player - 1
    public String toString(int index) {
        if (index < 0 || index >= numPlayers) {
            throw new IllegalArgumentException("Invalid index");
        } else {
            return "Player " + (index + 1) + ": " + scores.get(index);
        }
    }
}
