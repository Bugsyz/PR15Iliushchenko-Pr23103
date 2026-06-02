package com.example.pr15_23101_fi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private ImageView ivAvatar;
    private TextView tvName, tvLogout;
    private Button btnAddPhoto;
    private RecyclerView rvGallery;
    private SharedPreferences prefs;
    private PhotoAdapter photoAdapter;
    private ArrayList<PhotoItem> photoList;

    private static final String PREFS_NAME = "MeditationAppPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_NICKNAME = "user_nickname";
    private static final String KEY_AVATAR_URL = "user_avatar_url";
    private static final String KEY_LAST_EMAIL = "last_email";
    private static final String PHOTOS_DIR = "user_photos";

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        ivAvatar = findViewById(R.id.iv_profile_avatar);
        tvName = findViewById(R.id.tv_profile_name);
        tvLogout = findViewById(R.id.tv_logout);
        btnAddPhoto = findViewById(R.id.btn_add_photo);
        rvGallery = findViewById(R.id.rv_photo_gallery);

        loadUserData();

        photoList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(photoList, this);
        rvGallery.setLayoutManager(new GridLayoutManager(this, 2));
        rvGallery.setAdapter(photoAdapter);

        loadSavedPhotos();

        btnAddPhoto.setOnClickListener(v -> openGallery());

        photoAdapter.setOnItemClickListener(photoItem -> {
            Intent intent = new Intent(ProfileActivity.this, PhotoActivity.class);
            intent.putExtra("photo_path", photoItem.getImagePath());
            startActivity(intent);
        });

        tvLogout.setOnClickListener(v -> logout());

        findViewById(R.id.iv_nav_home).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.iv_nav_feelings).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ListenActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.iv_nav_profile).setOnClickListener(v -> {
            Toast.makeText(this, "Вы в профиле", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserData() {
        String nickname = prefs.getString(KEY_NICKNAME, "Пользователь");
        String avatarUrl = prefs.getString(KEY_AVATAR_URL, "");

        tvName.setText(nickname);
        ivAvatar.setImageResource(R.drawable.default_avatar);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadSavedPhotos() {
        File photosDir = new File(getFilesDir(), PHOTOS_DIR);
        if (photosDir.exists()) {
            File[] files = photosDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    long lastModified = file.lastModified();
                    String timestamp = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                            .format(new Date(lastModified));
                    photoList.add(new PhotoItem(file.getAbsolutePath(), timestamp));
                }
                photoAdapter.notifyDataSetChanged();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                savePhotoToInternalStorage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void savePhotoToInternalStorage(Bitmap bitmap) throws IOException {
        File photosDir = new File(getFilesDir(), PHOTOS_DIR);
        if (!photosDir.exists()) {
            photosDir.mkdirs();
        }

        String fileName = "photo_" + System.currentTimeMillis() + ".jpg";
        File file = new File(photosDir, fileName);

        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
        fos.flush();
        fos.close();

        String timestamp = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                .format(new Date());
        photoList.add(new PhotoItem(file.getAbsolutePath(), timestamp));
        photoAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Фото сохранено", Toast.LENGTH_SHORT).show();
    }

    public void logout() {
        String lastEmail = prefs.getString("user_email", "");

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_TOKEN);
        editor.putBoolean("is_logged_in", false);
        editor.putString(KEY_LAST_EMAIL, lastEmail);
        editor.apply();

        Toast.makeText(this, "Выход выполнен", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}