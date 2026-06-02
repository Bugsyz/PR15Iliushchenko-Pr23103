package com.example.pr15_23101_fi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        btnLogin = findViewById(R.id.btn_onboarding_login);
        tvRegister = findViewById(R.id.tv_onboarding_register);

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(OnboardingActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}