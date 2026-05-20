package com.example.mealrecommendationapp.ui.survey;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.data.network.LocalCacheManager;
import com.example.mealrecommendationapp.ui.home.RecommendationActivity;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class SurveyIngredientActivity extends AppCompatActivity {

    private EditText edtCustomIngredient;
    private Button btnAddIngredient;
    private TextView txtSelectedHeader;
    private FlexboxLayout flexboxSelectedIngredients;
    private FlexboxLayout flexboxPopularIngredients;
    private Button btnGetRecommendations;

    private LocalCacheManager cacheManager;
    private ArrayList<String> selectedIngredients = new ArrayList<>();
    
    private String selectedTime;
    private int selectedDayIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_ingredient);

        selectedTime = getIntent().getStringExtra("selected_time");
        selectedDayIndex = getIntent().getIntExtra("selected_day", 0);

        cacheManager = new LocalCacheManager(this);

        initViews();
        setupListeners();
        loadPopularIngredients();
        refreshUI();
    }

    private void initViews() {
        edtCustomIngredient = findViewById(R.id.edtCustomIngredient);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        txtSelectedHeader = findViewById(R.id.txtSelectedHeader);
        flexboxSelectedIngredients = findViewById(R.id.flexboxSelectedIngredients);
        flexboxPopularIngredients = findViewById(R.id.flexboxPopularIngredients);
        btnGetRecommendations = findViewById(R.id.btnGetRecommendations);
    }

    private void setupListeners() {
        btnAddIngredient.setOnClickListener(v -> {
            String input = edtCustomIngredient.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(this, "Please enter an ingredient name", Toast.LENGTH_SHORT).show();
                return;
            }
            String formatted = capitalize(input);
            if (!selectedIngredients.contains(formatted)) {
                selectedIngredients.add(formatted);
                edtCustomIngredient.setText("");
                refreshUI();
            } else {
                Toast.makeText(this, "Ingredient already added", Toast.LENGTH_SHORT).show();
            }
        });

        btnGetRecommendations.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecommendationActivity.class);
            intent.putExtra("selected_time", selectedTime);
            intent.putExtra("selected_day", selectedDayIndex);
            intent.putStringArrayListExtra("selected_ingredients", selectedIngredients);
            startActivity(intent);
        });
    }

    private void loadPopularIngredients() {
        List<String> popular = cacheManager.getPopularIngredients();
        flexboxPopularIngredients.removeAllViews();

        for (String ingredient : popular) {
            TextView chip = new TextView(this);
            chip.setText(ingredient);
            chip.setTextSize(14);
            chip.setPadding(28, 14, 28, 14);
            chip.setBackgroundResource(R.drawable.bg_input);
            chip.setTextColor(Color.parseColor("#222222"));

            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 8, 8, 8);
            chip.setLayoutParams(params);

            chip.setOnClickListener(v -> {
                if (selectedIngredients.contains(ingredient)) {
                    removeIngredient(ingredient);
                } else {
                    selectedIngredients.add(ingredient);
                    refreshUI();
                }
            });

            flexboxPopularIngredients.addView(chip);
        }
    }

    private void removeIngredient(String ingredient) {
        selectedIngredients.remove(ingredient);
        refreshUI();
    }

    private void refreshUI() {
        // Clear and rebuild selected ingredients
        flexboxSelectedIngredients.removeAllViews();
        if (selectedIngredients.isEmpty()) {
            txtSelectedHeader.setVisibility(View.GONE);
        } else {
            txtSelectedHeader.setVisibility(View.VISIBLE);
            for (String ingredient : selectedIngredients) {
                TextView chip = new TextView(this);
                chip.setText(ingredient + "  ✕");
                chip.setTextSize(14);
                chip.setPadding(28, 14, 28, 14);
                chip.setBackgroundResource(R.drawable.bg_survey_selected);
                chip.setTextColor(Color.parseColor("#222222"));

                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                        FlexboxLayout.LayoutParams.WRAP_CONTENT,
                        FlexboxLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(8, 8, 8, 8);
                chip.setLayoutParams(params);

                chip.setOnClickListener(v -> removeIngredient(ingredient));
                flexboxSelectedIngredients.addView(chip);
            }
        }

        // Highlight/update active popular chips
        for (int i = 0; i < flexboxPopularIngredients.getChildCount(); i++) {
            View child = flexboxPopularIngredients.getChildAt(i);
            if (child instanceof TextView) {
                TextView chip = (TextView) child;
                String text = chip.getText().toString();
                if (selectedIngredients.contains(text)) {
                    chip.setSelected(true);
                    chip.setBackgroundResource(R.drawable.bg_survey_selected);
                } else {
                    chip.setSelected(false);
                    chip.setBackgroundResource(R.drawable.bg_input);
                }
            }
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
