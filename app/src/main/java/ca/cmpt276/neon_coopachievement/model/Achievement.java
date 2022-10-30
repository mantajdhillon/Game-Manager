package ca.cmpt276.neon_coopachievement.model;

public class Achievement {
    private static final byte MAX_ACHIEVEMENTS = 10;

    private double[] achievementBoundaries;
    private String[] achievementNames;

    public Achievement(int low, int high, int numPlayers) {
        if (low > high) {
            throw new RuntimeException("Low score can not be greater than high score");
        }
        double diff = (double) (high - low) / MAX_ACHIEVEMENTS;
        double d = low;
        achievementBoundaries = new double[MAX_ACHIEVEMENTS];
        for (int i = 0; i < MAX_ACHIEVEMENTS; i++) {
            d += diff;
            achievementBoundaries[i] = d;
            System.out.println("Tier " + (i + 1) + ") " + d + " * " + numPlayers + " = " + d * numPlayers);
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

    public double[] getAchievementBoundaries() {
        return achievementBoundaries;
    }

    public String getAchievementName(int i) {
        return achievementNames[i];
    }

}


