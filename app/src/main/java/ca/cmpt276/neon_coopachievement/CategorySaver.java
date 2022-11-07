package ca.cmpt276.neon_coopachievement;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import ca.cmpt276.neon_coopachievement.model.GameCategory;

public class CategorySaver {

    private static final String KEY = "GAME_CATEGORY_JSON_KEY";
//    private static final String INVALID_PARSE_VAL = "FAILED_TO_PARSE";
//    private static final String INVALID_PARSE_VAL = "";

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

        Log.i("GameCategorySaver Constructor", "Game category was constructed. GameCategorySaver(Context context) successful");
    }

    // Retrieve game category JSON from SharedPreference
    private void getSharedPreferenceData() {
        String gameCategoryJson = preference.getString(KEY, "");

        // Update object if gameCategory was successfully parsed
        if (!gameCategoryJson.isEmpty()) {
            gameCategory = gson.fromJson(gameCategoryJson, GameCategory.class);
            gameCategory.setInstance();
        }
        Log.i("Get Shared Preference Data", "Game category was put in SharedPreferences. getSharedPreferenceData() successful");
    }

    // Serialize game category (store it as a json)
    public void saveData() {
        String gameCategoryJson = gson.toJson(gameCategory);

        // Store in SharedPreference
        editor.putString(KEY, gameCategoryJson);
        editor.apply();
        Log.i("Save Data", "Game category was saved. saveData() successful");
    }
}
