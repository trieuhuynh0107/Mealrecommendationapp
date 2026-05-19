package com.example.mealrecommendationapp.ui.survey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;

public class SurveyGenderActivity
        extends AppCompatActivity {

    private View btnMale;
    private View btnFemale;

    private Button btnNext;

    private String selectedGender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_survey_gender
        );

        initViews();

        setupClicks();
    }

    private void initViews() {

        btnMale =
                findViewById(R.id.btnMale);

        btnFemale =
                findViewById(R.id.btnFemale);

        btnNext =
                findViewById(R.id.btnNext);
    }

    private void setupClicks() {

        btnMale.setOnClickListener(v -> {

            selectedGender = "Male";

            highlight(btnMale);
        });

        btnFemale.setOnClickListener(v -> {

            selectedGender = "Female";

            highlight(btnFemale);
        });

        btnNext.setOnClickListener(v -> {

            if (!selectedGender.isEmpty()) {

                Intent intent =
                        new Intent(
                                this,
                                SurveyBirthActivity.class
                        );

                intent.putExtra(
                        "gender",
                        selectedGender
                );

                startActivity(intent);
            }
        });
    }

    private void highlight(View selected) {

        btnMale.setBackgroundResource(
                R.drawable.bg_input
        );

        btnFemale.setBackgroundResource(
                R.drawable.bg_input
        );

        selected.setBackgroundResource(
                R.drawable.bg_button_yellow
        );
    }
}