package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ca.cmpt276.neon_coopachievement.model.Achievement;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

public class AchievementActivity extends AppCompatActivity {

    public static final String NUM_PLAYERS = "numPlayers";
    public static final String GOOD_SCORE = "good score";
    public static final String POOR_SCORE = "poor score";
    public static final int MAX_ACHIEVEMENTS = 10;

    GameCategory gameCategory = GameCategory.getInstance();

    Achievement achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);


        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.achievement_activity_title);
        ab.setDisplayHomeAsUpEnabled(true);


        achievements = new Achievement(getPoorScore(), getGoodScore(), getNumPlayers());

        String[] ranks = new String[MAX_ACHIEVEMENTS];
        for (int i = 0; i < MAX_ACHIEVEMENTS; i++) {
            ranks[i] = achievements.getAchievementString(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.items,
                ranks);

        ListView achievements = findViewById(R.id.achievementList);
        achievements.setAdapter(adapter);
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
}