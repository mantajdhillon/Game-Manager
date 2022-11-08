package ca.cmpt276.neon_coopachievement.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/* GameManager
   Description: Holds the games for a single game type, such as the games for Poker or Blackjack
                - Takes the name, and good/poor individual scores for a given game
                - Holds games in an ArrayList
*/

public class GameManager {
    private ArrayList<Game> games = new ArrayList<>();
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

    public String getName(){
        return name;
    }

    public int getGreatScoreIndividual(){
        return greatScoreIndividual;
    }

    public int getPoorScoreIndividual(){
        return poorScoreIndividual;
    }

    public int getGamesStored(){
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

    public void addGame(Game game){
        this.games.add(game);
    }

    public void removeGame(int index){
        this.games.remove(index);
    }

    public void updateEdits(int poorScoreNew, int greatScoreNew) {
        for (int i = 0; i < getGamesStored(); i++) {
            Game oldGame = games.get(i);
            Game newGame = new Game(oldGame.getNumPlayers(), oldGame.getFinalTotalScore(),
                    poorScoreNew, greatScoreNew);
            newGame.setTime(oldGame.getTime());
            games.set(i, newGame);
        }
    }

    @Override
    public String toString() {
        return name + ": " + getGamesStored() + " games recorded.";
    }
}
