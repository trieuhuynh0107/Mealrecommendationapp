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

public class SurveyCuisineActivity extends AppCompatActivity {

    private Button btnNext;
    private FlexboxLayout flexboxCuisines;
    private LocalCacheManager cacheManager;

    private String selectedTime;
    private int selectedDayIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_cuisine);

        selectedTime = getIntent().getStringExtra("selected_time");
        selectedDayIndex = getIntent().getIntExtra("selected_day", 0);

        cacheManager = new LocalCacheManager(this);
        initViews();
        setupNext();
    }

    private void initViews() {
        btnNext = findViewById(R.id.btnNext);
        flexboxCuisines = findViewById(R.id.flexboxCuisines);

        List<String> cuisines = cacheManager.getCuisines();
        renderCuisineChips(cuisines);
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

    private void renderCuisineChips(List<String> cuisines) {
        flexboxCuisines.removeAllViews();
        for (String cuisine : cuisines) {
            TextView chip = new TextView(this);
            chip.setText(formatForDisplay(cuisine));
            chip.setTag(cuisine); // Save raw tag in tag property!
            chip.setTextSize(16);
            chip.setPadding(36, 22, 36, 22);
            chip.setBackgroundResource(R.drawable.bg_input);
            chip.setTextColor(android.graphics.Color.parseColor("#222222"));

            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 10, 10, 10);
            chip.setLayoutParams(params);

            chip.setOnClickListener(v -> {
                boolean selected = v.isSelected();
                v.setSelected(!selected);
                if (!selected) {
                    v.setBackgroundResource(R.drawable.bg_survey_selected);
                } else {
                    v.setBackgroundResource(R.drawable.bg_input);
                }
            });

            flexboxCuisines.addView(chip);
        }
    }

    private void setupNext() {
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, SurveyAllergyActivity.class);
            intent.putExtra("selected_time", selectedTime);
            intent.putExtra("selected_day", selectedDayIndex);

            ArrayList<String> selectedCuisines = new ArrayList<>();
            for (int i = 0; i < flexboxCuisines.getChildCount(); i++) {
                View child = flexboxCuisines.getChildAt(i);
                if (child instanceof TextView && child.isSelected()) {
                    selectedCuisines.add((String) child.getTag()); // Retrieve raw tag
                }
            }
            intent.putStringArrayListExtra("selected_cuisines", selectedCuisines);

            startActivity(intent);
        });
    }
}