package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * HelpActivity Class
 * <p>
 * - Displays one big text which describes the app
 *   and explain the purpose of each screen (activity)
 *   in the app.
 * - Accessed through ? icon in top right corner of
 *   every activity.
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setUpScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpScreen() {
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.help_activity_title);
        ab.setDisplayHomeAsUpEnabled(true);

        TextView text = findViewById(R.id.tvHelpCentreMessage);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }
}