package ca.cmpt276.neon_coopachievement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String ACTIVITY_TITLE = "Main Menu";

    // TODO: replace with category manager size
    private static final int SIZE_OF_GAME_CATEGORY_MANAGER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(ACTIVITY_TITLE);

        setUpEmptyState();
    }

    // Configure the empty state
    private void setUpEmptyState() {
        ImageView emptyStateIcon = findViewById(R.id.ivEmptyState);
        TextView emptyStateDesc = findViewById(R.id.tvEmptyStateDesc);

        // Display only if the category manager is 0
        if (SIZE_OF_GAME_CATEGORY_MANAGER == 0) {
            emptyStateIcon.setVisibility(View.VISIBLE);
            emptyStateDesc.setVisibility(View.VISIBLE);
        } else {
            emptyStateIcon.setVisibility(View.INVISIBLE);
            emptyStateDesc.setVisibility(View.INVISIBLE);
        }
    }
}