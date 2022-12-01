package ca.cmpt276.neon_coopachievement;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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

import ca.cmpt276.neon_coopachievement.model.Game;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

public class TakePhotoActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1234;
    private static final int CAPTURE_CODE = 1001;
    GameCategory gameCategory = GameCategory.getInstance();
    Uri image_uri;
    ImageView iv = findViewById(R.id.ivPhoto);

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
        takePic.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                openCamera();
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

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "new image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "from the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(camIntent, CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
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
        if (resultCode == RESULT_OK) {
            iv.setImageURI(image_uri);
        }
    }
}
