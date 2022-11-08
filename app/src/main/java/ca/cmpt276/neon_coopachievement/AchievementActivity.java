package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.neon_coopachievement.model.Achievement;

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

    private final List<AchievementListElement> achievementList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        // Set up Action bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.achievement_activity_title);
        ab.setDisplayHomeAsUpEnabled(true);

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

    private int getNumPlayers() {
        return getIntent().getIntExtra(EXTRA_NUM_PLAYERS, -1);
    }

    private int getPoorScore() {
        return getIntent().getIntExtra(EXTRA_POOR_SCORE, -1);
    }

    private int getGoodScore() {
        return getIntent().getIntExtra(EXTRA_GOOD_SCORE, -1);
    }

    private void populateAchievementsList() {
        Achievement achievements = new Achievement(getPoorScore(), getGoodScore(), getNumPlayers());

        for (int i = 0; i < MAX_ACHIEVEMENTS; i++) {
            String filename = getString(R.string.IconFileName) + (i + 1);
            int id = getResources().getIdentifier(filename, getString(R.string.defType), this.getPackageName());
            achievementList.add(new AchievementListElement(achievements.getAchievementString(i), id));
        }
    }

    private void populateListView() {
        ArrayAdapter<AchievementListElement> adapter = new AchievementListAdapter();
        ListView list = findViewById(R.id.achievementList);
        list.setAdapter(adapter);
    }

    private static class AchievementListElement {
        public String description;
        public int iconId;

        public AchievementListElement(String description, int iconId) {
            this.description = description;
            this.iconId = iconId;
        }
    }

    private class AchievementListAdapter extends ArrayAdapter<AchievementListElement> {
        public AchievementListAdapter() {
            super(AchievementActivity.this, R.layout.achievements_list, achievementList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View achievementView = convertView;
            if (achievementView == null) {
                achievementView = getLayoutInflater().inflate
                        (R.layout.achievements_list, parent, false);
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