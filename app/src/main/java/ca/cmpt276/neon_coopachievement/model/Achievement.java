package ca.cmpt276.neon_coopachievement.model;

/**
 * Achievements Class:
 * - Used to store the achievement boundaries of a game type.
 * - Each Game has an achievements variable that gets
 * - Constructed using the good score and bad score from the game manager, and the num players from the game.
 */
public class Achievement {
    private static final byte MIN_RANK = 1;
    private static final byte MAX_RANK = 10;

    private final double[] achievementBoundaries;
    private final String[] achievementNames;

    public Achievement(int low, int high, int numPlayers) {
        if (low > high) {
            throw new RuntimeException("Low score can not be greater than high score");
        } else if (numPlayers < 0) {
            throw new RuntimeException("Invalid number of players");
        }

        this.achievementNames = new String[]{
                "Horrendous Hamburgers", "Terrible Tacos",
                "Bad Broccoli's", "Alright Apples", "Mediocre Mangoes",
                "Okay Oranges", "Great Grapes", "Superb Sausages",
                "Awesome Avocados", "Excellent Eggs"
        };

        achievementBoundaries = new double[MAX_RANK - 1];

        double diff = (double) (high - low) / (MAX_RANK - 2);
        double d = low;
        for (int i = 0; i < achievementBoundaries.length; i++) {
            achievementBoundaries[i] = d * numPlayers;
            d += diff;
        }
    }

    // Return the achievement name at a given rank
    public String getAchievementName(int gameRank) {
        if (gameRank < MIN_RANK || gameRank > MAX_RANK) {
            throw new ArrayIndexOutOfBoundsException("Rank of a game must be between: between " + MIN_RANK + " and " + MAX_RANK);
        }
        return achievementNames[gameRank - 1];
    }

    // Return the highest achievement rank index obtainable given the sum of player scores
    public int getHighestRank(int totalScore) {
        int maxRank = 1;
        for (int i = 0; i < achievementBoundaries.length
                && totalScore > achievementBoundaries[i]; i++) {
            maxRank++;
        }
        return maxRank;
    }

    // Print the Achievement at a given index
    public String getAchievementString(int rankIdx) {
        final int MIN_RANK_IDX = 0;
        final int MAX_RANK_IDX = MAX_RANK - 1;

        // Maximum achievement rank
        if (rankIdx == MAX_RANK_IDX) {
            return "Level " + MAX_RANK + " (>"
                    + (int) achievementBoundaries[MAX_RANK_IDX - 1] + "): "
                    + achievementNames[MAX_RANK_IDX];
        }

        // Minimum achievement rank
        else if (rankIdx == MIN_RANK_IDX) {
            return "Level " + (rankIdx + 1) + " (<"
                    + (int) achievementBoundaries[rankIdx] + "): "
                    + achievementNames[rankIdx];
        } else {
            return "Level " + (rankIdx + 1) + " ("
                    + (int) achievementBoundaries[rankIdx - 1] + " - "
                    + (int) achievementBoundaries[rankIdx]
                    + "): " + achievementNames[rankIdx];
        }
    }
}