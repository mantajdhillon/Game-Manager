package ca.cmpt276.neon_coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryActivity extends AppCompatActivity {

    public static final String ACTIVITY_TITLE = "Game Category";

    // TODO: replace with game category manager size
    private static final int SIZE_OF_GAME_CATEGORY_MANAGER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(ACTIVITY_TITLE);

        String[] gameTypes = {"Game Type 1", "Game Type 2", "Game Type 3"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.items,
                gameTypes);

        ListView categories = findViewById(R.id.categoryList);
        categories.setAdapter(adapter);

        setUpEmptyState();
        setupAddCategoryBtn();
        categoryClickCallback();
    }

    // Configure the empty state when there are no more games categories
    private void setUpEmptyState() {
        ImageView emptyStateIcon = findViewById(R.id.ivEmptyStateCategory);
        TextView emptyStateDesc = findViewById(R.id.tvEmptyStateDescCategory);

        // Display only if the category manager is 0
        if (SIZE_OF_GAME_CATEGORY_MANAGER == 0) {
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
                Intent i = new Intent(CategoryActivity.this, CategoryConfigActivity.class);
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
}