package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.neon_coopachievement.model.Achievement;
import ca.cmpt276.neon_coopachievement.model.Game;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

/**
 * GameConfigActivity Class
 * <p>
 * - Used for add/edit/delete game.
 * - A new game is created when user inputs a number
 *   of players and sum of players' scores, and clicks
 *   save.
 * - Editing mode displays the previous number of players and score that
 *   the user entered in the inputs fields.
 * - Details are updated when user changes the fields and clicks save.
 *   The user may delete the game by clicking delete.
 */
public class GameConfigActivity extends AppCompatActivity {

    // TODO REMOVE HARDCODED DIFFICULTY
    //  - ALLOW USER TO SELECT DIFFICULTY FOR AN INDIVIDUAL GAME
    private static final Game.Difficulty HARD_CODED_DIFFICULTY = Game.Difficulty.HARD;

    private static final String EXTRA_GAME_TYPE_INDEX = "Game-Type-Index";
    private static final String EXTRA_IS_EDIT = "isEdit";
    private static final String EXTRA_GAME_INDEX = "gameIndex";

    private GameManager gameManager;
    private Game currentGame;

    private EditText etNumPlayers;
    private EditText etSumScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config);

        // Set up action bar (add game title by default)
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.game_config_activity_add_game);
        ab.setDisplayHomeAsUpEnabled(true);

        // Get current game manager
        GameCategory gameCategory = GameCategory.getInstance();
        gameManager = gameCategory.getGameManager(getGameManagerIndex());

        // Set delete button to invisible
        Button deleteBtn = findViewById(R.id.btnDeleteGame);
        deleteBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setEnabled(false);

        setUpSaveBtn();
        setUpTextView();

        // Editing a game configuration
        if (getisEdit()) {
            currentGame = gameManager.getGame(getGameIndex());
            ab.setTitle(R.string.game_config_activity_edit_game);
            populateFields();
            populateAchievementView();
            setUpDeleteBtn();
        }
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
                Intent i = new Intent(GameConfigActivity.this, HelpActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpTextView() {
        etNumPlayers = findViewById(R.id.etNumPlayers);
        etNumPlayers.addTextChangedListener(inputWatcher);

        etSumScore = findViewById(R.id.etSumPlayerScores);
        etSumScore.addTextChangedListener(inputWatcher);
    }

    private void setUpSaveBtn() {
        Button saveBtn = findViewById(R.id.btnSaveGame);

        saveBtn.setOnClickListener(view -> {

            try {
                int numPlayers = getInt(etNumPlayers);
                int sumScores = getInt(etSumScore);

                if (getisEdit()) {
                    currentGame.setNumPlayers(numPlayers);
                    currentGame.setFinalTotalScore(sumScores);
                    gameManager.updateEdits
                            (gameManager.getPoorScoreIndividual(), gameManager.getGreatScoreIndividual());
                }

                // Make a new game
                else {
                    Game newGame = new Game(numPlayers, sumScores,
                            gameManager.getPoorScoreIndividual(),
                            gameManager.getGreatScoreIndividual(),
                            HARD_CODED_DIFFICULTY);     // FIXME remove hardcoded difficulty
                    gameManager.addGame(newGame);
                }
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateFields() {
        EditText etNumPlayers = findViewById((R.id.etNumPlayers));
        etNumPlayers.setText(Integer.toString(currentGame.getNumPlayers()));

        EditText etSumScore = findViewById((R.id.etSumPlayerScores));
        etSumScore.setText(Integer.toString(currentGame.getFinalTotalScore()));
    }

    private void populateAchievementView() {
        String strNumPlayers = etNumPlayers.getText().toString().trim();
        String strSumScore = etSumScore.getText().toString().trim();

        TextView tvAchieveGenerator = findViewById(R.id.tvAchieveGenerator);

        if (!strNumPlayers.isEmpty() && !strSumScore.isEmpty()) {
            Achievement achievements = new Achievement(
                    gameManager.getPoorScoreIndividual(),
                    gameManager.getGreatScoreIndividual(),
                    Integer.parseInt(strNumPlayers),
                    HARD_CODED_DIFFICULTY);     // FIXME remove hardcoded difficulty

            int rank = achievements.getHighestRank(Integer.parseInt(strSumScore));
            String rankName = achievements.getAchievementName(rank);

            tvAchieveGenerator.setText(rankName);
        } else {
            tvAchieveGenerator.setText("");
        }
    }

    private void setUpDeleteBtn() {
        Button deleteBtn = findViewById(R.id.btnDeleteGame);
        deleteBtn.setVisibility(View.VISIBLE);
        deleteBtn.setEnabled(true);
        deleteBtn.setOnClickListener(v -> {
            gameManager.removeGame(getGameIndex());
            finish();
        });
    }

    public static Intent makeIntent(Context c, boolean isEdit, int gameIndex, int gameManagerIndex) {
        Intent intent = new Intent(c, GameConfigActivity.class);
        intent.putExtra(EXTRA_IS_EDIT, isEdit);
        intent.putExtra(EXTRA_GAME_INDEX, gameIndex);
        intent.putExtra(EXTRA_GAME_TYPE_INDEX, gameManagerIndex);
        return intent;
    }

    private int getGameManagerIndex() {
        return getIntent().getIntExtra(EXTRA_GAME_TYPE_INDEX, -1);
    }

    private int getGameIndex() {
        return getIntent().getIntExtra(EXTRA_GAME_INDEX, -1);
    }

    private boolean getisEdit() {
        return getIntent().getBooleanExtra(EXTRA_IS_EDIT, false);
    }

    private int getInt(EditText et) {
        String intStr = et.getText().toString();
        return Integer.parseInt(intStr);
    }

    private final TextWatcher inputWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                populateAchievementView();
            } catch (Exception e) {
                Toast.makeText(GameConfigActivity.this, "Not a valid integer", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}