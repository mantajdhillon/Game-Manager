package ca.cmpt276.neon_coopachievement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.neon_coopachievement.model.Game;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

/**
 * GameActivity Class
 * <p>
 * - Displays the list of game of one game category.
 * <p>
 * - Allows user to add a new game.
 * <p>
 * - Allows user to edit the game category configuration
 * by clicking pencil icon in top right.
 * <p>
 * - Allows user to view achievements of game category
 * for valid number of players.
 */
public class GameActivity extends AppCompatActivity {
    private static final String EXTRA_GAME_TYPE_INDEX = "Game-Type-Index";
    private static final GameCategory gameCategory = GameCategory.getInstance();

    private GameManager gameManager;

    List<GameActivity.GameListElement> listGames = new ArrayList<>();

    private int numPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Set up action bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        gameManager = gameCategory.getGameManager(getGameManagerIndex());

        populateGamesList();
        generateGamesList();

        setUpEmptyState(gameManager.getSize());
        setupAddGameBtn();
        setupViewAchievementsBtn();
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
            setUpEmptyState(gameManager.getSize());
            setupAddGameBtn();
            setupViewAchievementsBtn();
        }

        // Save game manager
        CategoryActivity.saveCategoryState();
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

    private void generateGamesList() {
        ArrayAdapter<GameListElement> adapter = new MyListAdapter();
        ListView playedGames = findViewById(R.id.gameList);
        playedGames.setAdapter(adapter);
    }

    public static Intent makeIntent(Context c, int index) {
        Intent intent = new Intent(c, GameActivity.class);
        intent.putExtra(EXTRA_GAME_TYPE_INDEX, index);
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
            AlertDialog.Builder playerDialog = new AlertDialog.Builder(GameActivity.this);
            playerDialog.setTitle(R.string.number_of_players);

            final EditText etNumPlayers = new EditText(GameActivity.this);
            etNumPlayers.setInputType(InputType.TYPE_CLASS_NUMBER);
            playerDialog.setView(etNumPlayers);

            playerDialog.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                try {
                    numPlayers = Integer.parseInt(etNumPlayers.getText().toString().trim());
                    if (numPlayers < 0) {
                        Toast.makeText(GameActivity.this, R.string.invalid_num_players_msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = GameConfigActivity.makeIntent(GameActivity.this, false, -1, getGameManagerIndex(), numPlayers);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    Toast.makeText(GameActivity.this, R.string.invalid_num_players_msg, Toast.LENGTH_SHORT).show();
                }
            });

            playerDialog.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

            playerDialog.show();
        });
    }

    private void setupViewAchievementsBtn() {
        Button viewAchievements = findViewById(R.id.viewAchievementsBtn);
        viewAchievements.setOnClickListener(v -> {

            AlertDialog.Builder achievementDialog = new AlertDialog.Builder(GameActivity.this);
            achievementDialog.setTitle(R.string.number_of_players);

            final EditText etNumPlayers = new EditText(GameActivity.this);
            etNumPlayers.setInputType(InputType.TYPE_CLASS_NUMBER);
            achievementDialog.setView(etNumPlayers);

            achievementDialog.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                try {
                    numPlayers = Integer.parseInt(etNumPlayers.getText().toString().trim());
                    if (numPlayers <= 0) {
                        Toast.makeText(GameActivity.this, R.string.invalid_num_players_msg, Toast.LENGTH_SHORT).show();
                    } else {

                        GameManager gameManager = gameCategory.getGameManager(getGameManagerIndex());

                        Intent i = AchievementActivity.makeIntent(GameActivity.this,
                                numPlayers, gameManager.getPoorScoreIndividual(), gameManager.getGreatScoreIndividual());

                        startActivity(i);
                    }
                } catch (Exception e) {
                    Toast.makeText(GameActivity.this, R.string.invalid_num_players_msg, Toast.LENGTH_SHORT).show();
                }
            });

            achievementDialog.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

            achievementDialog.show();
        });
    }

    private int getGameManagerIndex() {
        Intent intent = getIntent();
        return intent.getIntExtra(EXTRA_GAME_TYPE_INDEX, -1);
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
                Intent i = GameConfigActivity.makeIntent(GameActivity.this, true, position, getGameManagerIndex(), 0);
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
        for (int i = 0; i < gameManager.getSize(); i++) {
            Game g = gameManager.getGame(i);
            String filename = GameCategory.getInstance().getCurrentTheme() + getString(R.string.IconFileName) + g.getRank();
            g.updateAchievements(g.getDifficulty());
            int id = getResources().getIdentifier(filename, getString(R.string.defType), this.getPackageName());
            listGames.add(new GameListElement(g.toString(), id));
        }
    }
}
