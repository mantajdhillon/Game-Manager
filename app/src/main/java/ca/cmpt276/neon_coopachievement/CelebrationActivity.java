package ca.cmpt276.neon_coopachievement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import ca.cmpt276.neon_coopachievement.model.Achievement;
import ca.cmpt276.neon_coopachievement.model.Game;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

public class CelebrationActivity extends AppCompatActivity {

    private static final String EXTRA_GAME_TYPE_INDEX = "Game-Type-Index";
    private static final String EXTRA_GAME_INDEX = "gameIndex";

    private GameCategory gameCategory;
    private GameManager gameManager;
    private Game currentGame;

    private Achievement achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebration);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.achievement_celebration);
        ab.setDisplayHomeAsUpEnabled(true);

        // Get current game manager
        gameCategory = GameCategory.getInstance();
        gameManager = gameCategory.getGameManager(getGameManagerIndex());
        currentGame = gameManager.getGame(getGameIndex());

        setUpCelebrationActivity();
        playAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpCelebrationActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_theme_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_help:
                Intent help = new Intent(CelebrationActivity.this, HelpActivity.class);
                startActivity(help);
                return true;
            case R.id.select_theme:
                Intent themeSelect = ThemeSelectActivity.makeIntent(CelebrationActivity.this);
                startActivity(themeSelect);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpCelebrationActivity() {
        achievements = new Achievement(
                gameManager.getPoorScoreIndividual(),
                gameManager.getGreatScoreIndividual(),
                currentGame.getNumPlayers(),
                currentGame.getDifficulty()
        );

        populateAchievementView();
        populateNextAchievementView();
        populateNumPointsTillNextRankView();
        populateAchievementIcon();

        setUpPlayAnimationBtn();
    }

    private void populateAchievementView() {
        TextView tvAchieveGenerator = findViewById(R.id.tvCeleAchieveGenerator);

        int rank = currentGame.getRank();
        String rankName = achievements.getAchievementName(rank);

        tvAchieveGenerator.setText(rankName);
    }

    private void populateNextAchievementView() {
        TextView tvNextAchievementGen = findViewById(R.id.tvNextAchievementGen);

        int rank = currentGame.getRank();
        String nextRankName;
        try {
            nextRankName = achievements.getAchievementName(rank + 1);
        } catch (Exception e) {
            nextRankName = getString(R.string.dash);
        }
        tvNextAchievementGen.setText(nextRankName);
    }

    private void populateNumPointsTillNextRankView() {
        TextView tvPointsTillNextRank = findViewById(R.id.tvPointsTillNextRankGen);

        int numPoints = achievements.getNumPointsTillNextRank(
                currentGame.getRank(),
                currentGame.getFinalTotalScore());
        String strNumPoints = "" + numPoints;

        tvPointsTillNextRank.setText(strNumPoints);
    }

    private void populateAchievementIcon() {
        String filename = gameCategory.getCurrentTheme() + getString(R.string.IconFileName) + currentGame.getRank();
        int id = getResources().getIdentifier(filename, getString(R.string.defType), this.getPackageName());
        ImageView icon = findViewById(R.id.celebrationAchievementIcon);
        icon.setImageResource(id);
    }

    private void setUpPlayAnimationBtn() {
        Button playAnimation = findViewById(R.id.btnPlayAnimation);

        playAnimation.setOnClickListener(view -> {

            playAnimation();
        });
    }

    private void playAnimation() {
        // Create view
        View v = LayoutInflater.from(this).inflate(R.layout.achievement_layout, null);

        // Set up animations
        YoYo.with(Techniques.Tada).duration(500).repeat(YoYo.INFINITE).playOn(v);

        // Play celebration sound
        MediaPlayer cheering = MediaPlayer.create(this, R.raw.cheering);
        cheering.start();

        DialogInterface.OnClickListener positiveButtonListener = (dialogInterface, which) -> {
            // does nothing, just for looks
        };

        // Set up dismiss listener
        DialogInterface.OnDismissListener dismissListener = (dialogInterface) -> {
            cheering.stop();
        };

        // Build the alert dialog (achievement builder)
        android.app.AlertDialog achievementDialog = new android.app.AlertDialog.Builder(this)
                .setView(v)
                .setTitle(R.string.great_job)
                .setPositiveButton(android.R.string.ok, positiveButtonListener)
                .setOnDismissListener(dismissListener)
                .create();

        // Show dialog
        achievementDialog.show();
    }

    private int getGameManagerIndex() {
        return getIntent().getIntExtra(EXTRA_GAME_TYPE_INDEX, -1);
    }

    private int getGameIndex() {
        return getIntent().getIntExtra(EXTRA_GAME_INDEX, -1);
    }

    public static Intent makeIntent(Context c, int gameIndex, int gameManagerIndex) {
        Intent intent = new Intent(c, CelebrationActivity.class);
        intent.putExtra(EXTRA_GAME_INDEX, gameIndex);
        intent.putExtra(EXTRA_GAME_TYPE_INDEX, gameManagerIndex);
        return intent;
    }
}