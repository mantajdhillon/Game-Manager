package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

public class CategoryConfigActivity extends AppCompatActivity {
    // Call the GameCategory instance
    private GameCategory instance = GameCategory.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_config);

        // Set up Action Bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.category_config_activity_add_game);
        // ab.setTitle(R.string.category_config_activity_edit_game);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up buttons
        setUpDeleteBtn();
        setUpSaveBtn();
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

            if(goodScore > badScore) {
                GameManager newManager = new GameManager(name, goodScore, badScore);
                instance.addGameManager(newManager);

                Intent i = new Intent(CategoryConfigActivity.this, CategoryActivity.class);
                startActivity(i);
            }
            else{
                Toast.makeText(this,"Invalid scores",Toast.LENGTH_SHORT).show();
            }

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
        deleteBtn.setOnClickListener(view -> Toast.makeText(this,
                "Should delete game config",
                Toast.LENGTH_SHORT).show());
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
}