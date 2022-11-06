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

public class CategoryConfigActivity extends AppCompatActivity {

    private static final String GAME_NAME = "game name";
    private static final String GOOD_SCORE = "good score";
    private static final String BAD_SCORE = "bad score";

    private String gameName;
    private int goodScore;
    private int badScore;

    private GameCategory instance = GameCategory.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_config);

        // Set up Action Bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.category_config_activity_add_game);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up buttons
        setUpDeleteBtn();
        setUpSaveBtn();

        extractDataFromIntent();
        if (gameName != null) {
            ab.setTitle(R.string.category_config_activity_edit_game);
        }
    }

    private void setUpSaveBtn() {
        Button saveBtn = findViewById(R.id.btnSaveConfig);

        saveBtn.setOnClickListener(view -> {
            EditText getName = findViewById(R.id.etGameName);
            String name = getName.getText().toString();

            EditText getGoodScore = findViewById((R.id.etGoodScore));
            int goodScore = getInt(getGoodScore);

            EditText getBadScore = findViewById((R.id.etBadScore));
            int badScore = getInt(getBadScore);

            try{
                GameManager newManager = new GameManager(name, goodScore, badScore);
                instance.addGameManager(newManager);
            }
            catch(Exception e){
                Toast.makeText(this,"Invalid scores",Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }

    private int getInt(EditText et){
        int newInt = 0;
        String intStr = et.getText().toString();
        try {
            newInt = Integer.parseInt(intStr);
        }  catch (NumberFormatException ex){
            Toast.makeText(this, "INVALID ENTRY",Toast.LENGTH_SHORT).show();
        }
        return newInt;
    }


    private void setUpDeleteBtn() {
        Button deleteBtn = findViewById(R.id.btnDeleteConfig);

        // todo link
        deleteBtn.setOnClickListener(v -> {
            // Change removeGameManager to boolean? Then change below to a try catch block
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

    public static Intent makeCategoryConfigIntent(Context context, String gameName, int goodScore, int badScore) {
        Intent i = new Intent(context, CategoryConfigActivity.class);
        if (gameName != null) {
            i.putExtra(GAME_NAME, gameName);
            i.putExtra(GOOD_SCORE, goodScore);
            i.putExtra(BAD_SCORE, badScore);
        }
        return i;
    }

    private void extractDataFromIntent() {
        Intent i = getIntent();
        gameName = i.getStringExtra(GAME_NAME);
        goodScore = i.getIntExtra(GOOD_SCORE, -1);
        badScore = i.getIntExtra(BAD_SCORE, -1);
        if (gameName != null) {
            populateFields();
        }
    }

    private void populateFields() {
        EditText etGameName = findViewById(R.id.etGameName);
        etGameName.setText(gameName, TextView.BufferType.EDITABLE);

        EditText etGoodScore = findViewById(R.id.etGoodScore);
        etGoodScore.setText(Integer.toString(goodScore), TextView.BufferType.EDITABLE);

        EditText etBadScore = findViewById(R.id.etBadScore);
        etBadScore.setText(Integer.toString(badScore), TextView.BufferType.EDITABLE);
    }
}