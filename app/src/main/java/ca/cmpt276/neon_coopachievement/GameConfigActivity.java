package ca.cmpt276.neon_coopachievement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import androidx.fragment.app.FragmentManager;

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
 * - A new game is created when user inputs a number
 *   of players and sum of players' scores, and clicks
 *   save.
 * - Editing mode displays the previous number of players and score that
 *   the user entered in the inputs fields.
 * - Details are updated when user changes the fields and clicks save.
 *   The user may delete the game by clicking delete.
 */
public class GameConfigActivity extends AppCompatActivity {

    private Game.Difficulty currentDifficulty = Game.Difficulty.NORMAL;

    private static final String EXTRA_GAME_TYPE_INDEX = "Game-Type-Index";
    private static final String EXTRA_IS_EDIT = "isEdit";
    private static final String EXTRA_GAME_INDEX = "gameIndex";
    private static final String EXTRA_NUM_PLAYERS = "numPlayers";

    private GameManager gameManager;
    private Game currentGame;

    private ScoreCalculator sc = new ScoreCalculator();
    public static String achievement = "Achievement";

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

        setupRadioGroup();
        setUpAddPlayerBtn();
        setUpPlayersSlots();
        setUpEmptyState(sc.getNumPlayers());
        populatePlayerListView();
        populateAchievementView();
        registerListClickCallback();
        setUpSaveBtn();
        setUpClearBtn();

        // Editing a game configuration
        if (getIsEdit()) {
            currentGame = gameManager.getGame(getGameIndex());
            currentDifficulty = currentGame.getDifficulty();
            setupRadioGroup();
            currentGame.updateAchievements(currentDifficulty);
            ab.setTitle(R.string.game_config_activity_edit_game);
            sc.setScores(currentGame.getScores());
            populatePlayerListView();
            populateAchievementView();
            setUpEmptyState(sc.getNumPlayers());
        }
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
                    gameManager.removeGame(getGameIndex());
                }
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void setupRadioGroup() {
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

            final EditText playerScore = new EditText(GameConfigActivity.this);
            playerScore.setInputType(InputType.TYPE_CLASS_NUMBER);
            playerDialog.setView(playerScore);

            playerDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        sc.addScore(Integer.parseInt(playerScore.getText().toString().trim()));
                    } catch(Exception e) {
                        Toast.makeText(GameConfigActivity.this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
                    }
                    populatePlayerListView();
                    populateAchievementView();
                    setUpEmptyState(sc.getNumPlayers());
                }
            });

            playerDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            playerDialog.show();
        });
    }

    private void setUpPlayersSlots() {
        ArrayList<Integer> preScoresList = new ArrayList<>();
        for (int i = 0; i < getNumPlayers(); i++) {
            preScoresList.add(0);
        }
        sc.setScores(preScoresList);
    }

    private void populatePlayerListView() {
        ArrayList<String> players = new ArrayList<>();
        for (int i = 0; i < sc.getNumPlayers(); i++) {
            players.add(sc.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.items,
                players);

        ListView playersListView = findViewById(R.id.playersList);
        playersListView.setAdapter(adapter);
    }

    private void populateAchievementView() {

        int numPlayers = sc.getNumPlayers();
        int sumScores = sc.getSumScores();

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
        playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder playerDialog = new AlertDialog.Builder(GameConfigActivity.this);
                playerDialog.setTitle(R.string.edit_player_score_prompt);

                EditText playerScore = new EditText(GameConfigActivity.this);
                playerScore.setInputType(InputType.TYPE_CLASS_NUMBER);
                playerScore.setText(Integer.toString(sc.getScore(position + 1)));
                playerDialog.setView(playerScore);

                playerDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            sc.updateScore(position + 1, Integer.parseInt(playerScore.getText().toString().trim()));
                        } catch(Exception e) {
                            Toast.makeText(GameConfigActivity.this, R.string.invalid_input , Toast.LENGTH_SHORT).show();
                        }
                        populatePlayerListView();
                        populateAchievementView();
                    }
                });

                playerDialog.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                playerDialog.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sc.removeScore(position + 1);
                        populatePlayerListView();
                        populateAchievementView();
                    }
                });

                playerDialog.show();
            }
        });
    }

    private void setUpSaveBtn() {
        Button saveBtn = findViewById(R.id.btnSaveGame);

        saveBtn.setOnClickListener(view -> {

            int numPlayers = sc.getNumPlayers();

            if (numPlayers != 0) {

                int sumScores = sc.getSumScores();

                if (getIsEdit()) {
                    currentGame.setNumPlayers(numPlayers);
                    currentGame.setFinalTotalScore(sumScores);
                    currentGame.setScores(sc.getScores());
                    currentGame.setDifficulty(currentDifficulty);
                    currentGame.updateAchievements(currentDifficulty);
                    gameManager.updateEdits(
                            gameManager.getPoorScoreIndividual(),
                            gameManager.getGreatScoreIndividual());
                }

                // Make a new game
                else {
                    Game newGame = new Game(numPlayers, sumScores,
                            gameManager.getPoorScoreIndividual(),
                            gameManager.getGreatScoreIndividual(),
                            sc.getScores(), currentDifficulty);
                    gameManager.addGame(newGame);
                }
                makeAchievementDialog(numPlayers, sumScores);

            } else {
                Toast.makeText(this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void makeAchievementDialog(int numPlayers, int sumScores) {
        Achievement achievements = new Achievement(
                gameManager.getPoorScoreIndividual(),
                gameManager.getGreatScoreIndividual(),
                numPlayers, currentDifficulty);

        int rank = achievements.getHighestRank(sumScores);

        this.achievement = achievements.getAchievementName(rank);


        // Make the fragment manager
        FragmentManager manager = getSupportFragmentManager();
        AchievementFragment achievementDialog = new AchievementFragment();

        // Show dialog
        achievementDialog.show(manager, getString(R.string.achievement_dialog));

        // Make sound
        // Cheering audio downloaded from here: https://mixkit.co/free-sound-effects/applause/
        MediaPlayer cheering = MediaPlayer.create(this, R.raw.cheering);
        cheering.start();
    }


    private void setUpClearBtn() {
        Button clearBtn = findViewById(R.id.btnClear);

        clearBtn.setOnClickListener(view -> {
            sc.clearAll();
            populatePlayerListView();
            populateAchievementView();
            setUpEmptyState(sc.getNumPlayers());
        });
    }

    public static Intent makeIntent(Context c, boolean isEdit, int gameIndex, int gameManagerIndex, int numPlayers) {
        Intent intent = new Intent(c, GameConfigActivity.class);
        intent.putExtra(EXTRA_IS_EDIT, isEdit);
        intent.putExtra(EXTRA_GAME_INDEX, gameIndex);
        intent.putExtra(EXTRA_GAME_TYPE_INDEX, gameManagerIndex);
        intent.putExtra(EXTRA_NUM_PLAYERS, numPlayers);
        return intent;
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
}