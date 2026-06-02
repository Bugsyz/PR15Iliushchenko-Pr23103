package com.example.pr15_23101_fi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

public class PhotoActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        ScaleGestureDetector.OnScaleGestureListener {

    private ImageView ivPhotoView;
    private Button btnClose, btnDelete;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;

    private String photoPath;
    private float scaleFactor = 1.0f;
    private static final float MAX_SCALE = 2.0f;
    private static final float MIN_SCALE = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        photoPath = getIntent().getStringExtra("photo_path");

        ivPhotoView = findViewById(R.id.iv_photo_view);
        btnClose = findViewById(R.id.btn_close);
        btnDelete = findViewById(R.id.btn_delete);

        loadPhoto();

        gestureDetector = new GestureDetector(this, this);
        gestureDetector.setOnDoubleTapListener(this);
        scaleGestureDetector = new ScaleGestureDetector(this, this);

        btnClose.setOnClickListener(v -> closePhoto());
        btnDelete.setOnClickListener(v -> deletePhoto());

        ivPhotoView.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }

    private void loadPhoto() {
        if (photoPath != null && !photoPath.isEmpty()) {
            File file = new File(photoPath);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                ivPhotoView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(this, "Фотография не найдена", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Путь к фотографии не указан", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void closePhoto() {
        Intent intent = new Intent(PhotoActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void deletePhoto() {
        if (photoPath != null && !photoPath.isEmpty()) {
            File file = new File(photoPath);
            if (file.exists()) {
                if (file.delete()) {
                    Toast.makeText(this, "Фотография удалена", Toast.LENGTH_SHORT).show();
                    closePhoto();
                } else {
                    Toast.makeText(this, "Ошибка при удалении", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) { return true; }
    @Override
    public void onShowPress(@NonNull MotionEvent e) {}
    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) { return false; }
    @Override
    public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) { return false; }
    @Override
    public void onLongPress(@NonNull MotionEvent e) {}
    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        float deltaX = e2.getX() - e1.getX();
        float absDeltaX = Math.abs(deltaX);
        float absVelocityX = Math.abs(velocityX);

        if (absVelocityX > 100 && absDeltaX > 100) {
            if (deltaX > 0) {
                closePhoto();
            } else {
                deletePhoto();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(@NonNull MotionEvent e) { return false; }
    @Override
    public boolean onDoubleTap(@NonNull MotionEvent e) {
        if (scaleFactor == MIN_SCALE) {
            scaleFactor = MAX_SCALE;
        } else {
            scaleFactor = MIN_SCALE;
        }
        ivPhotoView.setScaleX(scaleFactor);
        ivPhotoView.setScaleY(scaleFactor);
        return true;
    }
    @Override
    public boolean onDoubleTapEvent(@NonNull MotionEvent e) { return false; }

    @Override
    public boolean onScale(@NonNull ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        scaleFactor = Math.max(MIN_SCALE, Math.min(scaleFactor, MAX_SCALE));
        ivPhotoView.setScaleX(scaleFactor);
        ivPhotoView.setScaleY(scaleFactor);
        return true;
    }
    @Override
    public boolean onScaleBegin(@NonNull ScaleGestureDetector detector) { return true; }
    @Override
    public void onScaleEnd(@NonNull ScaleGestureDetector detector) {}
}