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

            Intent intent =
                    new Intent(
                            this,
                            RecommendationActivity.class
                    );

            intent.putExtra(
                    "selected_time",
                    selectedTime
            );

            intent.putExtra(
                    "selected_day",
                    selectedDayIndex
            );

            startActivity(intent);
        });
    }
}