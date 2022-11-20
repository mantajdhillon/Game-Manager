package ca.cmpt276.neon_coopachievement.model;

import androidx.annotation.NonNull;

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
