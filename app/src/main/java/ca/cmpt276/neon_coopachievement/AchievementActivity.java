package ca.cmpt276.neon_coopachievement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.neon_coopachievement.model.Achievement;
import ca.cmpt276.neon_coopachievement.model.Game;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

/**
 * AchievementActivity Class
 * <p>
 * - Displays the list of achievements of a game based on the number of players the user enters.
 * - Accessed through GameActivity.
 */
public class AchievementActivity extends AppCompatActivity {
    private static final byte MAX_ACHIEVEMENTS = 10;
    private static final String EXTRA_NUM_PLAYERS = "numPlayers";
    private static final String EXTRA_GOOD_SCORE = "goodScore";
    private static final String EXTRA_POOR_SCORE = "poorScore";

    private GameCategory instance = GameCategory.getInstance();

    // Represents an achievement list element consisting of the image io and description
    private static class AchievementListElement {
        public String description;
        public int iconId;

        public AchievementListElement(String description, int iconId) {
            this.description = description;
            this.iconId = iconId;
        }
    }

    private List<AchievementListElement> achievementList = new ArrayList<>();
    private Game.Difficulty currentDifficulty = Game.Difficulty.NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        // Set up Action bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.achievement_activity_title);
        ab.setDisplayHomeAsUpEnabled(true);

        setupRadioGroup();
        populateAchievementsList();
        populateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_help:
                Intent i = new Intent(this, HelpActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent makeIntent(Context c, int numPlayers, int poorScore, int goodScore) {
        Intent intent = new Intent(c, AchievementActivity.class);
        intent.putExtra(EXTRA_NUM_PLAYERS, numPlayers);
        intent.putExtra(EXTRA_GOOD_SCORE, goodScore);
        intent.putExtra(EXTRA_POOR_SCORE, poorScore);
        return intent;
    }

    public static Intent makeIndexIntent(Context c, int index){
        Intent intent = new Intent(c, AchievementActivity.class);
        intent.putExtra("index", index);
        return intent;
    }

    private GameManager getGameManager(){
        int index = getIntent().getIntExtra("index", -1);
        return instance.getGameManager(index);
    }

    private int getNumPlayers() {
        return getIntent().getIntExtra(EXTRA_NUM_PLAYERS, -1);
    }

    private int getPoorScore() {
        return getIntent().getIntExtra(EXTRA_POOR_SCORE, -1);
    }

    private int getGoodScore() {
        return getIntent().getIntExtra(EXTRA_GOOD_SCORE, -1);
    }

    private void setupRadioGroup() {
        setRadioButtonListeners(R.id.radioAchieveDifficultyEasy, Game.Difficulty.EASY);
        setRadioButtonListeners(R.id.radioAchieveDifficultyNormal, Game.Difficulty.NORMAL);
        setRadioButtonListeners(R.id.radioAchieveDifficultyHard, Game.Difficulty.HARD);

    }

    private void setRadioButtonListeners(int btnId, Game.Difficulty difficulty) {
        RadioButton themeChoice = findViewById(btnId);
        if (currentDifficulty == difficulty) {
            themeChoice.setChecked(true);
        }
        themeChoice.setOnClickListener(v -> {
            currentDifficulty = difficulty;
            populateAchievementsList();
            populateListView();
        });
    }

    private void populateAchievementsList() {
//        Achievement achievements = new Achievement(getPoorScore(), getGoodScore(),
//                getNumPlayers(), currentDifficulty);
        Achievement achievements = new Achievement(getGameManager().getPoorScoreIndividual(),
                getGameManager().getGreatScoreIndividual(),
                getGameManager().getSize(),
                currentDifficulty);

        // Ensure list is empty before populating
        achievementList = new ArrayList<>();

        for (int i = 0; i < MAX_ACHIEVEMENTS; i++) {
            String filename = GameCategory.getInstance().getCurrentTheme() + getString(R.string.IconFileName) + (i + 1);
            int id = getResources().getIdentifier(filename, getString(R.string.defType), this.getPackageName());
            achievementList.add(new AchievementListElement(achievements.getAchievementString(i), id));
        }
    }

    private void populateListView() {
        ArrayAdapter<AchievementListElement> adapter = new AchievementListAdapter();
        ListView list = findViewById(R.id.achievementList);
        list.setAdapter(adapter);
    }

    private class AchievementListAdapter extends ArrayAdapter<AchievementListElement> {
        public AchievementListAdapter() {
            super(AchievementActivity.this, R.layout.complex_listview_layout, achievementList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View achievementView = convertView;
            if (achievementView == null) {
                achievementView = getLayoutInflater().inflate
                        (R.layout.complex_listview_layout, parent, false);
            }

            AchievementListElement currentElement = achievementList.get(position);

            ImageView icon = achievementView.findViewById(R.id.achievement_icon);
            icon.setImageResource(currentElement.iconId);

            TextView description = achievementView.findViewById(R.id.tvAchievementsDescription);
            description.setText(currentElement.description);

            return achievementView;
        }
    }
}