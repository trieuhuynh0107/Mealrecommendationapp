package com.example.mealrecommendationapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.mealrecommendationapp.adapter.FoodAdapter;
import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.data.network.SharedPreferencesHelper;
import com.example.mealrecommendationapp.databinding.ActivityHomeBinding;
import com.example.mealrecommendationapp.model.FoodItem;
import com.example.mealrecommendationapp.ui.survey.SurveyGenderActivity;
import com.example.mealrecommendationapp.ui.welcome.WelcomeActivity;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private ActivityHomeBinding binding;
    private HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new HomePresenter(this);

        setupClicks();
    }

    private void setupClicks() {
        binding.btnCalendar.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MealPlannerActivity.class);
            startActivity(intent);
        });

        binding.btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadDailyData(this);
    }

    @Override
    public void showLoading() {
        // Can optionally show a progress dialog or custom spinner
    }

    @Override
    public void hideLoading() {
        // Hide spinner if any
    }

    @Override
    public void onSummaryLoaded(ApiService.SummaryData summary) {
        ApiService.NutritionValues actual = summary.getActual();
        ApiService.NutritionValues target = summary.getTarget();
        ApiService.NutritionValues percentage = summary.getPercentage();

        binding.caloriesArc.setProgress((int) percentage.getCalories());

        binding.txtCalories.setText((int) actual.getCalories() + " Kcal");
        binding.txtGoal.setText("of " + (int) target.getCalories() + " kcal");

        binding.txtProtein.setText((int) actual.getProtein() + "/" + (int) target.getProtein() + "g");
        binding.proteinProgress.setProgress((int) percentage.getProtein());

        binding.txtFats.setText((int) actual.getFat() + "/" + (int) target.getFat() + "g");
        binding.fatsProgress.setProgress((int) percentage.getFat());

        binding.txtCarbs.setText((int) actual.getCarbs() + "/" + (int) target.getCarbs() + "g");
        binding.carbsProgress.setProgress((int) percentage.getCarbs());
    }

    @Override
    public void onSummaryFailed(String errorMessage) {
        Toast.makeText(this, "Không thể tải báo cáo: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMealsLoaded(List<FoodItem> meals) {
        binding.recyclerFood.setLayoutManager(new LinearLayoutManager(this));
        FoodAdapter adapter = new FoodAdapter(meals, foodItem -> {
            Intent intent = new Intent(HomeActivity.this, FoodDetailActivity.class);
            intent.putExtra("food_id", foodItem.getId());
            intent.putExtra("food_item", foodItem);
            startActivity(intent);
        });
        binding.recyclerFood.setAdapter(adapter);
    }


    @Override
    public void onMealsFailed(String errorMessage) {
        // Fail silently or notify
    }

    @Override
    public void navigateToOnboarding() {
        Toast.makeText(this, "Vui lòng hoàn thành Onboarding trước", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomeActivity.this, SurveyGenderActivity.class));
        finish();
    }

    @Override
    public void navigateToWelcome() {
        SharedPreferencesHelper.clear(this);
        startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
        finish();
    }
}