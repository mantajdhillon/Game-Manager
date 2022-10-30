package ca.cmpt276.neon_coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GameActivity extends AppCompatActivity {

    public static final String ACTIVITY_TITLE = "Games";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(ACTIVITY_TITLE);
        ab.setDisplayHomeAsUpEnabled(true);

        String[] games = {"Game 1", "Game 2", "Game 3"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.items,
                games);

        ListView playedGames = findViewById(R.id.gameList);
        playedGames.setAdapter(adapter);

        setupAddGameBtn();
        setupViewAchievementsBtn();
        gameClickCallback();
    }

    private void setupAddGameBtn() {
        FloatingActionButton newGame = findViewById(R.id.addGameBtn);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameActivity.this, GameConfigActivity.class);
                startActivity(i);
            }
        });
    }

    private void setupViewAchievementsBtn() {
        Button viewAchievements = findViewById(R.id.viewAchievementsBtn);
        viewAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameActivity.this, AchievementActivity.class);
                startActivity(i);
            }
        });
    }

    private void gameClickCallback() {
        ListView games = findViewById(R.id.gameList);
        games.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                TextView game = (TextView) viewClicked;

                Intent i = new Intent(GameActivity.this, GameConfigActivity.class);
                startActivity(i);
            }
        });
    }
}