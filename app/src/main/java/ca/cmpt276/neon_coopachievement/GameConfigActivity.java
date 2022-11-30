package ca.cmpt276.neon_coopachievement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ca.cmpt276.neon_coopachievement.model.Achievement;
import ca.cmpt276.neon_coopachievement.model.Game;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;
import ca.cmpt276.neon_coopachievement.model.ScoreCalculator;

/**
 * GameConfigActivity Class
 * <p>
 * - Used for add/edit/delete game.
 *
 * - A new game is created when user inputs a number
 *   of players. User may input the scores per player and calculate the total score
 *
 * - Editing mode displays the previous number of players and scores that
 *   the user entered in the inputs fields.
 *
 * - Details are updated when user changes the fields and clicks save.
 *
 * - The user may delete the game by clicking delete.
 *   Achievements tally is updated upon save
 */
public class GameConfigActivity extends AppCompatActivity {

    private Game.Difficulty currentDifficulty = Game.Difficulty.NORMAL;

    private static final String EXTRA_GAME_TYPE_INDEX = "Game-Type-Index";
    private static final String EXTRA_IS_EDIT = "isEdit";
    private static final String EXTRA_GAME_INDEX = "gameIndex";
    private static final String EXTRA_NUM_PLAYERS = "numPlayers";

    private GameManager gameManager;
    private Game currentGame;

    private final ScoreCalculator scoreCalculator = new ScoreCalculator();

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

        setUpGameConfigActivity();

        // Editing a game configuration
        if (getIsEdit()) {
            setUpGameConfigActivityEdit();
        }

        updateScreenUI();
        setUpDifficultyRadioButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // add delete game config
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_help:
                Intent i = new Intent(GameConfigActivity.this, HelpActivity.class);
                startActivity(i);
                return true;
            case R.id.action_delete:
                if (getIsEdit()) {
                    int rank = gameManager.getGame(getGameIndex()).getRank();
                    gameManager.removeGame(getGameIndex());
                    gameManager.decreaseTally(rank - 1);
                }
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Updates the screen UI (to reflect new changes)
    private void updateScreenUI() {
        populatePlayerListView();
        populateAchievementView();
        setUpEmptyState(scoreCalculator.getNumScores());
    }

    private void setUpGameConfigActivity() {
        // Set up buttons
        setUpAddPlayerBtn();
        setUpSaveBtn();
        setUpClearBtn();

        // Set up list items
        registerListClickCallback();
        setDefaultListSlots();
    }

    private void setUpGameConfigActivityEdit() {
        getSupportActionBar().setTitle(R.string.game_config_activity_edit_game);

        // Initialize variables necessary for editing
        this.currentGame = gameManager.getGame(getGameIndex());
        this.currentDifficulty = currentGame.getDifficulty();
        this.currentGame.updateAchievements(currentDifficulty);
        this.scoreCalculator.setScoreList(currentGame.getScores());
    }

    private void setUpEmptyState(int numGames) {
        ImageView emptyStateIcon = findViewById(R.id.ivEmptyStateGameConfigActivity);
        TextView emptyStateDesc = findViewById(R.id.tvEmptyStateDescGameConfigActivity);

        if (numGames == 0) {
            emptyStateIcon.setVisibility(View.VISIBLE);
            emptyStateDesc.setVisibility(View.VISIBLE);
        } else {
            emptyStateIcon.setVisibility(View.INVISIBLE);
            emptyStateDesc.setVisibility(View.INVISIBLE);
        }
    }

    private void setUpDifficultyRadioButtons() {
        setRadioButtonListeners(R.id.radioDifficultyEasy, Game.Difficulty.EASY);
        setRadioButtonListeners(R.id.radioDifficultyNormal, Game.Difficulty.NORMAL);
        setRadioButtonListeners(R.id.radioDifficultyHard, Game.Difficulty.HARD);
    }

    private void setRadioButtonListeners(int btnId, Game.Difficulty difficulty) {
        RadioButton themeChoice = findViewById(btnId);
        if (currentDifficulty == difficulty) {
            themeChoice.setChecked(true);
        }
        themeChoice.setOnClickListener(v -> {
            currentDifficulty = difficulty;
            populateAchievementView();
        });
    }

    private void setUpAddPlayerBtn() {
        FloatingActionButton newPlayer = findViewById(R.id.addPlayer);
        newPlayer.setOnClickListener(view -> {
            AlertDialog.Builder playerDialog = new AlertDialog.Builder(GameConfigActivity.this);
            playerDialog.setTitle(R.string.player_score_prompt);

            final EditText etPlayerScore = new EditText(GameConfigActivity.this);
            etPlayerScore.setInputType(InputType.TYPE_CLASS_NUMBER);
            playerDialog.setView(etPlayerScore);

            // Populate textview if there are any lost scores
            if (scoreCalculator.hasLostScore()) {
                etPlayerScore.setText(Integer.toString(scoreCalculator.peekLostScore()));
            }

            // Default value of 0 if no lost scores are found
            else {
                etPlayerScore.setText(R.string.zero);
            }

            // Confirm saving a player
            playerDialog.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                try {
                    scoreCalculator.addScore(Integer.parseInt(etPlayerScore.getText().toString().trim()));
                    scoreCalculator.popLostScore();     // Remove lost score
                } catch (Exception e) {
                    Toast.makeText(GameConfigActivity.this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
                }

                updateScreenUI();
            });

            playerDialog.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

            playerDialog.show();
        });
    }

    private void setDefaultListSlots() {
        ArrayList<Integer> preScoresList = new ArrayList<>();
        for (int i = 0; i < getNumPlayers(); i++) {
            preScoresList.add(0);
        }
        scoreCalculator.setScoreList(preScoresList);
    }

    private void populatePlayerListView() {
        ArrayList<String> players = new ArrayList<>();
        for (int i = 0; i < scoreCalculator.getNumScores(); i++) {
            players.add(scoreCalculator.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.items,
                players);

        ListView playersListView = findViewById(R.id.playersList);
        playersListView.setAdapter(adapter);
    }

    private void populateAchievementView() {
        int numPlayers = scoreCalculator.getNumScores();
        int sumScores = scoreCalculator.getSumScores();

        TextView tvAchieveGenerator = findViewById(R.id.tvAchieveGenerator);

        if (numPlayers != 0) {

            Achievement achievements = new Achievement(
                    gameManager.getPoorScoreIndividual(),
                    gameManager.getGreatScoreIndividual(),
                    numPlayers, currentDifficulty);

            int rank = achievements.getHighestRank(sumScores);
            String rankName = achievements.getAchievementName(rank);

            tvAchieveGenerator.setText(rankName);
        } else {
            tvAchieveGenerator.setText("");
        }
    }

    private void registerListClickCallback() {
        ListView playersListView = findViewById(R.id.playersList);
        playersListView.setOnItemClickListener((parent, view, position, id) -> {

            AlertDialog.Builder playerDialog = new AlertDialog.Builder(GameConfigActivity.this);
            playerDialog.setTitle(R.string.edit_player_score_prompt);

            final EditText etPlayerScore = new EditText(GameConfigActivity.this);
            etPlayerScore.setInputType(InputType.TYPE_CLASS_NUMBER);
            etPlayerScore.setText(Integer.toString(scoreCalculator.getScore(position)));
            playerDialog.setView(etPlayerScore);

            playerDialog.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                try {
                    scoreCalculator.updateScore(position, Integer.parseInt(etPlayerScore.getText().toString().trim()));
                } catch (Exception e) {
                    Toast.makeText(GameConfigActivity.this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
                }
                populatePlayerListView();
                populateAchievementView();
            });

            // Cancel
            playerDialog.setNeutralButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

            // Delete position
            playerDialog.setNegativeButton(R.string.delete_cap, (dialog, which) -> {
                scoreCalculator.removeScore(position);
                updateScreenUI();
            });

            playerDialog.show();
        });
    }

    private void setUpSaveBtn() {
        Button saveBtn = findViewById(R.id.btnSaveGame);

        saveBtn.setOnClickListener(view -> {

            int numPlayers = scoreCalculator.getNumScores();

            if (numPlayers != 0) {

                int sumScores = scoreCalculator.getSumScores();

                // Editing a game, update fields
                if (getIsEdit()) {
                    // Get indices for tally updates
                    Achievement achievements = new Achievement(
                            gameManager.getPoorScoreIndividual(),
                            gameManager.getGreatScoreIndividual(),
                            numPlayers, currentDifficulty);
                    int newIdx = achievements.getHighestRank(sumScores) - 1;
                    int oldIdx = currentGame.getRank() - 1;

                    currentGame.setNumPlayers(numPlayers);
                    currentGame.setFinalTotalScore(sumScores);
                    currentGame.setScores(scoreCalculator.getScoreList());
                    currentGame.setDifficulty(currentDifficulty);
                    currentGame.updateAchievements(currentDifficulty);
                    gameManager.updateEdits(
                            gameManager.getPoorScoreIndividual(),
                            gameManager.getGreatScoreIndividual());

                    gameManager.decreaseTally(oldIdx);
                    gameManager.addTally(newIdx);
                }

                // Adding a game, create new game
                else {
                    Game newGame = new Game(numPlayers, sumScores,
                            gameManager.getPoorScoreIndividual(),
                            gameManager.getGreatScoreIndividual(),
                            scoreCalculator.getScoreList(), currentDifficulty);
                    gameManager.addGame(newGame);
                    gameManager.addTally(newGame.getRank() - 1);
                }

                scoreCalculator.clearLostScores();
                launchAchievementDialog(numPlayers, sumScores);

            } else {
                Toast.makeText(this, R.string.no_players_err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchAchievementDialog(int numPlayers, int sumScores) {
        // Create view
        View v = LayoutInflater.from(this).inflate(R.layout.achievement_layout, null);

        // Set up animations
        YoYo.with(Techniques.Tada).duration(500).repeat(YoYo.INFINITE).playOn(v);

        // Play celebration sound
        MediaPlayer cheering = MediaPlayer.create(this, R.raw.cheering);
        cheering.start();

        // Set up listener when ok (positive button) is clicked
        DialogInterface.OnClickListener positiveButtonListener = (dialogInterface, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                finish();
            }
        };

        // Set up dismiss listener
        DialogInterface.OnDismissListener dismissListener = (dialogInterface) -> {
            cheering.stop();
            this.finish();
        };

        // Build the alert dialog (achievement builder)
        android.app.AlertDialog achievementDialog = new android.app.AlertDialog.Builder(this)
                .setView(v)
                .setTitle(R.string.great_job)
                .setPositiveButton(android.R.string.ok, positiveButtonListener)
                .setOnDismissListener(dismissListener)
                .create();

        // Get current achievement for game
        Achievement currAchievement = new Achievement(
                gameManager.getPoorScoreIndividual(),
                gameManager.getGreatScoreIndividual(),
                numPlayers,
                currentDifficulty);

        // Set body message for dialog
        int highestRank = currAchievement.getHighestRank(sumScores);
        String achievement = currAchievement.getAchievementName(highestRank);
        String gameRank = getString(R.string.your_rank_is) + " " + achievement;
        achievementDialog.setMessage(gameRank);

        // Show dialog
        achievementDialog.show();

        // Update Achievement message with appropriate font
        TextView textView = (TextView) achievementDialog.findViewById(android.R.id.message);
        Typeface comicNeueFont = ResourcesCompat.getFont(this, R.font.comic_neue);
        textView.setTypeface(comicNeueFont);
    }

    private void setUpClearBtn() {
        Button clearBtn = findViewById(R.id.btnClear);

        clearBtn.setOnClickListener(view -> {
            scoreCalculator.clearAll();
            updateScreenUI();
        });
    }

    private int getGameManagerIndex() {
        return getIntent().getIntExtra(EXTRA_GAME_TYPE_INDEX, -1);
    }

    private int getGameIndex() {
        return getIntent().getIntExtra(EXTRA_GAME_INDEX, -1);
    }

    private boolean getIsEdit() {
        return getIntent().getBooleanExtra(EXTRA_IS_EDIT, false);
    }

    private int getNumPlayers() {
        return getIntent().getIntExtra(EXTRA_NUM_PLAYERS, -1);
    }

    public static Intent makeIntent(Context c, boolean isEdit, int gameIndex, int gameManagerIndex, int numPlayers) {
        Intent intent = new Intent(c, GameConfigActivity.class);
        intent.putExtra(EXTRA_IS_EDIT, isEdit);
        intent.putExtra(EXTRA_GAME_INDEX, gameIndex);
        intent.putExtra(EXTRA_GAME_TYPE_INDEX, gameManagerIndex);
        intent.putExtra(EXTRA_NUM_PLAYERS, numPlayers);
        return intent;
    }
}