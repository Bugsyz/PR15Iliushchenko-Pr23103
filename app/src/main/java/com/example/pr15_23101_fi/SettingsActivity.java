package com.example.pr15_23101_fi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup rgLanguage;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "MeditationAppPrefs";
    private static final String KEY_LANGUAGE = "app_language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        rgLanguage = findViewById(R.id.rg_language);

        String savedLanguage = prefs.getString(KEY_LANGUAGE, "ru");
        if (savedLanguage.equals("en")) {
            rgLanguage.check(R.id.rb_english);
        } else {
            rgLanguage.check(R.id.rb_russian);
        }

        rgLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_english) {
                setLocale("en");
            } else {
                setLocale("ru");
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Context context = this;
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LANGUAGE, languageCode);
        editor.apply();

        Toast.makeText(this, "Язык изменён / Language changed", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}