package com.example.mealrecommendationapp.ui.survey;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.data.network.ApiClient;
import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.ui.custom.RulerView;
import com.example.mealrecommendationapp.ui.home.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyHeightWeightActivity
        extends AppCompatActivity {

    private RulerView rulerHeight;
    private RulerView rulerWeight;

    private TextView txtHeight;
    private TextView txtWeight;

    private Button btnNext;

    private String gender;

    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_survey_height_weight
        );

        gender =
                getIntent().getStringExtra(
                        "gender"
                );

        age =
                getIntent().getIntExtra(
                        "age",
                        20
                );

        initViews();

        setupRulers();

        setupNext();
    }

    private void initViews() {

        rulerHeight =
                findViewById(R.id.rulerHeight);

        rulerWeight =
                findViewById(R.id.rulerWeight);

        txtHeight =
                findViewById(R.id.txtHeight);

        txtWeight =
                findViewById(R.id.txtWeight);

        btnNext =
                findViewById(R.id.btnNext);

        if (
                rulerHeight == null
                        ||
                        rulerWeight == null
                        ||
                        txtHeight == null
                        ||
                        txtWeight == null
        ) {

            Toast.makeText(
                    this,
                    "Layout binding error!",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void setupRulers() {

        txtHeight.setText(
                getString(
                        R.string.height_format,
                        170
                )
        );

        txtWeight.setText(
                getString(
                        R.string.weight_format,
                        60
                )
        );

        rulerHeight.setRange(
                120,
                220,
                170
        );

        rulerHeight.setOnValueChangeListener(value ->

                txtHeight.setText(
                        getString(
                                R.string.height_format,
                                value
                        )
                )
        );

        rulerWeight.setRange(
                30,
                150,
                60
        );

        rulerWeight.setOnValueChangeListener(value ->

                txtWeight.setText(
                        getString(
                                R.string.weight_format,
                                value
                        )
                )
        );
    }

    private void setupNext() {

        btnNext.setOnClickListener(v -> {

            int height =
                    rulerHeight.getCurrentValue();

            int weight =
                    rulerWeight.getCurrentValue();

            String dob = getIntent().getStringExtra("dateOfBirth");
            if (dob == null) {
                dob = "2000-01-01";
            }

            btnNext.setEnabled(false);
            btnNext.setText("Saving...");

            String finalGender = "male";
            if (gender != null) {
                finalGender = gender.toLowerCase();
            }

            java.util.List<String> emptyList = new java.util.ArrayList<>();
            ApiService.OnboardingRequest req = new ApiService.OnboardingRequest(
                    finalGender,
                    dob,
                    height,
                    weight,
                    emptyList,
                    emptyList,
                    emptyList
            );

            ApiClient.getService(this).onboarding(req)
                    .enqueue(new Callback<ApiService.ApiResponse<ApiService.ProfileData>>() {
                        @Override
                        public void onResponse(Call<ApiService.ApiResponse<ApiService.ProfileData>> call,
                                               Response<ApiService.ApiResponse<ApiService.ProfileData>> response) {
                            btnNext.setEnabled(true);
                            btnNext.setText("Next");

                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                Toast.makeText(SurveyHeightWeightActivity.this, "Onboarding hoàn thành!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SurveyHeightWeightActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                String errorMsg = "Lỗi khi lưu thông tin";
                                if (response.body() != null && response.body().getError() != null) {
                                    errorMsg = response.body().getError().getMessage();
                                }
                                Toast.makeText(SurveyHeightWeightActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiService.ApiResponse<ApiService.ProfileData>> call, Throwable t) {
                            btnNext.setEnabled(true);
                            btnNext.setText("Next");
                            Toast.makeText(SurveyHeightWeightActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}