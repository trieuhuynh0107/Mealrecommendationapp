package com.example.mealrecommendationapp.ui.home;

import com.example.mealrecommendationapp.model.TimeMeal;
import java.util.List;

public interface MealPlannerView {
    void showLoading();
    void hideLoading();
    void onTimelineLoaded(List<TimeMeal> timeline);
    void onTimelineFailed(String message);
    void onMealDeletedSuccessfully();
    void onMealDeleteFailed(String message);
    void onMealUpdatedSuccessfully();
    void onMealUpdateFailed(String message);
}
