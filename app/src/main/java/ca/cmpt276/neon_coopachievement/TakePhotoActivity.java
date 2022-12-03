package ca.cmpt276.neon_coopachievement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ca.cmpt276.neon_coopachievement.model.Game;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

public class TakePhotoActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1234;
    private static final int CAPTURE_CODE = 1001;

    GameCategory gameCategory = GameCategory.getInstance();
    GameManager gameManager;
    Game game;
    ImageView iv;

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

        iv = findViewById(R.id.ivPhoto);

        gameManager = gameCategory.getGameManager(getGameManagerIndex());
        if(!getIsManager()) {
            game = gameManager.getGame(getGameIndex());
            try {
                setupGameImage(game);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "FILE NOT FOUND, TAKE NEW PHOTO",
                        Toast.LENGTH_SHORT).show();
                game.setImagePath(null);
            }
        } else {
            try {
                setupManagerImage(gameManager);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setupButton();
    }

    private void setupGameImage(Game game) throws IOException {
        if (game.getImagePath() != null) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(TakePhotoActivity.this.getContentResolver(), getImageUri(game.getImagePath()));
            iv.setImageBitmap(bitmap);
        }
    }

    private void setupManagerImage(GameManager gameManager) throws IOException {
        if (gameManager.getImagePath() != null) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(TakePhotoActivity.this.getContentResolver(), getImageUri(gameManager.getImagePath()));
            iv.setImageBitmap(bitmap);
        }
    }

    private void setupButton() {
        Button takePic = findViewById(R.id.btnTakePhoto);
        takePic.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText
                        (TakePhotoActivity.this,
                                "Allow Permission to use Camera",
                                Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            iv.setImageBitmap(bitmap);

            if (!getIsManager()) {
                game = gameManager.getGame(getGameIndex());
                game.setImagePath(getImageUriPath(TakePhotoActivity.this, bitmap));
            } else {
                gameManager.setImagePath(getImageUriPath(TakePhotoActivity.this, bitmap));
            }
        }
    }


    public String getImageUriPath(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return path;
    }

    public Uri getImageUri(String path) {
        return Uri.parse(path);
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
