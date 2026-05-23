package com.example.mealrecommendationapp.ui.home;

import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.model.FoodItem;
import java.util.List;

public interface HomeView {
    void showLoading();
    void hideLoading();
    void onSummaryLoaded(ApiService.SummaryData summary);
    void onSummaryFailed(String errorMessage);
    void onMealsLoaded(List<FoodItem> meals);
    void onMealsFailed(String errorMessage);
    void navigateToOnboarding();
    void navigateToWelcome();
}
