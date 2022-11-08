package ca.cmpt276.neon_coopachievement;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import ca.cmpt276.neon_coopachievement.model.GameCategory;

public class CategorySaver {

    private static final String KEY = "GAME_CATEGORY_JSON_KEY";
    private static final String INVALID_PARSE_VAL = "";

    private GameCategory gameCategory;
    private final SharedPreferences preference;
    private final SharedPreferences.Editor editor;
    private final Gson gson;

    public CategorySaver(Context context) {
        this.gameCategory = GameCategory.getInstance();
        this.gson = new Gson();
        this.preference = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        this.editor = preference.edit();

        getSharedPreferenceData();
    }

    // Retrieve game category JSON from SharedPreference
    private void getSharedPreferenceData() {
        String gameCategoryJson = preference.getString(KEY, INVALID_PARSE_VAL);

        // Update object if gameCategory was successfully parsed
        if (!gameCategoryJson.equals(INVALID_PARSE_VAL)) {
            gameCategory = gson.fromJson(gameCategoryJson, GameCategory.class);
            gameCategory.setInstance();
        }
    }

    // Serialize game category (store it as a json)
    public void saveData() {
        String gameCategoryJson = gson.toJson(gameCategory);

        // Store in SharedPreference
        editor.putString(KEY, gameCategoryJson);
        editor.apply();
    }
}
