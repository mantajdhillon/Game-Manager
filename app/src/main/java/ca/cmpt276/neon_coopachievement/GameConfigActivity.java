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

public class GameConfigActivity extends AppCompatActivity {

    private static final String GAME_TYPE_INDEX = "Game-Type-Index";
    public static final String IS_EDIT = "isEdit";
    public static final String GAME_INDEX = "gameIndex";
    GameCategory gameCategory = GameCategory.getInstance();
    GameManager gameManager;
    Game game;

    private EditText etNumPlayers;
    private EditText etSumScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config);

        // Set up Action Bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.game_config_activity_add_game);
        ab.setDisplayHomeAsUpEnabled(true);

        gameManager = gameCategory.getGameManager(getGameManagerIndex());

        Button deleteBtn = findViewById(R.id.btnDeleteGame);
        deleteBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setEnabled(false);

        setUpSaveBtn();

        etNumPlayers = findViewById(R.id.etNumPlayers);
        etNumPlayers.addTextChangedListener(inputWatcher);

        etSumScore = findViewById(R.id.etSumPlayerScores);
        etSumScore.addTextChangedListener(inputWatcher);

        if(getisEdit()){
            Toast.makeText(this, "isEdit", Toast.LENGTH_SHORT).show();
            game = gameManager.getGame(getGameIndex());
            ab.setTitle(R.string.game_config_activity_edit_game);
            populateFields();
            populateAchievementView();
            setUpDeleteBtn();
        }
    }

    public static Intent makeLaunchIntent(Context c, boolean isEdit, int gameIndex, int gameManagerIndex) {
        Intent intent = new Intent(c, GameConfigActivity.class);
        intent.putExtra(IS_EDIT,isEdit);
        intent.putExtra(GAME_INDEX,gameIndex);
        intent.putExtra(GAME_TYPE_INDEX, gameManagerIndex);
        return intent;
    }

    private int getGameManagerIndex() {
        Intent intent = getIntent();
        return intent.getIntExtra(GAME_TYPE_INDEX, -1);
    }

    private int getGameIndex(){
        Intent intent = getIntent();
        return intent.getIntExtra(GAME_INDEX,-1);
    }

    private boolean getisEdit(){
        Intent intent = getIntent();
        return intent.getBooleanExtra(IS_EDIT,false);
    }

    private void populateFields(){
        EditText etNumPlayers = findViewById((R.id.etNumPlayers));
        etNumPlayers.setText(Integer.toString(game.getNumPlayers()));

        EditText etSumScore = findViewById((R.id.etSumPlayerScores));
        etSumScore.setText(Integer.toString(game.getFinalTotalScore()));
    }

    private void setUpSaveBtn() {
        Button saveBtn = findViewById(R.id.btnSaveGame);

        saveBtn.setOnClickListener(view -> {

            try {
                int numPlayers = getInt(etNumPlayers);
                int sumScores = getInt(etSumScore);

                if(getisEdit()){
                    game.setNumPlayers(numPlayers);
                    game.setFinalTotalScore(sumScores);
                }
                else {
                    Game newGame = new Game(numPlayers, sumScores,
                            gameManager.getPoorScoreIndividual(), gameManager.getGreatScoreIndividual());
                    gameManager.addGame(newGame);
                }
                finish();
            } catch (Exception e){
                Toast.makeText(this,"Invalid input",Toast.LENGTH_SHORT).show();
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
        Button deleteBtn = findViewById(R.id.btnDeleteGame);
        deleteBtn.setVisibility(View.VISIBLE);
        deleteBtn.setEnabled(true);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameManager.removeGame(getGameIndex());
                finish();
            }
        });
    }

    private TextWatcher inputWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //leaving empty
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            populateAchievementView();
        }

        @Override
        public void afterTextChanged(Editable s) {
            //leaving empty
        }
    };

    private void populateAchievementView() {
        String strNumPlayers = etNumPlayers.getText().toString().trim();
        String strSumScore = etSumScore.getText().toString().trim();

        TextView tvAchieveGenerator = findViewById(R.id.tvAchieveGenerator);

        if (!strNumPlayers.isEmpty() && !strSumScore.isEmpty()) {
            Achievement achievements = new Achievement(
                    gameManager.getPoorScoreIndividual(),
                    gameManager.getGreatScoreIndividual(),
                    Integer.parseInt(strNumPlayers));

            int rank = achievements.getRank(Integer.parseInt(strSumScore));
            String rankName = achievements.getAchievementName(rank);

            tvAchieveGenerator.setText(rankName);
        } else {
            tvAchieveGenerator.setText("");
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
}