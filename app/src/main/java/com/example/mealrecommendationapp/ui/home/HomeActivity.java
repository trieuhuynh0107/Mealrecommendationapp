package com.example.mealrecommendationapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.adapter.FoodAdapter;
import com.example.mealrecommendationapp.data.network.ApiClient;
import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.data.network.SharedPreferencesHelper;
import com.example.mealrecommendationapp.model.FoodItem;
import com.example.mealrecommendationapp.ui.custom.CaloriesArcView;
import com.example.mealrecommendationapp.ui.survey.SurveyGenderActivity;
import com.example.mealrecommendationapp.ui.welcome.WelcomeActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private void fetchDailyData() {
        String todayStr = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());

        // 1. Fetch Daily Summary
        ApiClient.getService(this).getDailySummary(todayStr)
                .enqueue(new Callback<ApiService.ApiResponse<ApiService.SummaryData>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<ApiService.SummaryData>> call,
                                           Response<ApiService.ApiResponse<ApiService.SummaryData>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            updateSummaryUi(response.body().getData());
                        } else {
                            if (response.code() == 400 || (response.body() != null && response.body().getError() != null && 
                                    "ONBOARDING_REQUIRED".equals(response.body().getError().getCode()))) {
                                Toast.makeText(HomeActivity.this, "Vui lòng hoàn thành Onboarding trước", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HomeActivity.this, SurveyGenderActivity.class));
                                finish();
                            } else if (response.code() == 401) {
                                SharedPreferencesHelper.clear(HomeActivity.this);
                                startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<ApiService.SummaryData>> call, Throwable t) {
                        Toast.makeText(HomeActivity.this, "Không thể tải báo cáo: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // 2. Fetch Daily Meals
        ApiClient.getService(this).getMeals(todayStr)
                .enqueue(new Callback<ApiService.ApiResponse<List<ApiService.MealResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<List<ApiService.MealResponse>>> call,
                                           Response<ApiService.ApiResponse<List<ApiService.MealResponse>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            List<ApiService.MealResponse> meals = response.body().getData();
                            List<FoodItem> foodList = new ArrayList<>();
                            for (ApiService.MealResponse meal : meals) {
                                FoodItem item = new FoodItem(
                                        meal.getId(),
                                        meal.getFoodName(),
                                        (int) meal.getCaloriesSnap(),
                                        (int) meal.getProteinSnap(),
                                        (int) meal.getFatSnap(),
                                        (int) meal.getCarbsSnap(),
                                        meal.getFoodImageUrl()
                                );
                                foodList.add(item);
                            }
                            updateRecycler(foodList);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<List<ApiService.MealResponse>>> call, Throwable t) {
                        // Fail silently or log
                    }
                });
    }

    private void updateSummaryUi(ApiService.SummaryData summary) {
        ApiService.NutritionValues actual = summary.getActual();
        ApiService.NutritionValues target = summary.getTarget();
        ApiService.NutritionValues percentage = summary.getPercentage();

        caloriesArc.setProgress((int) percentage.getCalories());

        txtCalories.setText((int) actual.getCalories() + " Kcal");
        txtGoal.setText("of " + (int) target.getCalories() + " kcal");

        txtProtein.setText((int) actual.getProtein() + "/" + (int) target.getProtein() + "g");
        proteinProgress.setProgress((int) percentage.getProtein());

        txtFats.setText((int) actual.getFat() + "/" + (int) target.getFat() + "g");
        fatsProgress.setProgress((int) percentage.getFat());

        txtCarbs.setText((int) actual.getCarbs() + "/" + (int) target.getCarbs() + "g");
        carbsProgress.setProgress((int) percentage.getCarbs());
    }

    private void updateRecycler(List<FoodItem> foodList) {
        recyclerFood.setLayoutManager(new LinearLayoutManager(this));
        FoodAdapter adapter = new FoodAdapter(foodList, foodItem -> {
            // Food click actions if any
        });
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
        fetchDailyData();
    }
}