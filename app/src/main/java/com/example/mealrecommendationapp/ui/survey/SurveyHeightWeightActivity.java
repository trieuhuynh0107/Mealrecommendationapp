package com.example.mealrecommendationapp.ui.survey;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mealrecommendationapp.data.AuthRepository;
import com.example.mealrecommendationapp.data.RepositoryCallback;
import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.databinding.ActivitySurveyHeightWeightBinding;
import com.example.mealrecommendationapp.ui.home.HomeActivity;
import java.util.ArrayList;
import java.util.List;

public class SurveyHeightWeightActivity extends AppCompatActivity {

    private ActivitySurveyHeightWeightBinding binding;
    private AuthRepository authRepository;

    private String gender;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySurveyHeightWeightBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = AuthRepository.getInstance();

        gender = getIntent().getStringExtra("gender");
        age = getIntent().getIntExtra("age", 20);

        setupRulers();
        setupNext();
    }

    private void setupRulers() {
        binding.txtHeight.setText(getString(com.example.mealrecommendationapp.R.string.height_format, 170));
        binding.txtWeight.setText(getString(com.example.mealrecommendationapp.R.string.weight_format, 60));

        binding.rulerHeight.setRange(120, 220, 170);
        binding.rulerHeight.setOnValueChangeListener(value ->
                binding.txtHeight.setText(getString(com.example.mealrecommendationapp.R.string.height_format, value))
        );

        binding.rulerWeight.setRange(30, 150, 60);
        binding.rulerWeight.setOnValueChangeListener(value ->
                binding.txtWeight.setText(getString(com.example.mealrecommendationapp.R.string.weight_format, value))
        );
    }

    private void setupNext() {
        binding.btnNext.setOnClickListener(v -> {
            int height = binding.rulerHeight.getCurrentValue();
            int weight = binding.rulerWeight.getCurrentValue();

            String dob = getIntent().getStringExtra("dateOfBirth");
            if (dob == null) {
                dob = "2000-01-01";
            }

            binding.btnNext.setEnabled(false);
            binding.btnNext.setText("Saving...");

            String finalGender = "male";
            if (gender != null) {
                finalGender = gender.toLowerCase();
            }

            List<String> emptyList = new ArrayList<>();
            ApiService.OnboardingRequest req = new ApiService.OnboardingRequest(
                    finalGender,
                    dob,
                    height,
                    weight,
                    emptyList,
                    emptyList,
                    emptyList
            );

            authRepository.onboarding(this, req, new RepositoryCallback<ApiService.ProfileData>() {
                @Override
                public void onSuccess(ApiService.ProfileData result) {
                    binding.btnNext.setEnabled(true);
                    binding.btnNext.setText("Next");

                    Toast.makeText(SurveyHeightWeightActivity.this, "Onboarding hoàn thành!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SurveyHeightWeightActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String errorMessage) {
                    binding.btnNext.setEnabled(true);
                    binding.btnNext.setText("Next");
                    Toast.makeText(SurveyHeightWeightActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}