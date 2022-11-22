package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.Toast;

import ca.cmpt276.neon_coopachievement.model.Achievement;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.Theme;

/**
 * Theme Select Activity
 * <p>
 * - Changes theme based on click of radio buttons
 * - Loads old theme from Game Category Manager
 */
public class ThemeSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_select);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.theme_selection_title);
        ab.setDisplayHomeAsUpEnabled(true);

        setupRadioGroup();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRadioGroup() {
        setRadioButtonListeners(R.id.radioTheme1, Theme.ONE);
        setRadioButtonListeners(R.id.radioTheme2, Theme.TWO);
        setRadioButtonListeners(R.id.radioTheme3, Theme.THREE);
    }

    private void setRadioButtonListeners(int btnId, Theme theme) {
        RadioButton themeChoice = findViewById(btnId);
        if (GameCategory.getInstance().getCurrentTheme() == theme) {
            themeChoice.setChecked(true);
        }

        themeChoice.setOnClickListener(v -> {
                    GameCategory.getInstance().setCurrentTheme(theme);

                    // Save date for SharedPreferences
                    CategoryActivity.saveCategoryState();
                }
        );
    }

    public static Intent makeIntent(Context c) {
        return new Intent(c, ThemeSelectActivity.class);
    }
}