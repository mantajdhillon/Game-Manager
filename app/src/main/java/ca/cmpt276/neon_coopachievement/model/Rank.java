package ca.cmpt276.neon_coopachievement.model;

public class Rank {

    private static final int NUM_RANKS = 8;

    private int achievementLevel;
    private String[] achievements;

    public Rank(int rank) {
        if (rank < 1 || rank > 8) {
            throw new IllegalArgumentException
                    ("The achievement level must be between 1 and 8 (inclusive");
        }
        this.achievementLevel = rank;
        setAchievementNames();
    }

    private void setAchievementNames() {
        this.achievements = new String[]{
                "Achievement Level 1", "Achievement Level 2", "Achievement Level 3",
                "Achievement Level 4", "Achievement Level 5", "Achievement Level 6",
                "Achievement Level 7", "Achievement Level 8"
        };
    }

    public void setAchievementLevel(int achievementLevel) {
        this.achievementLevel = achievementLevel;
    }

    public int getAchievementLevel() {
        return achievementLevel;
    }

    public String getAchievements(int i) {
        return achievements[i];
    }

    public String getAchievementName() {
        return getAchievements(achievementLevel - 1);
    }
}
