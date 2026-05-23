package com.example.mealrecommendationapp.ui.home;

import android.content.Context;
import com.example.mealrecommendationapp.data.MealRepository;
import com.example.mealrecommendationapp.data.RepositoryCallback;
import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.model.FoodItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomePresenter {

    private final HomeView view;
    private final MealRepository mealRepository;

    public HomePresenter(HomeView view) {
        this.view = view;
        this.mealRepository = MealRepository.getInstance();
    }

    public void loadDailyData(Context context) {
        String todayStr = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        
        view.showLoading();

        // 1. Fetch Daily Summary
        mealRepository.getDailySummary(context, todayStr, new RepositoryCallback<ApiService.SummaryData>() {
            @Override
            public void onSuccess(ApiService.SummaryData result) {
                view.onSummaryLoaded(result);
            }

            @Override
            public void onError(String errorMessage) {
                if ("ONBOARDING_REQUIRED".equals(errorMessage)) {
                    view.navigateToOnboarding();
                } else if ("UNAUTHORIZED".equals(errorMessage)) {
                    view.navigateToWelcome();
                } else {
                    view.onSummaryFailed(errorMessage);
                }
            }
        });

        // 2. Fetch Daily Meals
        mealRepository.getMeals(context, todayStr, new RepositoryCallback<List<ApiService.MealResponse>>() {
            @Override
            public void onSuccess(List<ApiService.MealResponse> meals) {
                view.hideLoading();
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
                view.onMealsLoaded(foodList);
            }

            @Override
            public void onError(String errorMessage) {
                view.hideLoading();
                view.onMealsFailed(errorMessage);
            }
        });
    }
}
