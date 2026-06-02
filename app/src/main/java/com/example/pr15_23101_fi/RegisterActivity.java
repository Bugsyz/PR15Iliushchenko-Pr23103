package com.example.pr15_23101_fi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;
    private SharedPreferences prefs;

    private static final String PREFS_NAME = "MeditationAppPrefs";
    private static final String KEY_LAST_EMAIL = "last_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        etEmail = findViewById(R.id.et_reg_email);
        etPassword = findViewById(R.id.et_reg_password);
        etConfirmPassword = findViewById(R.id.et_reg_confirm_password);
        btnRegister = findViewById(R.id.btn_do_register);
        TextView tvLoginLink = findViewById(R.id.tv_login_link);

        tvLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.contains("@")) {
                Toast.makeText(RegisterActivity.this, "Некорректный email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                return;
            }

            btnRegister.setEnabled(false);
            btnRegister.setText("Регистрация...");

            performRegisterRequest(email, password);
        });
    }

    @SuppressLint("SetTextI18n")
    private void performRegisterRequest(String email, String password) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            boolean isSuccess = true;

            if (isSuccess) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_LAST_EMAIL, email);
                editor.apply();

                Toast.makeText(RegisterActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Ошибка регистрации. Попробуйте другой email.", Toast.LENGTH_LONG).show();
                btnRegister.setEnabled(true);
                btnRegister.setText("Register");
            }
        }, 1500);
    }
}