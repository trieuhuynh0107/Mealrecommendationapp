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

public class SurveyAllergyActivity extends AppCompatActivity {

    private Button btnNext;
    private FlexboxLayout flexboxAllergies;
    private LocalCacheManager cacheManager;

    private String selectedTime;
    private int selectedDayIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_allergy);

        selectedTime = getIntent().getStringExtra("selected_time");
        selectedDayIndex = getIntent().getIntExtra("selected_day", 0);

        cacheManager = new LocalCacheManager(this);
        initViews();
        setupNext();
    }

    private void initViews() {
        btnNext = findViewById(R.id.btnNext);
        flexboxAllergies = findViewById(R.id.flexboxAllergies);

        List<String> allergies = new ArrayList<>(cacheManager.getAllergies());
        allergies.add("None of them"); // Add fallback dynamically
        renderAllergyChips(allergies);
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

    private void renderAllergyChips(List<String> allergies) {
        flexboxAllergies.removeAllViews();
        for (String allergy : allergies) {
            TextView chip = new TextView(this);
            chip.setText(formatForDisplay(allergy));
            chip.setTag(allergy); // Save raw tag in tag property!
            chip.setTextSize(15);
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
                
                if ("None of them".equalsIgnoreCase(allergy)) {
                    // Select "None of them" and deselect all others
                    v.setSelected(!selected);
                    if (!selected) {
                        v.setBackgroundResource(R.drawable.bg_survey_selected);
                        deselectAllExceptNone();
                    } else {
                        v.setBackgroundResource(R.drawable.bg_input);
                    }
                } else {
                    // Select this option and deselect "None of them"
                    v.setSelected(!selected);
                    if (!selected) {
                        v.setBackgroundResource(R.drawable.bg_survey_selected);
                        deselectNoneOption();
                    } else {
                        v.setBackgroundResource(R.drawable.bg_input);
                    }
                }
            });

            flexboxAllergies.addView(chip);
        }
    }

    private void deselectAllExceptNone() {
        for (int i = 0; i < flexboxAllergies.getChildCount(); i++) {
            View child = flexboxAllergies.getChildAt(i);
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
        for (int i = 0; i < flexboxAllergies.getChildCount(); i++) {
            View child = flexboxAllergies.getChildAt(i);
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
            Intent intent = new Intent(this, SurveyHealthActivity.class);
            intent.putExtra("selected_time", selectedTime);
            intent.putExtra("selected_day", selectedDayIndex);

            ArrayList<String> selectedCuisines = getIntent().getStringArrayListExtra("selected_cuisines");
            intent.putStringArrayListExtra("selected_cuisines", selectedCuisines);

            ArrayList<String> selectedAllergies = new ArrayList<>();
            for (int i = 0; i < flexboxAllergies.getChildCount(); i++) {
                View child = flexboxAllergies.getChildAt(i);
                if (child instanceof TextView && child.isSelected()) {
                    String allergy = (String) child.getTag();
                    if (!"None of them".equalsIgnoreCase(allergy)) {
                        selectedAllergies.add(allergy);
                    }
                }
            }
            intent.putStringArrayListExtra("selected_allergies", selectedAllergies);

            startActivity(intent);
        });
    }
}