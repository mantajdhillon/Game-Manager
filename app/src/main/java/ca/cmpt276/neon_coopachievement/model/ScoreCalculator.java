package ca.cmpt276.neon_coopachievement.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Score Calculator Class:
 * <p>
 * - Used to calculate the sum of all scores of an individual player in a single game.
 * - Holds scores of each player, sum scores, and num players.
 */
public class ScoreCalculator {

//    private int numPlayers;
    private int sumScores;
    private ArrayList<Integer> scoreList;
    private final Queue<Integer> lostScoreList;

    public ScoreCalculator() {
//        this.numPlayers = 0;
        this.sumScores = 0;
        this.scoreList = new ArrayList<>();
        this.lostScoreList = new LinkedList<>();
    }


    public void addScore(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Invalid score");
        } else {
            scoreList.add(score);
//            numPlayers++;
            calculateSum();
        }
    }

    public void updateScore(int player, int score) {
        if (player <= 0 || player > scoreList.size()) {
            throw new IllegalArgumentException("Invalid player");
        } else if (score < 0) {
            throw new IllegalArgumentException("Invalid score");
        } else {
            scoreList.remove(player - 1);
            scoreList.add(player - 1, score);
            calculateSum();
        }
    }

    public void removeScore(int player) {
        if (player <= 0 || player > scoreList.size()) {
            throw new IllegalArgumentException("Invalid player");
        } else {
            // Remove player and store lost score
            int playerToRemove = scoreList.remove(player - 1);
            lostScoreList.add(playerToRemove);
//            numPlayers--;
            calculateSum();
        }
    }

    // Check if there are any remaining lost scores
    public boolean hasLostScore() {
        return !lostScoreList.isEmpty();
    }

    // Removes all lost scores
    public void clearLostScores() {
        lostScoreList.clear();
    }

    // Remove and return the last lost score
    public int peekLostScore() {
        return lostScoreList.peek();
    }

    // Remove and return the last lost score
    public void popLostScore() {
        lostScoreList.poll();
    }

    public ArrayList<Integer> getScoreList() {
        return scoreList;
    }

    private void calculateSum() {
        sumScores = 0;
        for (int i = 0; i < scoreList.size(); i++) {
            sumScores += scoreList.get(i);
        }
    }

    public int getSumScores() {
        return sumScores;
    }

    public int getNumPlayers() {
        return scoreList.size();
    }

    public int getScore(int player) {
        if (player <= 0 || player > scoreList.size()) {
            throw new IllegalArgumentException("Invalid player");
        } else {
            return scoreList.get(player - 1);
        }
    }

    public void setScoreList(ArrayList<Integer> scoresList) {
        if (scoresList == null) {
            throw new IllegalArgumentException("Invalid list of scores");
        } else {
//            this.numPlayers = scoresList.size();
            this.scoreList = new ArrayList<>(scoresList);
//            for (int i = 0; i < numPlayers; i++) {
//                this.scoreList.add(scoresList.get(i));
//            }
            calculateSum();
        }
    }

    public void clearAll() {
//        numPlayers = 0;
        sumScores = 0;

        // TODO: when pressing clear, should I also save the lost scores?
        //  Ex. If i pressed (+), it would say 11, then 22, then 33, then ___?
        //  Or would it make more sense if it were a "restart everything I was doing" button

        // Add all scores into lost scores
        for (int i = 0; i < scoreList.size(); i++) {
            lostScoreList.add(scoreList.get(i));
        }

        scoreList = new ArrayList<>();
    }

    // Prints a score as Takes in index player - 1
    public String toString(int index) {
        if (index < 0 || index >= scoreList.size()) {
            throw new IllegalArgumentException("Invalid index");
        } else {
            return "Player " + (index + 1) + ": " + scoreList.get(index);
        }
    }
}
