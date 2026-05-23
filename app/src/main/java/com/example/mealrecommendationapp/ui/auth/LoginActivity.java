package com.example.mealrecommendationapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.data.network.SharedPreferencesHelper;
import com.example.mealrecommendationapp.databinding.ActivityLoginBinding;
import com.example.mealrecommendationapp.ui.home.HomeActivity;
import com.example.mealrecommendationapp.ui.survey.SurveyGenderActivity;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private ActivityLoginBinding binding;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new LoginPresenter(this);

        setupClicks();
    }

    private void setupClicks() {
        binding.txtSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.btnSignIn.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();
            presenter.login(this, email, password);
        });
    }

    @Override
    public void showLoading() {
        binding.btnSignIn.setEnabled(false);
        binding.btnSignIn.setText("Đang đăng nhập...");
    }

    @Override
    public void hideLoading() {
        binding.btnSignIn.setEnabled(true);
        binding.btnSignIn.setText("Sign in");
    }

    @Override
    public void onLoginSuccess(ApiService.AuthData authData) {
        String email = binding.edtEmail.getText().toString().trim();
        SharedPreferencesHelper.saveTokens(this, authData.getAccessToken(), authData.getRefreshToken());
        SharedPreferencesHelper.saveUserInfo(this, email, "");
        
        presenter.fetchProfile(this);
    }

    @Override
    public void onLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProfileLoaded(ApiService.ProfileData profile) {
        SharedPreferencesHelper.saveUserInfo(this, profile.getEmail(), profile.getName());

        if (profile.isOnboardingComplete()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        } else {
            startActivity(new Intent(LoginActivity.this, SurveyGenderActivity.class));
        }
        finish();
    }

    @Override
    public void onProfileLoadFailed() {
        // Fallback to onboarding if profile fails
        startActivity(new Intent(LoginActivity.this, SurveyGenderActivity.class));
        finish();
    }
}