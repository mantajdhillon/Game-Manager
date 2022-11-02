package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GameActivity extends AppCompatActivity {

    public static final String ACTIVITY_TITLE = "Games";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        TextView numPlayersMsg = findViewById(R.id.numPlayersMsg);
        numPlayersMsg.setVisibility(View.INVISIBLE);
        EditText numPlayersInput = findViewById(R.id.numPlayersInput);
        numPlayersInput.setEnabled(false);
        numPlayersInput.setVisibility(View.INVISIBLE);
        Button goBtn = findViewById(R.id.gotoAchievements);
        goBtn.setEnabled(false);
        goBtn.setVisibility(View.INVISIBLE);


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
                viewAchievements.setEnabled(false);
                viewAchievements.setVisibility(View.INVISIBLE);

                FloatingActionButton newGame = findViewById(R.id.addGameBtn);
                newGame.setEnabled(false);

                TextView numPlayersMsg = findViewById(R.id.numPlayersMsg);
                numPlayersMsg.setVisibility(View.VISIBLE);

                EditText numPlayersInput = findViewById(R.id.numPlayersInput);
                numPlayersInput.setEnabled(true);
                numPlayersInput.setVisibility(View.VISIBLE);

                Button goBtn = findViewById(R.id.gotoAchievements);
                goBtn.setEnabled(true);
                goBtn.setVisibility(View.VISIBLE);
                setupGoAchievementsBtn();

            }
        });
    }
    private void setupGoAchievementsBtn() {
        Button goBtn = findViewById(R.id.gotoAchievements);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etNumPlayers = findViewById(R.id.numPlayersInput);
                String strNumPlayers = etNumPlayers.getText().toString().trim();
                if (!TextUtils.isEmpty(strNumPlayers)) {
                    int numPlayers = Integer.parseInt(strNumPlayers);
                    if (numPlayers <= 1) {
                        Toast.makeText(GameActivity.this, R.string.invalid_num_players_msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Button viewAchievements = findViewById(R.id.viewAchievementsBtn);
                        viewAchievements.setEnabled(true);
                        viewAchievements.setVisibility(View.VISIBLE);
                        FloatingActionButton newGame = findViewById(R.id.addGameBtn);
                        newGame.setEnabled(true);
                        TextView numPlayersMsg = findViewById(R.id.numPlayersMsg);
                        numPlayersMsg.setVisibility(View.INVISIBLE);
                        etNumPlayers.setEnabled(false);
                        etNumPlayers.setVisibility(View.INVISIBLE);
                        goBtn.setEnabled(false);
                        goBtn.setVisibility(View.INVISIBLE);

                        Toast.makeText(GameActivity.this, "Number of players is " + numPlayers, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(GameActivity.this, AchievementActivity.class);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(GameActivity.this, R.string.invalid_num_players_msg, Toast.LENGTH_SHORT).show();
                }
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