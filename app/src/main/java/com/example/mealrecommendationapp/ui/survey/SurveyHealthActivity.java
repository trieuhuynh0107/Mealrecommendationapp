package com.example.mealrecommendationapp.ui.survey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.data.network.LocalCacheManager;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class SurveyHealthActivity extends AppCompatActivity {

    private Button btnNext;
    private FlexboxLayout flexboxDietTags;
    private LocalCacheManager cacheManager;

    private String selectedTime;
    private int selectedDayIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_health);

        selectedTime = getIntent().getStringExtra("selected_time");
        selectedDayIndex = getIntent().getIntExtra("selected_day", 0);

        cacheManager = new LocalCacheManager(this);
        initViews();
        setupNext();
    }

    private void initViews() {
        btnNext = findViewById(R.id.btnNext);
        flexboxDietTags = findViewById(R.id.flexboxDietTags);

        List<String> dietTags = new ArrayList<>(cacheManager.getDietTags());
        dietTags.add("None of them"); // Add fallback dynamically
        renderDietChips(dietTags);
    }

    private String formatForDisplay(String tag) {
        if (tag == null || tag.isEmpty()) return tag;
        
        // Handle specific cases nicely
        if ("eggs-dairy".equalsIgnoreCase(tag)) return "Eggs & Dairy";
        if ("soy-tofu".equalsIgnoreCase(tag)) return "Soy & Tofu";
        if ("peanut-butter".equalsIgnoreCase(tag)) return "Peanuts";
        
        String formatted = tag.replace("-", " ");
        StringBuilder sb = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : formatted.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                sb.append(c);
            } else if (capitalizeNext) {
                sb.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void renderDietChips(List<String> dietTags) {
        flexboxDietTags.removeAllViews();
        for (String tag : dietTags) {
            TextView chip = new TextView(this);
            chip.setText(formatForDisplay(tag));
            chip.setTag(tag); // Save raw tag in tag property!
            chip.setTextSize(14);
            chip.setPadding(28, 14, 28, 14);
            chip.setBackgroundResource(R.drawable.bg_input);
            chip.setTextColor(android.graphics.Color.parseColor("#222222"));

            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 8, 8, 8);
            chip.setLayoutParams(params);

            chip.setOnClickListener(v -> {
                boolean selected = v.isSelected();

                if ("None of them".equalsIgnoreCase(tag)) {
                    v.setSelected(!selected);
                    if (!selected) {
                        v.setBackgroundResource(R.drawable.bg_survey_selected);
                        deselectAllExceptNone();
                    } else {
                        v.setBackgroundResource(R.drawable.bg_input);
                    }
                } else {
                    v.setSelected(!selected);
                    if (!selected) {
                        v.setBackgroundResource(R.drawable.bg_survey_selected);
                        deselectNoneOption();
                    } else {
                        v.setBackgroundResource(R.drawable.bg_input);
                    }
                }
            });

            flexboxDietTags.addView(chip);
        }
    }

    private void deselectAllExceptNone() {
        for (int i = 0; i < flexboxDietTags.getChildCount(); i++) {
            View child = flexboxDietTags.getChildAt(i);
            if (child instanceof TextView) {
                String tag = (String) child.getTag();
                if (!"None of them".equalsIgnoreCase(tag)) {
                    child.setSelected(false);
                    child.setBackgroundResource(R.drawable.bg_input);
                }
            }
        }
    }

    private void deselectNoneOption() {
        for (int i = 0; i < flexboxDietTags.getChildCount(); i++) {
            View child = flexboxDietTags.getChildAt(i);
            if (child instanceof TextView) {
                String tag = (String) child.getTag();
                if ("None of them".equalsIgnoreCase(tag)) {
                    child.setSelected(false);
                    child.setBackgroundResource(R.drawable.bg_input);
                }
            }
        }
    }

    private void setupNext() {
        btnNext.setOnClickListener(v -> {
            btnNext.setEnabled(false);
            btnNext.setText("Saving Preferences...");

            ArrayList<String> selectedCuisines = getIntent().getStringArrayListExtra("selected_cuisines");
            ArrayList<String> selectedAllergies = getIntent().getStringArrayListExtra("selected_allergies");

            ArrayList<String> selectedDietTags = new ArrayList<>();
            for (int i = 0; i < flexboxDietTags.getChildCount(); i++) {
                View child = flexboxDietTags.getChildAt(i);
                if (child instanceof TextView && child.isSelected()) {
                    String tag = (String) child.getTag();
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
                            navigateToIngredients();
                        }

                        @Override
                        public void onFailure(retrofit2.Call<com.example.mealrecommendationapp.data.network.ApiService.ApiResponse<com.example.mealrecommendationapp.data.network.ApiService.ProfileData>> call, Throwable t) {
                            navigateToIngredients();
                        }
                    });
        });
    }

    private void navigateToIngredients() {
        Intent intent = new Intent(this, SurveyIngredientActivity.class);
        intent.putExtra("selected_time", selectedTime);
        intent.putExtra("selected_day", selectedDayIndex);
        startActivity(intent);
        finish();
    }
}