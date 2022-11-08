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
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

/*
    CategoryConfigActivity Class
    - Used for add/edit/delete game category configurations.
    - A new game category is created when user inputs a game
      name, good score and bad score, and clicks save.
    - Editing mode displays the previous name and scores
      the user entered in the inputs fields. Configurations
      updated when user changes the fields and clicks save.
      Or the user may delete the game category by clicking
      delete.
 */
public class CategoryConfigActivity extends AppCompatActivity {

    public static final String GAME_INDEX = "gameIndex";
    private static final String GAME_NAME = "game name";
    private static final String GOOD_SCORE = "good score";
    private static final String BAD_SCORE = "bad score";
    public static final String IS_EDIT = "isEdit";


    private boolean isEdit;
    private int gameIndex;
    private GameManager gameManager;

    private GameCategory instance = GameCategory.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_config);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.category_config_activity_add_game);
        ab.setDisplayHomeAsUpEnabled(true);

        extractDataFromIntent();

        setUpSaveBtn();

        if(isEdit){
            gameManager = instance.getGameManager(gameIndex);
            ab.setTitle(R.string.category_config_activity_edit_game);
            setUpDeleteBtn();
            populateFields();
        }
        else{
            hideDeleteButton();
        }
    }

    private void hideDeleteButton() {
        Button deleteBtn = findViewById(R.id.btnDeleteConfig);
        deleteBtn.setVisibility(View.INVISIBLE);
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
                if(isEdit){
                    gameManager.setName(name);
                    gameManager.setGreatScoreIndividual(goodScore);
                    gameManager.setPoorScoreIndividual(badScore);
                    gameManager.updateEdits(badScore, goodScore);
                }
                else {
                    GameManager newManager = new GameManager(name, goodScore, badScore);
                    instance.addGameManager(newManager);
                }
                finish();
            }
            catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private int getInt(EditText et){
        int newInt = 0;
        String intStr = et.getText().toString();
        newInt = Integer.parseInt(intStr);
        return newInt;
    }


    private void setUpDeleteBtn() {
        Button deleteBtn = findViewById(R.id.btnDeleteConfig);

        deleteBtn.setOnClickListener(v -> {
            if (isEdit) {
                instance.removeGameManager(gameIndex);
            }
            finish();
        });
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
                Intent i = new Intent(CategoryConfigActivity.this, HelpActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent makeCategoryConfigIntent(Context context, boolean isEdit, int gameIndex) {
        Intent i = new Intent(context, CategoryConfigActivity.class);
        i.putExtra(IS_EDIT, isEdit);
        i.putExtra(GAME_INDEX, gameIndex);
        return i;
    }


    private void extractDataFromIntent() {
        Intent i = getIntent();
        isEdit = i.getBooleanExtra(IS_EDIT, false);
        gameIndex = i.getIntExtra(GAME_INDEX,-1);
    }

    private void populateFields() {
        EditText etGameName = findViewById(R.id.etGameName);
        etGameName.setText(gameManager.getName());

        EditText etGoodScore = findViewById(R.id.etGoodScore);
        etGoodScore.setText(Integer.toString(gameManager.getGreatScoreIndividual()));

        EditText etBadScore = findViewById(R.id.etBadScore);
        etBadScore.setText(Integer.toString(gameManager.getPoorScoreIndividual()));
    }
}