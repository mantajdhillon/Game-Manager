package ca.cmpt276.neon_coopachievement.model;

public class Achievement {
    private static final byte MAX_ACHIEVEMENTS = 10;

    private double[] achievementBoundaries;
    private String[] achievementNames;

    public Achievement(int low, int high, int numPlayers) {
        if (low > high) {
            throw new RuntimeException("Low score can not be greater than high score");
        }
        achievementBoundaries = new double[MAX_ACHIEVEMENTS-1];

        double diff = (double) (high - low) / (MAX_ACHIEVEMENTS-2);
        double d = low;
        for (int i = 0; i < achievementBoundaries.length; i++) {
            achievementBoundaries[i] = d * numPlayers;
            d += diff;
            System.out.println("Tier " + (i+1) + ") " + achievementBoundaries[i]);
        }
        setAchievementNames();
    }

    private void setAchievementNames() {
        this.achievementNames = new String[]{
                "Achievement Level 1", "Achievement Level 2",
                "Achievement Level 3", "Achievement Level 4", "Achievement Level 5",
                "Achievement Level 6", "Achievement Level 7", "Achievement Level 8",
                "Achievement Level 9", "Achievement Level 10"
        };
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

}



