package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

/**
 * CategoryConfigActivity Class
 * <p>
 * - Used for add/edit/delete game category configurations.
 *
 * - A new game category is created when user inputs a game
 *   name, good score and bad score, and clicks save.
 *
 * - Editing mode displays the previous name and scores
 *   the user entered in the inputs fields. Configurations
 *   updated when user changes the fields and clicks save.
 *   Or the user may delete the game category by clicking
 *   delete.
 */
public class CategoryConfigActivity extends AppCompatActivity {

    private static final String GAME_INDEX = "gameIndex";
    private static final String IS_EDIT = "isEdit";

    private final GameCategory instance = GameCategory.getInstance();

    // Current game
    private int currGameIndex;
    private GameManager currGameManager;

    private boolean editConfig;     // Determines whether the user is adding or editing a game

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_config);

        // Set up action bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setUpScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                if (!editConfig) {
                    instance.removeGameManager(instance.size() - 1);
                }
                return true;
            case R.id.action_help:
                Intent i = new Intent(CategoryConfigActivity.this, HelpActivity.class);
                startActivity(i);
                return true;

            case R.id.take_photo:
                Intent launchPhoto;
                if (editConfig) {
                    launchPhoto = TakePhotoActivity.makeLaunchIntent
                            (CategoryConfigActivity.this, currGameIndex, -1, true);
                } else {
                    launchPhoto = TakePhotoActivity.makeLaunchIntent
                            (CategoryConfigActivity.this, instance.size() - 1, -1, true);
                }
                startActivity(launchPhoto);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpScreen() {
        ActionBar ab = getSupportActionBar();

        extractDataFromIntent();
        setUpSaveBtn();

        // Editing existing configuration
        if (editConfig) {
            ab.setTitle(R.string.category_config_activity_edit_game);
            currGameManager = instance.getGameManager(currGameIndex);
            setUpDeleteBtn();
            populateEditTexts();
        }

        // Adding new game configuration
        else {
            ab.setTitle(R.string.category_config_activity_add_game);
            hideDeleteButton();
            currGameManager = new GameManager();
            instance.addGameManager(currGameManager);
        }
    }

    private void extractDataFromIntent() {
        Intent i = getIntent();
        editConfig = i.getBooleanExtra(IS_EDIT, false);
        currGameIndex = i.getIntExtra(GAME_INDEX, -1);
    }

    private void setUpSaveBtn() {
        Button saveBtn = findViewById(R.id.btnSaveConfig);
        saveBtn.setOnClickListener(view -> {
            try {
                EditText getName = findViewById(R.id.etGameName);
                String name = getName.getText().toString();

                EditText getGoodScore = findViewById((R.id.etGoodScore));
                int goodScore = getInt(getGoodScore);

                EditText getBadScore = findViewById((R.id.etBadScore));
                int badScore = getInt(getBadScore);
                if (editConfig) {
                    currGameManager.setName(name);
                    currGameManager.isValidScore(goodScore, badScore);
                    currGameManager.setGreatScoreIndividual(goodScore);
                    currGameManager.setPoorScoreIndividual(badScore);
                    currGameManager.updateEdits(badScore, goodScore);
                    currGameManager.updateTallyArray();
                }

                // Add Configuration: Create new game manager
                else {
                    currGameManager.setName(name);
                    currGameManager.isValidScore(goodScore, badScore);
                    currGameManager.setPoorScoreIndividual(badScore);
                    currGameManager.setGreatScoreIndividual(goodScore);

//                    GameManager newManager = new GameManager(name, goodScore, badScore);
//                    instance.addGameManager(newManager);
                }
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.score_valid_int_err_msg, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpDeleteBtn() {
        Button deleteBtn = findViewById(R.id.btnDeleteConfig);

        deleteBtn.setOnClickListener(v -> {
            if (editConfig) {
                instance.removeGameManager(currGameIndex);
            }
            finish();
        });
    }

    private void populateEditTexts() {
        EditText etGameName = findViewById(R.id.etGameName);
        etGameName.setText(currGameManager.getName());

        EditText etGoodScore = findViewById(R.id.etGoodScore);
        etGoodScore.setText(Integer.toString(currGameManager.getGreatScoreIndividual()));

        EditText etBadScore = findViewById(R.id.etBadScore);
        etBadScore.setText(Integer.toString(currGameManager.getPoorScoreIndividual()));
    }

    private void hideDeleteButton() {
        Button deleteBtn = findViewById(R.id.btnDeleteConfig);
        deleteBtn.setVisibility(View.INVISIBLE);
    }

    // Parse an integer from a textview
    private int getInt(EditText et) {
        String intStr = et.getText().toString();
        return Integer.parseInt(intStr);
    }

    public static Intent makeIntent(Context context, boolean isEdit, int gameIndex) {
        Intent i = new Intent(context, CategoryConfigActivity.class);
        i.putExtra(IS_EDIT, isEdit);
        i.putExtra(GAME_INDEX, gameIndex);
        return i;
    }
}