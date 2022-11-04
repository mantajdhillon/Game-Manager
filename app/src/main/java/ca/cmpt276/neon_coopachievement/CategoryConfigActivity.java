package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class CategoryConfigActivity extends AppCompatActivity {

    public static final String ACTIVITY_TITLE = "Add game configuration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_config);

        // Set up Action Bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(ACTIVITY_TITLE);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up buttons
        setUpDeleteBtn();
        setUpSaveBtn();
    }

    private void setUpSaveBtn() {
        Button saveBtn = findViewById(R.id.btnSaveConfig);

        saveBtn.setOnClickListener(view -> Toast.makeText(this,
                "Should save game config",
                Toast.LENGTH_SHORT).show());
    }

    private void setUpDeleteBtn() {
        Button deleteBtn = findViewById(R.id.btnDeleteConfig);

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