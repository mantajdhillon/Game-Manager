package ca.cmpt276.neon_coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ca.cmpt276.neon_coopachievement.model.GameCategory;

public class ThemeSelectActivity extends AppCompatActivity {

    private GameCategory gameCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_select);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.theme_selection_title);

        gameCategory = GameCategory.getInstance();
    }


    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, ThemeSelectActivity.class);
        return intent;
    }
}