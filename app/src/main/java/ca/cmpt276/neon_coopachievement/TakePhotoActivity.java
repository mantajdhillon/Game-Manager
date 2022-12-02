package ca.cmpt276.neon_coopachievement;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.cmpt276.neon_coopachievement.model.Game;
import ca.cmpt276.neon_coopachievement.model.GameCategory;
import ca.cmpt276.neon_coopachievement.model.GameManager;

public class TakePhotoActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1234;
    private static final int CAPTURE_CODE = 1001;
    GameCategory gameCategory = GameCategory.getInstance();
    GameManager gameManager;
    String currentPhotoPath;

    Game game;
    Uri image_uri;
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

        gameManager = gameCategory.getGameManager(getGameManagerIndex());
        if(!getIsManager()) {
            game = gameManager.getGame(getGameIndex());
            try {
                setupGameImage(game);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        iv = findViewById(R.id.ivPhoto);

        setupButton();


    }

    private void setupGameImage(Game game) throws IOException {
        File imageFile = game.getImageFile();
        if (imageFile != null) {
            String filePath = this.getCacheDir().getAbsolutePath() + "/image_" + getGameManagerIndex() + "_" + getGameIndex();
            filePath += ".png";
            Toast.makeText(this, filePath, Toast.LENGTH_LONG).show();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            iv.setImageBitmap(bitmap);
        }
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
                String fileName = "image_" + getGameManagerIndex() + "_" + getGameIndex();
                try {
                    game.setImageFile(convertBitmapToFile(bitmap, fileName));
                } catch (IOException e) {
                    Toast.makeText(TakePhotoActivity.this, "ERROR SAVING IMAGE TO GAME", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private File convertBitmapToFile(Bitmap bitmap, String fileName) throws IOException {
        File f = new File(this.getCacheDir(), fileName);
        f.createNewFile();

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapData = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapData);
        fos.flush();
        fos.close();
        return f;
    }
}
