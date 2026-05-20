package com.example.mealrecommendationapp.ui.home;

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
import com.example.mealrecommendationapp.ui.welcome.WelcomeActivity;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView btnMenu;
    private EditText edtUsername;
    private EditText edtGender;
    private EditText edtAge;
    private EditText edtWeight;
    private EditText edtHeight;
    private Button btnSave;
    private Button btnLogout;

    private String originalDob = "2000-01-01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        loadUser();
        setupButtons();
    }

    private void initViews() {
        btnMenu = findViewById(R.id.btnMenu);
        edtUsername = findViewById(R.id.edtUsername);
        edtGender = findViewById(R.id.edtGender);
        edtAge = findViewById(R.id.edtAge);
        edtWeight = findViewById(R.id.edtWeight);
        edtHeight = findViewById(R.id.edtHeight);
        btnSave = findViewById(R.id.btnSaveProfile);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void loadUser() {
        ApiClient.getService(this).getProfile()
                .enqueue(new Callback<ApiService.ApiResponse<ApiService.ProfileData>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<ApiService.ProfileData>> call,
                                           Response<ApiService.ApiResponse<ApiService.ProfileData>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            ApiService.ProfileData profile = response.body().getData();
                            
                            edtUsername.setText(profile.getName());
                            edtGender.setText(profile.getGender());
                            edtWeight.setText(String.valueOf(profile.getWeightKg()));
                            edtHeight.setText(String.valueOf(profile.getHeightCm()));
                            
                            originalDob = profile.getDateOfBirth();
                            if (originalDob != null && originalDob.contains("-")) {
                                try {
                                    int birthYear = Integer.parseInt(originalDob.split("-")[0]);
                                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                                    edtAge.setText(String.valueOf(currentYear - birthYear));
                                } catch (Exception e) {
                                    edtAge.setText("25");
                                }
                            } else {
                                edtAge.setText("25");
                            }
                        } else {
                            Toast.makeText(ProfileActivity.this, "Không thể tải thông tin profile", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<ApiService.ProfileData>> call, Throwable t) {
                        Toast.makeText(ProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupButtons() {
        btnMenu.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String name = edtUsername.getText().toString().trim();
            String gender = edtGender.getText().toString().trim().toLowerCase();
            String ageStr = edtAge.getText().toString().trim();
            String weightStr = edtWeight.getText().toString().trim();
            String heightStr = edtHeight.getText().toString().trim();

            if (name.isEmpty() || gender.isEmpty() || ageStr.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = Integer.parseInt(ageStr);
            int weight = Integer.parseInt(weightStr);
            int height = Integer.parseInt(heightStr);

            // Reconstruct a dateOfBirth based on the inputted age
            String dob = originalDob;
            if (originalDob != null && originalDob.contains("-")) {
                try {
                    String[] parts = originalDob.split("-");
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    int birthYear = currentYear - age;
                    dob = String.format(java.util.Locale.US, "%04d-%s-%s", birthYear, parts[1], parts[2]);
                } catch (Exception e) {
                    dob = String.format(java.util.Locale.US, "%04d-01-01", Calendar.getInstance().get(Calendar.YEAR) - age);
                }
            } else {
                dob = String.format(java.util.Locale.US, "%04d-01-01", Calendar.getInstance().get(Calendar.YEAR) - age);
            }

            btnSave.setEnabled(false);
            btnSave.setText("Saving...");

            ApiService.UpdateProfileRequest request = new ApiService.UpdateProfileRequest(
                    name, gender, dob, height, weight
            );

            ApiClient.getService(this).updateProfile(request)
                    .enqueue(new Callback<ApiService.ApiResponse<ApiService.ProfileData>>() {
                        @Override
                        public void onResponse(Call<ApiService.ApiResponse<ApiService.ProfileData>> call,
                                               Response<ApiService.ApiResponse<ApiService.ProfileData>> response) {
                            btnSave.setEnabled(true);
                            btnSave.setText("Save Changes");

                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                Toast.makeText(ProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Lỗi khi cập nhật profile", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiService.ApiResponse<ApiService.ProfileData>> call, Throwable t) {
                            btnSave.setEnabled(true);
                            btnSave.setText("Save Changes");
                            Toast.makeText(ProfileActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnLogout.setOnClickListener(v -> {
            ApiClient.getService(ProfileActivity.this).logout().enqueue(new Callback<ApiService.ApiResponse<Void>>() {
                @Override
                public void onResponse(Call<ApiService.ApiResponse<Void>> call, Response<ApiService.ApiResponse<Void>> response) {
                    proceedLogout();
                }

                @Override
                public void onFailure(Call<ApiService.ApiResponse<Void>> call, Throwable t) {
                    proceedLogout();
                }
            });
        });
    }

    private void proceedLogout() {
        SharedPreferencesHelper.clear(ProfileActivity.this);
        Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}