package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}