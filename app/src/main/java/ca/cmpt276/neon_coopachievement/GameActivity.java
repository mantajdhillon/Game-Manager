package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

public class GameActivity extends AppCompatActivity {

    private static final String GAME_TYPE_INDEX = "Game-Type-Index";
    private static GameCategory gameCategory = GameCategory.getInstance();


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

        GameManager gameManager = gameCategory.getGameManager(getGameIndex());

        generateGamesList(gameManager);

        setUpEmptyState(gameManager.getGamesStored());
        setupAddGameBtn();
        setupViewAchievementsBtn();
        gameClickCallback();
    }

    private void generateGamesList(GameManager gameManager) {
        String[] games = getGameStrings(gameManager);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.items,
                games);

        ListView playedGames = findViewById(R.id.gameList);
        playedGames.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameManager gameManager = gameCategory.getGameManager(getGameIndex());

        generateGamesList(gameManager);
        setUpEmptyState(gameManager.getGamesStored());
        setupAddGameBtn();
        setupViewAchievementsBtn();
        gameClickCallback();

    }

    public static Intent makeLaunchIntent(Context c, int index) {
        Intent intent = new Intent(c, GameActivity.class);
        intent.putExtra(GAME_TYPE_INDEX, index);
        return intent;
    }

    private String[] getGameStrings(GameManager gM) {
        String[] s = new String[gM.getGamesStored()];
        for (int i = 0; i < s.length; i++) {
            s[i] = gM.getGameString(i);
        }
        return s;
    }

    // Configure the empty state when there are no more games
    private void setUpEmptyState(int numGames) {
        ImageView emptyStateIcon = findViewById(R.id.ivEmptyStateGameActivity);
        TextView emptyStateDesc = findViewById(R.id.tvEmptyStateDescGameActivity);

        // Display only if the category manager is 0
        if (numGames == 0) {
            emptyStateIcon.setVisibility(View.VISIBLE);
            emptyStateDesc.setVisibility(View.VISIBLE);
        } else {
            emptyStateIcon.setVisibility(View.INVISIBLE);
            emptyStateDesc.setVisibility(View.INVISIBLE);
        }
    }

    private void setupAddGameBtn() {
        FloatingActionButton newGame = findViewById(R.id.addGameBtn);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = GameConfigActivity.makeLaunchIntent(GameActivity.this, getGameIndex());
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
                    if (numPlayers <= 0) {
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

                        GameManager gameManager = gameCategory.getGameManager(getGameIndex());



                        Toast.makeText(GameActivity.this, "Number of players is " + numPlayers, Toast.LENGTH_SHORT).show();

                        Intent i = AchievementActivity.makeLaunchIntent(GameActivity.this,
                                numPlayers, gameManager.getPoorScoreIndividual(), gameManager.getGreatScoreIndividual());
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

                Intent i = GameConfigActivity.makeLaunchIntent(GameActivity.this, getGameIndex());
                startActivity(i);
            }
        });
    }

    private int getGameIndex() {
        Intent intent = getIntent();
        return intent.getIntExtra(GAME_TYPE_INDEX, -1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_help:
                Intent i1 = new Intent(GameActivity.this, HelpActivity.class);
                startActivity(i1);
                return true;
            case R.id.action_edit_category:
                // TO-DO: replace with gameCategory info
                Intent i2 = CategoryConfigActivity.makeCategoryConfigIntent(GameActivity.this, true,getGameIndex());
                startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
