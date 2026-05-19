package com.example.mealrecommendationapp.ui.survey;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.data.FakeUser;
import com.example.mealrecommendationapp.ui.custom.RulerView;
import com.example.mealrecommendationapp.ui.home.HomeActivity;

public class SurveyHeightWeightActivity
        extends AppCompatActivity {

    private RulerView rulerHeight;
    private RulerView rulerWeight;

    private TextView txtHeight;
    private TextView txtWeight;

    private Button btnNext;

    private String gender;

    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_survey_height_weight
        );

        gender =
                getIntent().getStringExtra(
                        "gender"
                );

        age =
                getIntent().getIntExtra(
                        "age",
                        20
                );

        initViews();

        setupRulers();

        setupNext();
    }

    private void initViews() {

        rulerHeight =
                findViewById(R.id.rulerHeight);

        rulerWeight =
                findViewById(R.id.rulerWeight);

        txtHeight =
                findViewById(R.id.txtHeight);

        txtWeight =
                findViewById(R.id.txtWeight);

        btnNext =
                findViewById(R.id.btnNext);

        if (
                rulerHeight == null
                        ||
                        rulerWeight == null
                        ||
                        txtHeight == null
                        ||
                        txtWeight == null
        ) {

            Toast.makeText(
                    this,
                    "Layout binding error!",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void setupRulers() {

        txtHeight.setText(
                getString(
                        R.string.height_format,
                        170
                )
        );

        txtWeight.setText(
                getString(
                        R.string.weight_format,
                        60
                )
        );

        rulerHeight.setRange(
                120,
                220,
                170
        );

        rulerHeight.setOnValueChangeListener(value ->

                txtHeight.setText(
                        getString(
                                R.string.height_format,
                                value
                        )
                )
        );

        rulerWeight.setRange(
                30,
                150,
                60
        );

        rulerWeight.setOnValueChangeListener(value ->

                txtWeight.setText(
                        getString(
                                R.string.weight_format,
                                value
                        )
                )
        );
    }

    private void setupNext() {

        btnNext.setOnClickListener(v -> {

            int height =
                    rulerHeight.getCurrentValue();

            int weight =
                    rulerWeight.getCurrentValue();

            FakeUser.updateUser(

                    "User",

                    gender,

                    age,

                    weight,

                    height
            );

            Intent intent =
                    new Intent(
                            this,
                            HomeActivity.class
                    );

            startActivity(intent);

            finish();
        });
    }
}