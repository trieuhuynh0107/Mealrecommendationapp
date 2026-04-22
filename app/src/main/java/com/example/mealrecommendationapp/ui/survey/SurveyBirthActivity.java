package com.example.mealrecommendationapp.ui.survey;

import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;

public class SurveyBirthActivity extends AppCompatActivity {

    NumberPicker day, month, year;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_birth);

        day = findViewById(R.id.pickerDay);
        month = findViewById(R.id.pickerMonth);
        year = findViewById(R.id.pickerYear);
        btnNext = findViewById(R.id.btnNext);

        // Day
        day.setMinValue(1);
        day.setMaxValue(31);

        // Month
        month.setMinValue(1);
        month.setMaxValue(12);

        // Year
        year.setMinValue(1950);
        year.setMaxValue(2025);

        btnNext.setOnClickListener(v -> {
            int d = day.getValue();
            int m = month.getValue();
            int y = year.getValue();

        });
    }
}