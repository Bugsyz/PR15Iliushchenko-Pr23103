package com.example.pr15_23101_fi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String BASE_URL = "https://mskko2021.mad.hakta.pro/api/";

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private SharedPreferences prefs;
    private ApiService apiService;

    private static final String PREFS_NAME = "MeditationAppPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_NICKNAME = "user_nickname";
    private static final String KEY_AVATAR_URL = "user_avatar_url";
    private static final String KEY_LAST_EMAIL = "last_email";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: Login Activity started");

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        if (prefs.getBoolean(KEY_IS_LOGGED_IN, false)) {
            Log.d(TAG, "User already logged in, navigating to Main");
            navigateToMain();
            return;
        }

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login_go);
        tvRegister = findViewById(R.id.tv_register_link);

        String lastEmail = prefs.getString(KEY_LAST_EMAIL, "");
        if (!lastEmail.isEmpty()) {
            etEmail.setText(lastEmail);
            Log.d(TAG, "Last email restored: " + lastEmail);
        }

        tvRegister.setOnClickListener(v -> {
            Log.d(TAG, "Register button clicked");
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            Log.d(TAG, "Login button clicked, email: " + email);

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this,
                        getString(R.string.error_fill_all_fields),
                        Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Validation failed: empty fields");
                return;
            }

            if (!email.contains("@")) {
                Toast.makeText(LoginActivity.this,
                        getString(R.string.error_invalid_email),
                        Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Validation failed: invalid email");
                return;
            }

            btnLogin.setEnabled(false);
            btnLogin.setText(getString(R.string.logging_in));

            performLoginRequest(email, password);
        });

        Button btnProfile = findViewById(R.id.btn_profile_go);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                if (prefs.getBoolean(KEY_IS_LOGGED_IN, false)) {
                    navigateToMain();
                } else {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.error_login_first),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void performLoginRequest(String email, String password) {
        Log.d(TAG, "Performing login request for: " + email);

        apiService.login(new LoginRequest(email, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse data = response.body();
                    Log.d(TAG, "Login successful, token received");

                    saveAuthData(data.getToken(), email, data.getNickName(), data.getAvatar());
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.login_success),
                            Toast.LENGTH_SHORT).show();
                    navigateToMain();
                } else {
                    Log.e(TAG, "Login failed: " + response.code());
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.login_failed),
                            Toast.LENGTH_LONG).show();
                    btnLogin.setEnabled(true);
                    btnLogin.setText(getString(R.string.sign_in));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                Toast.makeText(LoginActivity.this,
                        getString(R.string.error_network) + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                btnLogin.setEnabled(true);
                btnLogin.setText(getString(R.string.sign_in));
            }
        });
    }

    private void saveAuthData(String token, String email, String nickName, String avatarUrl) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_NICKNAME, nickName);
        editor.putString(KEY_AVATAR_URL, avatarUrl);
        editor.putString(KEY_LAST_EMAIL, email); // Сохраняем для следующего входа
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
        Log.d(TAG, "Auth data saved for: " + nickName);
    }

    private void navigateToMain() {
        Log.d(TAG, "Navigating to MainActivity");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}