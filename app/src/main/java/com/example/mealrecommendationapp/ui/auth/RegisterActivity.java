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
import com.example.mealrecommendationapp.ui.survey.SurveyGenderActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPassword;
    private TextView txtSignIn;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupClicks();
    }

    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        txtSignIn = findViewById(R.id.txtSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    private void setupClicks() {
        txtSignIn.setOnClickListener(v -> finish());

        btnSignUp.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            btnSignUp.setEnabled(false);
            btnSignUp.setText("Đang đăng ký...");

            ApiClient.getService(this).register(new ApiService.RegisterRequest(email, password, name))
                    .enqueue(new Callback<ApiService.ApiResponse<ApiService.AuthData>>() {
                        @Override
                        public void onResponse(Call<ApiService.ApiResponse<ApiService.AuthData>> call,
                                               Response<ApiService.ApiResponse<ApiService.AuthData>> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                performSilentLogin(email, password, name);
                            } else {
                                btnSignUp.setEnabled(true);
                                btnSignUp.setText("Sign up");
                                String errorMsg = "Email đã tồn tại hoặc không hợp lệ";
                                if (response.body() != null && response.body().getError() != null) {
                                    errorMsg = response.body().getError().getMessage();
                                }
                                Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiService.ApiResponse<ApiService.AuthData>> call, Throwable t) {
                            btnSignUp.setEnabled(true);
                            btnSignUp.setText("Sign up");
                            Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void performSilentLogin(String email, String password, String name) {
        ApiClient.getService(this).login(new ApiService.LoginRequest(email, password))
                .enqueue(new Callback<ApiService.ApiResponse<ApiService.AuthData>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<ApiService.AuthData>> call,
                                           Response<ApiService.ApiResponse<ApiService.AuthData>> response) {
                        btnSignUp.setEnabled(true);
                        btnSignUp.setText("Sign up");

                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            ApiService.AuthData authData = response.body().getData();
                            SharedPreferencesHelper.saveTokens(RegisterActivity.this, authData.getAccessToken(), authData.getRefreshToken());
                            SharedPreferencesHelper.saveUserInfo(RegisterActivity.this, email, name);

                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, SurveyGenderActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công nhưng lỗi đăng nhập tự động", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<ApiService.AuthData>> call, Throwable t) {
                        btnSignUp.setEnabled(true);
                        btnSignUp.setText("Sign up");
                        Toast.makeText(RegisterActivity.this, "Lỗi kết nối sau đăng ký", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}