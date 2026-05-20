package com.example.mealrecommendationapp.ui.survey;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.ui.home.RecommendationActivity;

import java.util.ArrayList;
import java.util.List;

public class SurveyHealthActivity
        extends AppCompatActivity {

    private Button btnNext;

    private String selectedTime;

    private int selectedDayIndex;

    private List<TextView> options =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_survey_health
        );

        selectedTime =
                getIntent().getStringExtra(
                        "selected_time"
                );

        selectedDayIndex =
                getIntent().getIntExtra(
                        "selected_day",
                        0
                );

        initViews();

        setupOptions();

        setupNext();
    }

    private void initViews() {

        btnNext = findViewById(R.id.btnNext);

        options.add(findViewById(R.id.optionDairyFree));
        options.add(findViewById(R.id.optionDiabetic));
        options.add(findViewById(R.id.optionEggFree));
        options.add(findViewById(R.id.optionGlutenFree));
        options.add(findViewById(R.id.optionHealthy));
        options.add(findViewById(R.id.optionHighFiber));
        options.add(findViewById(R.id.optionHighProtein));
        options.add(findViewById(R.id.optionLowCalorie));
        options.add(findViewById(R.id.optionLowCarb));
        options.add(findViewById(R.id.optionLowCholesterol));
        options.add(findViewById(R.id.optionLowSodium));
        options.add(findViewById(R.id.optionVegetarian));
        options.add(findViewById(R.id.optionNone));
    }

    private void setupOptions() {

        for (TextView option : options) {

            option.setOnClickListener(v -> {

                boolean selected =
                        v.isSelected();

                v.setSelected(!selected);

                if (!selected) {

                    v.setBackgroundResource(
                            R.drawable.bg_survey_selected
                    );

                } else {

                    v.setBackgroundResource(
                            R.drawable.bg_input
                    );
                }
            });
        }
    }

    private void setupNext() {

        btnNext.setOnClickListener(v -> {
            btnNext.setEnabled(false);
            btnNext.setText("Saving Preferences...");

            ArrayList<String> selectedCuisines = getIntent().getStringArrayListExtra("selected_cuisines");
            ArrayList<String> selectedAllergies = getIntent().getStringArrayListExtra("selected_allergies");

            ArrayList<String> selectedDietTags = new ArrayList<>();
            for (TextView option : options) {
                if (option.isSelected()) {
                    String tag = option.getText().toString();
                    if (!"None of them".equalsIgnoreCase(tag)) {
                        selectedDietTags.add(tag);
                    }
                }
            }

            // Call profile update API to save selections on the backend
            com.example.mealrecommendationapp.data.network.ApiClient.getService(this)
                    .updateProfile(new com.example.mealrecommendationapp.data.network.ApiService.UpdateProfileRequest(
                            selectedCuisines != null ? selectedCuisines : new ArrayList<>(),
                            selectedAllergies != null ? selectedAllergies : new ArrayList<>(),
                            selectedDietTags
                    ))
                    .enqueue(new retrofit2.Callback<com.example.mealrecommendationapp.data.network.ApiService.ApiResponse<com.example.mealrecommendationapp.data.network.ApiService.ProfileData>>() {
                        @Override
                        public void onResponse(retrofit2.Call<com.example.mealrecommendationapp.data.network.ApiService.ApiResponse<com.example.mealrecommendationapp.data.network.ApiService.ProfileData>> call,
                                               retrofit2.Response<com.example.mealrecommendationapp.data.network.ApiService.ApiResponse<com.example.mealrecommendationapp.data.network.ApiService.ProfileData>> response) {
                            navigateToRecommendations();
                        }

                        @Override
                        public void onFailure(retrofit2.Call<com.example.mealrecommendationapp.data.network.ApiService.ApiResponse<com.example.mealrecommendationapp.data.network.ApiService.ProfileData>> call, Throwable t) {
                            navigateToRecommendations();
                        }
                    });
        });
    }

    private void navigateToRecommendations() {
        Intent intent = new Intent(this, RecommendationActivity.class);
        intent.putExtra("selected_time", selectedTime);
        intent.putExtra("selected_day", selectedDayIndex);
        startActivity(intent);
        finish();
    }
}