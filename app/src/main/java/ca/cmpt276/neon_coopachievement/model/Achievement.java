package ca.cmpt276.neon_coopachievement.model;

import java.util.Arrays;

public class Achievement {
    private static final byte MAX_ACHIEVEMENTS = 10;

    private final double[] achievementBoundaries;
    private final String[] achievementNames;
    int numPlayers;

    public Achievement(int low, int high, int numPlayers) {
        if (low > high) {
            throw new RuntimeException("Low score can not be greater than high score");
        }
        if (numPlayers < 0) {
            throw new IllegalArgumentException("Invalid number of players");
        }
        this.numPlayers = numPlayers;
        this.achievementNames = new String[]{
                "Horrendous Hamburgers", "Terrible Tacos",
                "Bad Broccoli's", "Alright Apples", "Mediocre Mangoes",
                "Okay Oranges", "Great Grapes", "Superb Sausages",
                "Awesome Avocados", "Excellent Eggs"
        };

        achievementBoundaries = new double[MAX_ACHIEVEMENTS-1];

        double diff = (double) (high - low) / (MAX_ACHIEVEMENTS-2);
        double d = low;
        for (int i = 0; i < achievementBoundaries.length; i++) {
            achievementBoundaries[i] = d * numPlayers;
            d += diff;
        }
    }


    public double getAchievementBoundary(int i) {
        return achievementBoundaries[i];
    }

    public String getAchievementName(int gameRank) {
        if (gameRank < 1 || gameRank > MAX_ACHIEVEMENTS) {
            throw new ArrayIndexOutOfBoundsException
                    ("Rank of a game must be valid: between " + 1 + " and " + MAX_ACHIEVEMENTS);
        }
        return achievementNames[gameRank - 1];
    }

    public int getRank(int totalScore) {
        int rank = 1;
        int i = 0;
        while (i < achievementBoundaries.length &&
                totalScore > achievementBoundaries[i]) {
            rank++;
            i++;
        }
        return rank;
    }

    public String getAchievementString(int i) {
        if (i == MAX_ACHIEVEMENTS - 1) {
            return "Level " + MAX_ACHIEVEMENTS + " (>" + achievementBoundaries[MAX_ACHIEVEMENTS-2]
                    + "): " + achievementNames[MAX_ACHIEVEMENTS - 1];
        } else if (i == 0){
            return "Level " + (i+1) + " (<" + achievementBoundaries[i]
                    + "): " + achievementNames[i];
        } else {
            return "Level " + (i+1) + " (" + achievementBoundaries[i-1] + " - " + achievementBoundaries[i] + "): " + achievementNames[i];
        }
    }

}



