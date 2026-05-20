package com.example.mealrecommendationapp.ui.survey;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;

import java.util.ArrayList;
import java.util.List;

public class SurveyAllergyActivity
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
                R.layout.activity_survey_allergy
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

        options.add(findViewById(R.id.optionPeanut));
        options.add(findViewById(R.id.optionMilk));
        options.add(findViewById(R.id.optionEgg));
        options.add(findViewById(R.id.optionSoy));
        options.add(findViewById(R.id.optionWheat));
        options.add(findViewById(R.id.optionSesame));
        options.add(findViewById(R.id.optionFish));
        options.add(findViewById(R.id.optionShrimp));
        options.add(findViewById(R.id.optionCrab));
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

            Intent intent =
                    new Intent(
                            this,
                            SurveyHealthActivity.class
                    );

            intent.putExtra(
                    "selected_time",
                    selectedTime
            );

            intent.putExtra(
                    "selected_day",
                    selectedDayIndex
            );

            ArrayList<String> selectedCuisines = getIntent().getStringArrayListExtra("selected_cuisines");
            intent.putStringArrayListExtra("selected_cuisines", selectedCuisines);

            ArrayList<String> selectedAllergies = new ArrayList<>();
            for (TextView option : options) {
                if (option.isSelected()) {
                    String allergy = option.getText().toString();
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