package com.example.mealrecommendationapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.adapter.FoodAdapter;
import com.example.mealrecommendationapp.data.FakeData;
import com.example.mealrecommendationapp.model.CaloriesSummary;
import com.example.mealrecommendationapp.ui.custom.CaloriesArcView;
import com.example.mealrecommendationapp.ui.home.ProfileActivity;

public class HomeActivity extends AppCompatActivity {

    private CaloriesArcView caloriesArc;

    private TextView txtCalories;
    private TextView txtGoal;

    private TextView txtProtein;
    private TextView txtFats;
    private TextView txtCarbs;

    private TextView btnCalendar;
    private TextView btnMenu;

    private ProgressBar proteinProgress;
    private ProgressBar fatsProgress;
    private ProgressBar carbsProgress;

    private RecyclerView recyclerFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        initViews();

        setupSummary();

        setupRecycler();

        setupClicks();
    }

    private void initViews() {

        caloriesArc =
                findViewById(R.id.caloriesArc);

        txtCalories =
                findViewById(R.id.txtCalories);

        txtGoal =
                findViewById(R.id.txtGoal);

        txtProtein =
                findViewById(R.id.txtProtein);

        txtFats =
                findViewById(R.id.txtFats);

        txtCarbs =
                findViewById(R.id.txtCarbs);

        proteinProgress =
                findViewById(R.id.proteinProgress);

        fatsProgress =
                findViewById(R.id.fatsProgress);

        carbsProgress =
                findViewById(R.id.carbsProgress);

        recyclerFood =
                findViewById(R.id.recyclerFood);

        btnCalendar =
                findViewById(R.id.btnCalendar);

        btnMenu =
                findViewById(R.id.btnMenu);
    }

    private void setupSummary() {

        CaloriesSummary summary =
                FakeData.getSummary();

        int percent =
                (summary.getCurrentCalories() * 100)
                        / summary.getGoalCalories();

        caloriesArc.setProgress(percent);

        txtCalories.setText(
                summary.getCurrentCalories()
                        + " Kcal"
        );

        txtGoal.setText(
                "of "
                        + summary.getGoalCalories()
                        + " kcal"
        );

        txtProtein.setText(
                summary.getProteinCurrent()
                        + "/"
                        + summary.getProteinGoal()
                        + "g"
        );

        proteinProgress.setProgress(
                (summary.getProteinCurrent() * 100)
                        / summary.getProteinGoal()
        );

        txtFats.setText(
                summary.getFatsCurrent()
                        + "/"
                        + summary.getFatsGoal()
                        + "g"
        );

        fatsProgress.setProgress(
                (summary.getFatsCurrent() * 100)
                        / summary.getFatsGoal()
        );

        txtCarbs.setText(
                summary.getCarbsCurrent()
                        + "/"
                        + summary.getCarbsGoal()
                        + "g"
        );

        carbsProgress.setProgress(
                (summary.getCarbsCurrent() * 100)
                        / summary.getCarbsGoal()
        );
    }

    private void setupRecycler() {

        recyclerFood.setLayoutManager(
                new LinearLayoutManager(this)
        );

        FoodAdapter adapter =
                new FoodAdapter(

                        FakeData.getFoods(),

                        foodItem -> {

                        }
                );

        recyclerFood.setAdapter(adapter);
    }

    private void setupClicks() {

        btnCalendar.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            MealPlannerActivity.class
                    );

            startActivity(intent);
        });

        btnMenu.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            ProfileActivity.class
                    );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupRecycler();
    }
}