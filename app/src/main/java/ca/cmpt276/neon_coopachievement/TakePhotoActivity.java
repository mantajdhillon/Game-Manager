package ca.cmpt276.neon_coopachievement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import ca.cmpt276.neon_coopachievement.model.Game;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

public class TakePhotoActivity extends AppCompatActivity {

    GameCategory gameCategory = GameCategory.getInstance();

    public static final String EXTRA_GAME_INDEX = "Game index";
    public static final String EXTRA_GAME_TYPE_INDEX = "Game type index";
    public static final String EXTRA_IS_MANAGER = "is Manager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.take_game_photo_title);
        ab.setDisplayHomeAsUpEnabled(true);

        setupButton();

        if (ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
    }

    private void setupGameImage(Game game, Bitmap bitmap) {
        ImageView gamePic = findViewById(R.id.ivPhoto);
        gamePic.setImageBitmap(bitmap);
    }

    private void setupManagerImage(GameManager gameManager, Bitmap bitmap) {
        ImageView managerPic = findViewById(R.id.ivPhoto);
        managerPic.setImageBitmap(bitmap);

    }

    private void setupButton() {
        Button takePic = findViewById(R.id.btnTakePhoto);
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //getResult.launch(intent);
                //Method in Video is not in Android Studio Tomorrow
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            if(getIsManager() == true) {
//                GameManager gameManager = gameCategory.getGameManager(getGameManagerIndex());
//                setupManagerImage(gameManager, bitmap);
//            } else {
//                Game game = gameCategory.getGameManager(getGameManagerIndex()).getGame(getGameIndex());
//                setupGameImage(game, bitmap);
//            }
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean getIsManager() {
        return getIntent().getBooleanExtra(EXTRA_IS_MANAGER, false);
    }

    private int getGameManagerIndex() {
        return getIntent().getIntExtra(EXTRA_GAME_TYPE_INDEX, -1);
    }

    private int getGameIndex() {
        return getIntent().getIntExtra(EXTRA_GAME_INDEX, -1);
    }

    public static Intent makeLaunchIntent(Context c, int gameManagerIndex, int gameIndex, boolean isManager) {
        Intent intent = new Intent(c, TakePhotoActivity.class);
        intent.putExtra(EXTRA_IS_MANAGER, isManager);
        intent.putExtra(EXTRA_GAME_INDEX, gameIndex);
        intent.putExtra(EXTRA_GAME_TYPE_INDEX, gameManagerIndex);
        return intent;
    }

}