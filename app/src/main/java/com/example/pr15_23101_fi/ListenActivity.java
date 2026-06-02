package com.example.pr15_23101_fi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ListenActivity extends AppCompatActivity {

    private ImageView ivPlayPause;
    private SeekBar seekBarProgress;
    private TextView tvCurrentTime, tvTotalTime;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);

        ivPlayPause = findViewById(R.id.iv_play_pause);
        seekBarProgress = findViewById(R.id.seek_bar_progress);
        tvCurrentTime = findViewById(R.id.tv_current_time);
        tvTotalTime = findViewById(R.id.tv_total_time);

        ivPlayPause.setOnClickListener(v -> {
            isPlaying = !isPlaying;
            if (isPlaying) {
                ivPlayPause.setImageResource(R.drawable.ic_pause);
                Toast.makeText(this, "Воспроизведение...", Toast.LENGTH_SHORT).show();
            } else {
                ivPlayPause.setImageResource(R.drawable.ic_play);
                Toast.makeText(this, "Пауза", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.iv_rewind).setOnClickListener(v -> {
            int progress = seekBarProgress.getProgress();
            if (progress >= 10) {
                seekBarProgress.setProgress(progress - 10);
            }
            Toast.makeText(this, "Назад 10 сек", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.iv_forward).setOnClickListener(v -> {
            int progress = seekBarProgress.getProgress();
            if (progress <= 50) {
                seekBarProgress.setProgress(progress + 10);
            }
            Toast.makeText(this, "Вперёд 10 сек", Toast.LENGTH_SHORT).show();
        });

        seekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvCurrentTime.setText(formatTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        findViewById(R.id.iv_nav_home).setOnClickListener(v -> {
            Intent intent = new Intent(ListenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.iv_nav_feelings).setOnClickListener(v -> {
            Toast.makeText(this, "Вы в прослушивании", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.iv_nav_profile).setOnClickListener(v -> {
            Intent intent = new Intent(ListenActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @SuppressLint("DefaultLocale")
    private String formatTime(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}