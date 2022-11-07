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

import ca.cmpt276.neon_coopachievement.model.Game;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

public class GameConfigActivity extends AppCompatActivity {

    private static final String GAME_TYPE_INDEX = "Game-Type-Index";
    GameCategory gameCategory = GameCategory.getInstance();
    GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config);

        // Set up Action Bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.game_config_activity_add_game);
        // ab.setTitle(R.string.game_config_activity_edit_game);
        ab.setDisplayHomeAsUpEnabled(true);

        gameManager = gameCategory.getGameManager(getGameIndex());

        // Set up buttons
        setUpSaveBtn();
        setUpDeleteBtn();
    }

    public static Intent makeLaunchIntent(Context c, int index) {
        Intent intent = new Intent(c, GameConfigActivity.class);
        intent.putExtra(GAME_TYPE_INDEX, index);
        return intent;
    }

    private int getGameIndex() {
        Intent intent = getIntent();
        return intent.getIntExtra(GAME_TYPE_INDEX, -1);
    }

    private void setUpSaveBtn() {
        Button saveBtn = findViewById(R.id.btnSaveGame);

        saveBtn.setOnClickListener(view -> {

            try {
                EditText etNumPlayers = findViewById((R.id.etNumPlayers));
                int numPlayers = getInt(etNumPlayers);

                EditText etSumScore = findViewById((R.id.etSumPlayerScores));
                int sumScores = getInt(etSumScore);
                Game game = new Game(numPlayers, sumScores,
                        gameManager.getPoorScoreIndividual(), gameManager.getGreatScoreIndividual());
                gameManager.addGame(game);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private int getInt(EditText et) {
        String intStr = et.getText().toString();
        return Integer.parseInt(intStr);
    }

    private void setUpDeleteBtn() {
        Button deleteBtn = findViewById(R.id.btnDeleteGame);
        deleteBtn.setVisibility(View.INVISIBLE);
//        for iteration 2:
//        deleteBtn.setOnClickListener(view -> Toast.makeText(this,
//                "Should delete game",
//                Toast.LENGTH_SHORT).show());
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
}