package com.example.mealrecommendationapp.ui.survey;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;

import java.util.ArrayList;
import java.util.List;

public class SurveyCuisineActivity
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
                R.layout.activity_survey_cuisine
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

        options.add(
                findViewById(R.id.optionVietnamese)
        );

        options.add(
                findViewById(R.id.optionAmerican)
        );

        options.add(
                findViewById(R.id.optionItalian)
        );

        options.add(
                findViewById(R.id.optionThai)
        );

        options.add(
                findViewById(R.id.optionKorean)
        );

        options.add(
                findViewById(R.id.optionChinese)
        );
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
                            SurveyAllergyActivity.class
                    );

            intent.putExtra(
                    "selected_time",
                    selectedTime
            );

            intent.putExtra(
                    "selected_day",
                    selectedDayIndex
            );

            ArrayList<String> selectedCuisines = new ArrayList<>();
            for (TextView option : options) {
                if (option.isSelected()) {
                    selectedCuisines.add(option.getText().toString());
                }
            }
            intent.putStringArrayListExtra("selected_cuisines", selectedCuisines);

            startActivity(intent);
        });
    }
}