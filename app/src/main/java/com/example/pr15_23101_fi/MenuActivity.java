package com.example.pr15_23101_fi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private static final String PREFS_NAME = "MeditationAppPrefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        CardView cardProfile = findViewById(R.id.card_profile);
        CardView cardFeelings = findViewById(R.id.card_feelings);
        CardView cardMeditations = findViewById(R.id.card_meditations);
        CardView cardSettings = findViewById(R.id.card_settings);
        CardView cardLogout = findViewById(R.id.card_logout);

        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });

        cardFeelings.setOnClickListener(v -> {
            Toast.makeText(this, "Переход к ощущениям", Toast.LENGTH_SHORT).show();
        });

        cardMeditations.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ListenActivity.class);
            startActivity(intent);
        });

        cardSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show();
        });

        cardSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        cardLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_IS_LOGGED_IN, false);
            editor.apply();

            Toast.makeText(this, "Выход выполнен", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        ImageView ivNavHome = findViewById(R.id.iv_nav_home);
        ImageView ivNavFeelings = findViewById(R.id.iv_nav_feelings);
        ImageView ivNavProfile = findViewById(R.id.iv_nav_profile);

        ivNavHome.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        ivNavFeelings.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ListenActivity.class);
            startActivity(intent);
        });

        ivNavProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });
    }
}