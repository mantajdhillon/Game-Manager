package ca.cmpt276.neon_coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;

import ca.cmpt276.neon_coopachievement.model.Achievement;

public class ThemeSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_select);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.theme_selection_title);

        setupRadioGroup();
    }

    private void setupRadioGroup() {
        setRadioButtonListeners(R.id.radioTheme1, Achievement.Theme.ONE);
        setRadioButtonListeners(R.id.radioTheme2, Achievement.Theme.TWO);
        setRadioButtonListeners(R.id.radioTheme3, Achievement.Theme.THREE);

    }

    private void setRadioButtonListeners(int btnId, Achievement.Theme theme) {
        RadioButton themeChoice = findViewById(btnId);
        if (Achievement.getTheme() == theme) {
            themeChoice.setChecked(true);
        }
        themeChoice.setOnClickListener(v -> Achievement.setTheme(theme));
    }

    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, ThemeSelectActivity.class);
        return intent;
    }
}