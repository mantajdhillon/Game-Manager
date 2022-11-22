package ca.cmpt276.neon_coopachievement.model;

import androidx.annotation.NonNull;

/**
 * Theme Enum Class
 * <p>
 * - Holds different enums which are used to determine the Theme choice of the Game Category
 */
public enum Theme {
    ONE("one"), TWO("two"), THREE("three");
    private final String numberString;

    Theme(String string) {
        this.numberString = string;
    }

    @NonNull
    @Override
    public String toString() {
        return numberString;
    }
}
