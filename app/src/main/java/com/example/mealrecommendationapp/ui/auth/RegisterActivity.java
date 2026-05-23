package com.example.mealrecommendationapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mealrecommendationapp.data.AuthRepository;
import com.example.mealrecommendationapp.data.RepositoryCallback;
import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.data.network.SharedPreferencesHelper;
import com.example.mealrecommendationapp.databinding.ActivityRegisterBinding;
import com.example.mealrecommendationapp.ui.survey.SurveyGenderActivity;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = AuthRepository.getInstance();

        setupClicks();
    }

    private void setupClicks() {
        binding.txtSignIn.setOnClickListener(v -> finish());

        binding.btnSignUp.setOnClickListener(v -> {
            String name = binding.edtName.getText().toString().trim();
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.btnSignUp.setEnabled(false);
            binding.btnSignUp.setText("Đang đăng ký...");

            authRepository.register(this, email, password, name, new RepositoryCallback<ApiService.AuthData>() {
                @Override
                public void onSuccess(ApiService.AuthData result) {
                    performSilentLogin(email, password, name);
                }

                @Override
                public void onError(String errorMessage) {
                    binding.btnSignUp.setEnabled(true);
                    binding.btnSignUp.setText("Sign up");
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void performSilentLogin(String email, String password, String name) {
        authRepository.login(this, email, password, new RepositoryCallback<ApiService.AuthData>() {
            @Override
            public void onSuccess(ApiService.AuthData authData) {
                binding.btnSignUp.setEnabled(true);
                binding.btnSignUp.setText("Sign up");

                SharedPreferencesHelper.saveTokens(RegisterActivity.this, authData.getAccessToken(), authData.getRefreshToken());
                SharedPreferencesHelper.saveUserInfo(RegisterActivity.this, email, name);

                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, SurveyGenderActivity.class));
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                binding.btnSignUp.setEnabled(true);
                binding.btnSignUp.setText("Sign up");
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công nhưng lỗi đăng nhập tự động", Toast.LENGTH_SHORT).show();
            }
        });
    }
}