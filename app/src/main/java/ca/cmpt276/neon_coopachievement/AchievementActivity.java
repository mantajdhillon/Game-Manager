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
import ca.cmpt276.neon_coopachievement.model.GameCategory;

/*
    AchievementActivity Class
    - Displays the list of achievements of a game based on
      the number of players the user enters.
    - Accessed through GameActivity.
 */
public class AchievementActivity extends AppCompatActivity {

    public static final String NUM_PLAYERS = "numPlayers";
    public static final String GOOD_SCORE = "goodScore";
    public static final String POOR_SCORE = "poorScore";
    public static final int MAX_ACHIEVEMENTS = 10;


    List<AchievementListElement> listAchievements = new ArrayList<AchievementListElement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);


        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.achievement_activity_title);
        ab.setDisplayHomeAsUpEnabled(true);

        populateAchievementsList();
        populateListView();

    }


    private void populateAchievementsList() {
        Achievement achievements = new Achievement(getPoorScore(), getGoodScore(), getNumPlayers());
        for (int i = 0; i < MAX_ACHIEVEMENTS; i++) {
            String filename = getString(R.string.IconFileName) + (i+1);
            int id = getResources().getIdentifier(filename, getString(R.string.defType), this.getPackageName());
            listAchievements.add(new AchievementListElement
                     (achievements.getAchievementString(i), id));
        }
    }

    private void populateListView() {
        ArrayAdapter<AchievementListElement> adapter = new MyListAdapter();
        ListView list = findViewById(R.id.achievementList);
        list.setAdapter(adapter);
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
                Intent i = new Intent(AchievementActivity.this, HelpActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent makeLaunchIntent(Context c, int numPlayers, int poorScore, int goodScore) {
        Intent intent = new Intent(c, AchievementActivity.class);
        intent.putExtra(NUM_PLAYERS, numPlayers);
        intent.putExtra(GOOD_SCORE, goodScore);
        intent.putExtra(POOR_SCORE, poorScore);
        return intent;
    }

    private int getNumPlayers() {
        Intent intent = getIntent();
        return intent.getIntExtra(NUM_PLAYERS, -1);
    }
    private int getPoorScore() {
        Intent intent = getIntent();
        return intent.getIntExtra(POOR_SCORE, -1);
    }
    private int getGoodScore() {
        Intent intent = getIntent();
        return intent.getIntExtra(GOOD_SCORE, -1);
    }

    private class AchievementListElement {
        public String description;
        public int iconId;

        public AchievementListElement(String description, int iconId) {
            this.description = description;
            this.iconId = iconId;
        }
    }

    private class MyListAdapter extends ArrayAdapter<AchievementListElement> {
        public MyListAdapter() {
            super(AchievementActivity.this, R.layout.achievements_list, listAchievements);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View achievementView = convertView;
            if (achievementView == null) {
                achievementView = getLayoutInflater().inflate
                        (R.layout.achievements_list, parent, false);
            }

            AchievementListElement currentElement= listAchievements.get(position);

            ImageView icon = achievementView.findViewById(R.id.achievement_icon);
            icon.setImageResource(currentElement.iconId);

            TextView description = achievementView.findViewById(R.id.tvAchievementsDescription);
            description.setText(currentElement.description);

            return achievementView;
        }
    }
}