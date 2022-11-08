package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ca.cmpt276.neon_coopachievement.model.GameCategory;

/*
    CategoryActivity Class
    - Displays the list of game categories.
    - Allows user to add a new game category by clicking +.
    - Allows user to access the list of played games of a
      game category by clicking on the game category.
 */
public class CategoryActivity extends AppCompatActivity {

    private GameCategory gameCategory;
    private static CategorySaver saveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Set up action bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.category_config_activity_title);

        saveState = new CategorySaver(this);
        gameCategory = GameCategory.getInstance();
        setUpScreen();
    }

    // Save the current state of the game category
    public static void saveCategoryState() {
        saveState.saveData();
    }

    private void setUpScreen() {
        populateListView();
        setUpEmptyState();
        setupAddCategoryBtn();
        registerListClickCallback();
    }

    @Override
    protected void onStart() {
        super.onStart();

        saveCategoryState();
        setUpScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();

        saveCategoryState();
        setUpScreen();
    }

    private void populateListView() {
        ArrayList<String> gameTypes = new ArrayList<>();
        for (int i = 0; i < gameCategory.size(); i++) {
            gameTypes.add(gameCategory.getGameManager(i).toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.items,
                gameTypes);

        ListView categoryListView = findViewById(R.id.categoryList);
        categoryListView.setAdapter(adapter);
    }

    // Configure the empty state when there are no more games categories
    private void setUpEmptyState() {
        ImageView emptyStateIcon = findViewById(R.id.ivEmptyStateCategory);
        TextView emptyStateDesc = findViewById(R.id.tvEmptyStateDescCategory);

        // Display only if the category manager is 0
        if (gameCategory.size() == 0) {
            emptyStateIcon.setVisibility(View.VISIBLE);
            emptyStateDesc.setVisibility(View.VISIBLE);
        } else {
            emptyStateIcon.setVisibility(View.INVISIBLE);
            emptyStateDesc.setVisibility(View.INVISIBLE);
        }
    }

    private void setupAddCategoryBtn() {
        FloatingActionButton newCategory = findViewById(R.id.addCategoryBtn);
        newCategory.setOnClickListener(v -> {
            Intent i = CategoryConfigActivity.makeIntent(
                    CategoryActivity.this,
                    false,
                    -1);
            startActivity(i);
        });
    }

    private void registerListClickCallback() {
        ListView categories = findViewById(R.id.categoryList);
        categories.setOnItemClickListener((parent, viewClicked, position, id) -> {
            Intent i = GameActivity.makeIntent(
                    CategoryActivity.this,
                    position);
            startActivity(i);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent i = new Intent(CategoryActivity.this, HelpActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}