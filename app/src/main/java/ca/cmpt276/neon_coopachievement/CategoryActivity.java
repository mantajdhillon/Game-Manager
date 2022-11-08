package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

/*
    CategoryActivity Class
    - Displays the list of game categories.
    - Allows user to add a new game category by clicking +.
    - Allows user to access the list of played games of a
      game category by clicking on the game category.
 */
public class CategoryActivity extends AppCompatActivity {

    private static GameCategory gameCategory = GameCategory.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.category_config_activity_title);

        getGameManagerList();
        setUpEmptyState();
        setupAddCategoryBtn();
        categoryClickCallback();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getGameManagerList();
        setUpEmptyState();
        setupAddCategoryBtn();
        categoryClickCallback();
    }

    private void getGameManagerList() {
        ArrayList<String> gameTypes = new ArrayList<>();
        for (int i=0; i < gameCategory.getGameManagersStored(); i++) {
            gameTypes.add(gameCategory.getGameManager(i).toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.items,
                gameTypes);

        ListView categories = findViewById(R.id.categoryList);
        categories.setAdapter(adapter);
    }

    // Configure the empty state when there are no more games categories
    private void setUpEmptyState() {
        ImageView emptyStateIcon = findViewById(R.id.ivEmptyStateCategory);
        TextView emptyStateDesc = findViewById(R.id.tvEmptyStateDescCategory);

        // Display only if the category manager is 0
        if (gameCategory.getGameManagersStored() == 0) {
            emptyStateIcon.setVisibility(View.VISIBLE);
            emptyStateDesc.setVisibility(View.VISIBLE);
        } else {
            emptyStateIcon.setVisibility(View.INVISIBLE);
            emptyStateDesc.setVisibility(View.INVISIBLE);
        }
    }

    private void setupAddCategoryBtn() {
        FloatingActionButton newCategory = findViewById(R.id.addCategoryBtn);
        newCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = CategoryConfigActivity.makeCategoryConfigIntent(CategoryActivity.this, false,-1);
                startActivity(i);
            }
        });
    }

    private void categoryClickCallback() {
        ListView categories = findViewById(R.id.categoryList);
        categories.setOnItemClickListener((parent, viewClicked, position, id) -> {

            TextView category = (TextView) viewClicked;

            Intent i = GameActivity.makeLaunchIntent(CategoryActivity.this, position);
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