package ca.cmpt276.neon_coopachievement.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * GameManager Class
 * <p>
 * Description: Holds the games for a single game type, such as the games for Poker or Blackjack
 * - Takes the name, and good/poor individual scores for a given game, if the parameters are valid
 * - Holds games in an ArrayList
 * - Can only be accessed through the GameCategory instance
 */

public class GameManager {
    private final ArrayList<Game> games = new ArrayList<>();
    private String name;
    private int greatScoreIndividual;
    private int poorScoreIndividual;

    public GameManager(String name, int gs, int ps) {
        isValidName(name);
        isValidScore(gs, ps);
        this.name = name;
        this.greatScoreIndividual = gs;
        this.poorScoreIndividual = ps;
    }

    public String getName() {
        return name;
    }

    public int getGreatScoreIndividual() {
        return greatScoreIndividual;
    }

    public int getPoorScoreIndividual() {
        return poorScoreIndividual;
    }

    public int size() {
        return games.size();
    }

    public void setName(String gameName) {
        isValidName(gameName);
        this.name = gameName;
    }

    // Checks whether a given name is valid
    private void isValidName(String gameName) {
        if (gameName == null || gameName.isEmpty()) {
            throw new RuntimeException("Invalid name: " + gameName);
        }
    }

    public void setGreatScoreIndividual(int score) {
        isValidScore(score, poorScoreIndividual);
        this.greatScoreIndividual = score;
    }

    // Checks whether a given great score is valid
    public void setPoorScoreIndividual(int score) {
        isValidScore(greatScoreIndividual, score);
        this.poorScoreIndividual = score;
    }

    // Checks whether a given great and poor score are valid
    private void isValidScore(int gs, int ps) {
        if (gs <= ps) {
            throw new RuntimeException("Invalid score entry: ");
        }
    }

    public Game getGame(int i) {
        return games.get(i);
    }

    public String getGameString(int i) {
        return games.get(i).toString();
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void removeGame(int index) {
        games.remove(index);
    }

    // Fix the edits
    public void updateEdits(int poorScoreNew, int greatScoreNew) {
        for (int i = 0; i < games.size(); i++) {
            Game oldGame = games.get(i);
            Game newGame = new Game(oldGame.getNumPlayers(), oldGame.getFinalTotalScore(),
                    poorScoreNew, greatScoreNew);
            newGame.setTime(oldGame.getTime());
            games.set(i, newGame);
        }
    }

    @Override
    public String toString() {
        if (games.size() == 1) {
            return name + ": " + games.size() + " game recorded";
        } else {
            return name + ": " + games.size() + " games recorded";
        }
    }
}
