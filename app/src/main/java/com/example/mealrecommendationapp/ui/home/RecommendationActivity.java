package com.example.mealrecommendationapp.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.adapter.FoodAdapter;
import com.example.mealrecommendationapp.data.FakeData;

public class RecommendationActivity
        extends AppCompatActivity {

    private RecyclerView recyclerRecommended;

    private String selectedTime;

    private int selectedDayIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recommendation
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

        setupRecycler();
    }

    private void initViews() {

        recyclerRecommended =
                findViewById(
                        R.id.recyclerRecommended
                );
    }

    private void setupRecycler() {

        recyclerRecommended.setLayoutManager(
                new LinearLayoutManager(this)
        );

        FoodAdapter adapter =
                new FoodAdapter(

                        FakeData.getRecommendedFoods(),

                        foodItem -> {

                            FakeData.addFoodToTime(
                                    selectedDayIndex,
                                    selectedTime,
                                    foodItem
                            );

                            Intent intent =
                                    new Intent(
                                            RecommendationActivity.this,
                                            MealPlannerActivity.class
                                    );

                            intent.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            );

                            startActivity(intent);

                            finish();
                        }
                );

        recyclerRecommended.setAdapter(adapter);
    }
}