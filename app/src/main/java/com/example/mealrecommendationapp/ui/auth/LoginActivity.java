package com.example.mealrecommendationapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.data.network.ApiClient;
import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.data.network.SharedPreferencesHelper;
import com.example.mealrecommendationapp.ui.home.HomeActivity;
import com.example.mealrecommendationapp.ui.survey.SurveyGenderActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnSignIn;
    private TextView txtSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupClicks();
    }

    private void initViews() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        txtSignUp = findViewById(R.id.txtSignUp);
    }

    private void setupClicks() {
        txtSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnSignIn.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            btnSignIn.setEnabled(false);
            btnSignIn.setText("Đang đăng nhập...");

            ApiClient.getService(this).login(new ApiService.LoginRequest(email, password))
                    .enqueue(new Callback<ApiService.ApiResponse<ApiService.AuthData>>() {
                        @Override
                        public void onResponse(Call<ApiService.ApiResponse<ApiService.AuthData>> call,
                                               Response<ApiService.ApiResponse<ApiService.AuthData>> response) {
                            btnSignIn.setEnabled(true);
                            btnSignIn.setText("Sign in");

                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                ApiService.AuthData authData = response.body().getData();
                                SharedPreferencesHelper.saveTokens(LoginActivity.this, authData.getAccessToken(), authData.getRefreshToken());
                                SharedPreferencesHelper.saveUserInfo(LoginActivity.this, email, "");

                                fetchProfileAndNavigate();
                            } else {
                                String errorMsg = "Sai email hoặc mật khẩu";
                                if (response.body() != null && response.body().getError() != null) {
                                    errorMsg = response.body().getError().getMessage();
                                }
                                Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiService.ApiResponse<ApiService.AuthData>> call, Throwable t) {
                            btnSignIn.setEnabled(true);
                            btnSignIn.setText("Sign in");
                            Toast.makeText(LoginActivity.this, "Không thể kết nối đến server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void fetchProfileAndNavigate() {
        ApiClient.getService(this).getProfile().enqueue(new Callback<ApiService.ApiResponse<ApiService.ProfileData>>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse<ApiService.ProfileData>> call,
                                   Response<ApiService.ApiResponse<ApiService.ProfileData>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ApiService.ProfileData profile = response.body().getData();
                    SharedPreferencesHelper.saveUserInfo(LoginActivity.this, profile.getEmail(), profile.getName());

                    if (profile.isOnboardingComplete()) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, SurveyGenderActivity.class));
                    }
                    finish();
                } else {
                    // Profile not found or needs onboarding
                    startActivity(new Intent(LoginActivity.this, SurveyGenderActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse<ApiService.ProfileData>> call, Throwable t) {
                // If profile fails, go to onboarding as a fallback
                startActivity(new Intent(LoginActivity.this, SurveyGenderActivity.class));
                finish();
            }
        });
    }
}