package com.example.mealrecommendationapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.ui.survey.SurveyGenderActivity;

public class RegisterActivity extends AppCompatActivity {

    private TextView txtSignIn;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtSignIn = findViewById(R.id.txtSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        txtSignIn.setOnClickListener(v -> finish());

        btnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SurveyGenderActivity.class));
            finish();
        });
    }
}