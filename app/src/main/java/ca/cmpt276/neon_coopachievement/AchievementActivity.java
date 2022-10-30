package ca.cmpt276.neon_coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AchievementActivity extends AppCompatActivity {

    public static final String ACTIVITY_TITLE = "Achievements";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(ACTIVITY_TITLE);
        ab.setDisplayHomeAsUpEnabled(true);

        String[] ranks = {"Goofy Goblins", "Majestic Unicorns", "Fabulous Dragons", "Brilliant Pixies"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.items,
                ranks);

        ListView achievements = findViewById(R.id.achievementList);
        achievements.setAdapter(adapter);
    }
}