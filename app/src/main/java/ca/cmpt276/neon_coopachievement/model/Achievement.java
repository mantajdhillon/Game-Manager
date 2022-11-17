package ca.cmpt276.neon_coopachievement.model;

/**
 * Achievements Class:
 * <p>
 * - Used to store the achievement boundaries of a game type.
 * - Each Game has an achievements variable that gets constructed using
 *   the good score, bad score, and the # of players from the game.
 */
public class Achievement {

    private static final byte MIN_ACHIEVEMENT_RANK = 1;
    private static final byte MAX_ACHIEVEMENT_RANK = 10;
    private static final double EASY_MULTIPLIER = 0.75;
    private static final double NORMAL_MULTIPLIER = 1.00;
    private static final double HARD_MULTIPLIER = 1.25;

    private final double[] rankBoundaries;
    private final String[] achievementNames;

    // Game variables needed for making score
    private final int numPlayers;
    private final int low;
    private final int high;
    private Game.Difficulty difficulty;
    private static int theme;

    public Achievement(int low, int high, int numPlayers, Game.Difficulty diff, int theme) {
        // Error handling for low and high
        if (low > high) {
            throw new RuntimeException("Low score can not be greater than high score");
        } else if (numPlayers < 0) {
            throw new RuntimeException("Invalid number of players");
        } else if (diff == null) {
            throw new RuntimeException("Difficulty can not be null");
        }

        // update variables
        this.theme = theme;
        this.low = low;
        this.high = high;
        this.numPlayers = numPlayers;
        this.difficulty = diff;
        this.rankBoundaries = new double[MAX_ACHIEVEMENT_RANK - 1];
        this.achievementNames = changeAchievementNames(theme);

        // Initialize boundaries for different achievements
        initializeRankBoundaries(low, high, numPlayers, diff);
    }

    // todo test
    private void initializeRankBoundaries(int low, int high, int numPlayers, Game.Difficulty difficulty) {
        // todo, repeated code?
        if (low > high) {
            throw new RuntimeException("Low score can not be greater than high score");
        } else if (numPlayers < 0) {
            throw new RuntimeException("Invalid number of players");
        }

        final double MULTIPLIER = getDifficultlyMultiplier(difficulty);

        double difference = (double) (high - low) / (MAX_ACHIEVEMENT_RANK - 2);
        double d = low;
        for (int i = 0; i < rankBoundaries.length; i++) {
            rankBoundaries[i] = (d * numPlayers) * MULTIPLIER;
            d += difference;
        }
    }

    // Returns the appropriate multiplier for the given difficulty
    // ex. EASY -> 0.75
    private double getDifficultlyMultiplier(Game.Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return EASY_MULTIPLIER;
            case NORMAL:
                return NORMAL_MULTIPLIER;
            case HARD:
                return HARD_MULTIPLIER;
            default:
                throw new RuntimeException("Invalid multiplier: " + difficulty);
        }
    }

    // Change the difficulty of a given game
    public void changeDifficulty(Game.Difficulty difficulty) {
        // Ensure that rank boundaries were created correctly
        if (rankBoundaries == null || rankBoundaries.length != (MAX_ACHIEVEMENT_RANK - 1)) {
            throw new RuntimeException("Rank boundaries were not constructed properly");
        }

        // Update achievement ranks if game difficulty has changed
        if (this.difficulty != difficulty) {
            // Initialize boundaries again
            initializeRankBoundaries(low, high, numPlayers, difficulty);

            // Update game difficulty
            this.difficulty = difficulty;
        }
    }

    // Return the achievement name at a given rank
    public String getAchievementName(int gameRank) {
        if (gameRank < MIN_ACHIEVEMENT_RANK || gameRank > MAX_ACHIEVEMENT_RANK) {
            throw new ArrayIndexOutOfBoundsException("Rank of a game must be between: between " + MIN_ACHIEVEMENT_RANK + " and " + MAX_ACHIEVEMENT_RANK);
        }
        return achievementNames[gameRank - 1];
    }

    // Return the highest achievement rank index obtainable given the sum of player scores
    public int getHighestRank(int totalScore) {
        int maxRank = 1;
        for (int i = 0; i < rankBoundaries.length
                && totalScore > rankBoundaries[i]; i++) {
            maxRank++;
        }
        return maxRank;
    }

    // Print the Achievement at a given index
    public String getAchievementString(int rankIdx) {
        final int MIN_RANK_IDX = 0;                           // Min rank (index = 0)
        final int MAX_RANK_IDX = MAX_ACHIEVEMENT_RANK - 1;    // Max rank (index = 9)

        // Highest achievement rank
        if (rankIdx == MAX_RANK_IDX) {
            return "Level " + MAX_ACHIEVEMENT_RANK + " (>"
                    + (int) rankBoundaries[MAX_RANK_IDX - 1] + "): "
                    + achievementNames[MAX_RANK_IDX];
        }

        // Lowest achievement rank
        else if (rankIdx == MIN_RANK_IDX) {
            return "Level " + (rankIdx + 1) + " (<"
                    + (int) rankBoundaries[rankIdx] + "): "
                    + achievementNames[rankIdx];
        }

        // Other
        else {
            return "Level " + (rankIdx + 1) + " ("
                    + (int) rankBoundaries[rankIdx - 1] + " - "
                    + (int) rankBoundaries[rankIdx]
                    + "): " + achievementNames[rankIdx];
        }
    }

    //Changes the achievements names based on the theme 1-3 which are selected by the user.
    public String[] changeAchievementNames(int theme) {
        if (theme < 1 || theme > 3) {
            throw new IllegalArgumentException("The theme must be 1, 2, or 3");
        }
        String[] s;
        if (theme == 1) {
            s = new String[]{
                    "Horrible Hamburgers", "Terrible Tacos",
                    "Bad Broccoli's", "Alright Apples", "Mediocre Mangoes",
                    "Okay Oranges", "Great Grapes", "Superb Sausages",
                    "Awesome Avocados", "Excellent Eggs"
            };
        }
        else if (theme == 2) {
            s = new String[]{
                    "Nasty Neptunes", "Underwhelming Uranus'",
                    "Sucky Saturns", "Just enough Jupiters", "Moderate Mars'",
                    "Endearing Earths", "Vigorous Venuses", "Marvelous Mercury's",
                    "Precious Plutos", "Stunning Suns"
            };
        }
        else {
            s = new String[]{
                    "Revolting Reds", "Obnoxious Oranges",
                    "Yucky Yellows", "Good Enough Greens", "Not Bad Blues",
                    "Inferior Indigos", "Valuable Violets", "Pretty Pinks",
                    "Breathtaking Browns", "Gorgeous Greys"
            };
        }
        return s;
    }

    public String getTheme() {
        if (theme == 1) {
            return "one";
        }
        else if (theme == 2) {
            return "two";
        }
        else {
            return "three";
        }
    }

    public static void setTheme(int theme) {
        Achievement.theme = theme;
    }
}