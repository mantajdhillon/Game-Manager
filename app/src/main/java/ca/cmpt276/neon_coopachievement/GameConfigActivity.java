package ca.cmpt276.neon_coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

public class GameConfigActivity extends AppCompatActivity {

    private static final String ACTIVITY_TITLE = "Add game";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config);

        // Set up Action Bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(ACTIVITY_TITLE);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up buttons
        setUpSaveBtn();
        setUpDeleteBtn();
    }

    private void setUpSaveBtn() {
        Button saveBtn = findViewById(R.id.btnSaveGame);

        saveBtn.setOnClickListener(view -> Toast.makeText(this,
                "Should save game",
                Toast.LENGTH_SHORT).show());
    }

    private void setUpDeleteBtn() {
        Button deleteBtn = findViewById(R.id.btnDeleteGame);

        deleteBtn.setOnClickListener(view -> Toast.makeText(this,
                "Should delete game",
                Toast.LENGTH_SHORT).show());
    }
}