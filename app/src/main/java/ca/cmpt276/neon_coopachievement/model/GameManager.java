package ca.cmpt276.neon_coopachievement.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

// GameManager class: Holds the games for a single game type, such as the games for Poker or Blackjack

public class GameManager {
    private final ArrayList<Game> games = new ArrayList<>();
    private String name;
    private int greatScoreIndividual;
    private int poorScoreIndividual;

    public GameManager(String name, int gs, int ps){
        isValidName(name);
        isValidScore(gs,ps);
        this.name = name;
        this.greatScoreIndividual = gs;
        this.poorScoreIndividual = ps;
    }

    // Getters and setters
    public String getName(){
        return name;
    }

    public int getGreatScoreIndividual(){
        return greatScoreIndividual;
    }

    public int getPoorScoreIndividual(){
        return poorScoreIndividual;
    }

    public int size() {
        return games.size();
    }

    public void setName(String gameName){
        isValidName(gameName);
        this.name = gameName;
    }

    private void isValidName(String gameName) {
        if(gameName == null || gameName.equals("")){
            throw new InvalidParameterException("Invalid name");
        }
    }

    public void setGreatScoreIndividual(int score){
        isValidScore(score, poorScoreIndividual);
        this.greatScoreIndividual = score;
    }

    public void setPoorScoreIndividual(int score){
        isValidScore(greatScoreIndividual, score);
        this.poorScoreIndividual = score;
    }

    private void isValidScore(int gs, int ps) {
        if(gs <= ps){
            throw new InvalidParameterException("Invalid score entry");
        }
    }

    public Game getGame(Game toGet){
        boolean found = this.games.contains(toGet);
        if(found){
            for(int i = 0; i < games.size(); i++){
                if(games.get(i) == toGet){
                    return toGet;
                }
            }
        }
        throw new InvalidParameterException("Game does not exist!");
    }

    public Game getGame(int i) {
        return games.get(i);
    }

    public String getGameString(int i) {
        return games.get(i).toString();
    }

    // addGame: adds a game to the games array
    public void addGame(Game game){
        this.games.add(game);
    }

    // deleteGame: deletes a game from the games array
    public void removeGame(int index){
        this.games.remove(index);
    }

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
        return name + ": " + size() + " games recorded.";
    }
}
