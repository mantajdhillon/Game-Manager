package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.neon_coopachievement.model.Achievement;
import ca.cmpt276.neon_coopachievement.model.Game;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

/**
 * GameActivity Class
 * <p>
 * - Displays the list of game of one game category.
 * - Allows user to add a new game.
 * - Allows user to edit the game category configuration
 *   by clicking pencil icon in top right.
 * - Allows user to view achievements of game category
 *   for valid number of players.
 */
public class GameActivity extends AppCompatActivity {
    // TODO REMOVE HARDCODED DIFFICULTY
    //  - ALLOW USER TO SELECT DIFFICULTY FOR AN INDIVIDUAL GAME
    private static final Game.Difficulty HARD_CODED_DIFFICULTY = Game.Difficulty.HARD;


    private static final String GAME_TYPE_INDEX = "Game-Type-Index";
    private static final GameCategory gameCategory = GameCategory.getInstance();

    private GameManager gameManager;

    List<GameActivity.GameListElement> listGames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Set up action bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        updateLaunchAchievementUI(View.INVISIBLE, false);

        gameManager = gameCategory.getGameManager(getGameManagerIndex());

        populateGamesList();
        generateGamesList();

        setUpEmptyState(gameManager.size());
        setupAddGameBtn();
        setupViewAchievementsBtn();
    }

    // Sets up UI elements to launch achievements page
    private void updateLaunchAchievementUI(int visibility, boolean isEnabled) {
        TextView numPlayersMsg = findViewById(R.id.numPlayersMsg);
        numPlayersMsg.setVisibility(visibility);

        EditText numPlayersInput = findViewById(R.id.numPlayersInput);
        numPlayersInput.setEnabled(isEnabled);
        numPlayersInput.setVisibility(visibility);

        Button goBtn = findViewById(R.id.gotoAchievements);
        goBtn.setEnabled(isEnabled);
        goBtn.setVisibility(visibility);
    }

    private void generateGamesList() {
        ArrayAdapter<GameListElement> adapter = new MyListAdapter();
        ListView playedGames = findViewById(R.id.gameList);
        playedGames.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Update title
        getSupportActionBar().setTitle(gameManager.getName());

        GameManager temp = null;
        try {
            temp = gameCategory.getGameManager(getGameManagerIndex());
        } catch (Exception e) {
            finish();
        }

        if (!gameManager.equals(temp)) {
            finish();
        } else {
            populateGamesList();
            generateGamesList();
            setUpEmptyState(gameManager.size());
            setupAddGameBtn();
            setupViewAchievementsBtn();
        }

        // Save game manager
        CategoryActivity.saveCategoryState();
    }

    public static Intent makeIntent(Context c, int index) {
        Intent intent = new Intent(c, GameActivity.class);
        intent.putExtra(GAME_TYPE_INDEX, index);
        return intent;
    }

    private void setUpEmptyState(int numGames) {
        ImageView emptyStateIcon = findViewById(R.id.ivEmptyStateGameActivity);
        TextView emptyStateDesc = findViewById(R.id.tvEmptyStateDescGameActivity);

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
        newGame.setOnClickListener(v -> {
            Intent i = GameConfigActivity.makeIntent(GameActivity.this, false, -1, getGameManagerIndex());
            startActivity(i);
        });
    }

    private void setupViewAchievementsBtn() {
        Button viewAchievements = findViewById(R.id.viewAchievementsBtn);
        viewAchievements.setOnClickListener(v -> {
            viewAchievements.setEnabled(false);
            viewAchievements.setVisibility(View.INVISIBLE);

            // Disable floating action bar
            FloatingActionButton newGame = findViewById(R.id.addGameBtn);
            newGame.setEnabled(false);
            newGame.setVisibility(View.INVISIBLE);

            updateLaunchAchievementUI(View.VISIBLE, true);
            setupGoAchievementsBtn();
        });
    }

    private void setupGoAchievementsBtn() {
        Button goBtn = findViewById(R.id.gotoAchievements);
        goBtn.setOnClickListener(v -> setupAchievementsBtn(goBtn));
    }

    private void setupAchievementsBtn(Button goBtn) {
        EditText etNumPlayers = findViewById(R.id.numPlayersInput);
        String strNumPlayers = etNumPlayers.getText().toString().trim();
        if (!TextUtils.isEmpty(strNumPlayers)) {
            try {
                int numPlayers = Integer.parseInt(strNumPlayers);
                if (numPlayers <= 0) {
                    Toast.makeText(GameActivity.this, R.string.invalid_num_players_msg, Toast.LENGTH_SHORT).show();
                } else {
                    hideButtonAndText(goBtn, etNumPlayers);

                    GameManager gameManager = gameCategory.getGameManager(getGameManagerIndex());

                    // TODO TEST
                    Intent i = AchievementActivity.makeIntent(GameActivity.this,
                            numPlayers, gameManager.getPoorScoreIndividual(), gameManager.getGreatScoreIndividual(),
                            HARD_CODED_DIFFICULTY);     // FIXME remove hardcoded difficulty

                    startActivity(i);
                }
            } catch (Exception e) {
                Toast.makeText(GameActivity.this, R.string.invalid_num_players_msg, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(GameActivity.this, R.string.invalid_num_players_msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void hideButtonAndText(Button goBtn, EditText etNumPlayers) {
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
    }

    private int getGameManagerIndex() {
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
                Intent i2 = CategoryConfigActivity.makeIntent(GameActivity.this, true, getGameManagerIndex());
                startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MyListAdapter extends ArrayAdapter<GameActivity.GameListElement> {
        public MyListAdapter() {
            super(GameActivity.this, R.layout.complex_listview_layout, listGames);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View gameView = convertView;
            if (gameView == null) {
                gameView = getLayoutInflater().inflate
                        (R.layout.complex_listview_layout, parent, false);
            }

            gameView.setOnClickListener(v -> {
                Intent i = GameConfigActivity.makeIntent(GameActivity.this, true, position, getGameManagerIndex());
                startActivity(i);
            });

            GameActivity.GameListElement currentElement = listGames.get(position);

            ImageView icon = gameView.findViewById(R.id.achievement_icon);
            icon.setImageResource(currentElement.iconId);

            TextView description = gameView.findViewById(R.id.tvAchievementsDescription);
            description.setText(currentElement.description);

            return gameView;
        }
    }

    private static class GameListElement {
        public String description;
        public int iconId;

        public GameListElement(String description, int iconId) {
            this.description = description;
            this.iconId = iconId;
        }
    }

    private void populateGamesList() {
        listGames.clear();
        for (int i = 0; i < gameManager.size(); i++) {
            Game g = gameManager.getGame(i);
            String filename = g.getAchievementTheme() + getString(R.string.IconFileName) + g.getRank();
            int id = getResources().getIdentifier(filename, getString(R.string.defType), this.getPackageName());
            listGames.add(new GameListElement(g.toString(), id));
        }
    }
}
