package com.example.mealrecommendationapp.ui.survey;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;

import java.util.Calendar;

public class SurveyBirthActivity
        extends AppCompatActivity {

    private NumberPicker day;
    private NumberPicker month;
    private NumberPicker year;

    private Button btnNext;

    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_survey_birth
        );

        gender =
                getIntent().getStringExtra(
                        "gender"
                );

        initViews();

        setupPickers();

        setupNext();
    }

    private void initViews() {

        day =
                findViewById(R.id.pickerDay);

        month =
                findViewById(R.id.pickerMonth);

        year =
                findViewById(R.id.pickerYear);

        btnNext =
                findViewById(R.id.btnNext);
    }

    private void setupPickers() {

        day.setMinValue(1);
        day.setMaxValue(31);

        month.setMinValue(1);
        month.setMaxValue(12);

        year.setMinValue(1950);
        year.setMaxValue(2025);
    }

    private void setupNext() {

        btnNext.setOnClickListener(v -> {

            int birthYear =
                    year.getValue();
            int birthMonth =
                    month.getValue();
            int birthDay =
                    day.getValue();

            int currentYear =
                    Calendar.getInstance()
                            .get(Calendar.YEAR);

            int age =
                    currentYear - birthYear;

            String dateOfBirth = String.format(java.util.Locale.US, "%04d-%02d-%02d", birthYear, birthMonth, birthDay);

            Intent intent =
                    new Intent(
                            SurveyBirthActivity.this,
                            SurveyHeightWeightActivity.class
                    );

            intent.putExtra(
                    "gender",
                    gender
            );

            intent.putExtra(
                    "age",
                    age
            );

            intent.putExtra(
                    "dateOfBirth",
                    dateOfBirth
            );

            startActivity(intent);

            finish();
        });
    }
}