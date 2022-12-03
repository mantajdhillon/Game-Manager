package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

/**
 * CategoryActivity Class
 * <p>
 * - Displays the list of game categories.
 * - Allows user to add a new game category by clicking +.
 * - Allows user to access the list of played games of a ame category by clicking on the game category.
 */
public class CategoryActivity extends AppCompatActivity {

    private GameCategory gameCategory;
    private static CategorySaver saveState;

    List<CategoryActivity.CategoryListElement> listGames = new ArrayList<>();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_theme_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent help = new Intent(CategoryActivity.this, HelpActivity.class);
                startActivity(help);
                return true;
            case R.id.select_theme:
                Intent themeSelect = ThemeSelectActivity.makeIntent(CategoryActivity.this);
                startActivity(themeSelect);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Save the current state of the game category
    public static void saveCategoryState() {
        saveState.saveData();
    }

    // Initialize screen elements
    private void setUpScreen() {
        try {
            populateCategoryList();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "COULD NOT POPULATE CATEGORY LIST", Toast.LENGTH_SHORT).show();
        }
        generateCategoryList();
        setUpEmptyState();
        setupAddCategoryBtn();
    }

    private void setUpEmptyState() {
        ImageView emptyStateIcon = findViewById(R.id.ivEmptyStateCategory);
        TextView emptyStateDesc = findViewById(R.id.tvEmptyStateDescCategory);

        // Display empty state only if the category manager is empty
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

    private void generateCategoryList() {
        ArrayAdapter<CategoryActivity.CategoryListElement> adapter = new CategoryActivity.MyListAdapter();
        ListView playedGames = findViewById(R.id.categoryList);
        playedGames.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<CategoryActivity.CategoryListElement> {
        public MyListAdapter() {
            super(CategoryActivity.this, R.layout.complex_listview_layout, listGames);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View categoryView = convertView;
            if (categoryView == null) {
                categoryView = getLayoutInflater().inflate
                        (R.layout.complex_listview_layout, parent, false);
            }

            categoryView.setOnClickListener(v -> {
                Intent i = GameActivity.makeIntent(CategoryActivity.this, position);
                startActivity(i);
            });

            CategoryActivity.CategoryListElement currentElement = listGames.get(position);

            ImageView icon = categoryView.findViewById(R.id.achievement_icon);

            icon.setImageBitmap(currentElement.image);

            TextView description = categoryView.findViewById(R.id.tvAchievementsDescription);
            description.setText(currentElement.description);

            return categoryView;
        }
    }

    public Uri getImageUri(String path) {
        return Uri.parse(path);
    }

    private static class CategoryListElement {
        public String description;
        public Bitmap image;

        public CategoryListElement(String description, Bitmap bitmap) {
            this.description = description;
            this.image = bitmap;
        }
    }

    private void populateCategoryList() throws IOException {
        listGames.clear();
        for (int i = 0; i < gameCategory.size(); i++) {
            GameManager gameManager = gameCategory.getGameManager(i);
            Bitmap bitmap;
            String imagePath = gameManager.getImagePath();
            if (imagePath == null) {
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.default_image);
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(CategoryActivity.this.getContentResolver(), getImageUri(imagePath));
            }
            listGames.add(new CategoryActivity.CategoryListElement(gameManager.toString(), bitmap));
        }
    }
}