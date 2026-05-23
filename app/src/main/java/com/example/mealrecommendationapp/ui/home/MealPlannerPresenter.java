package com.example.mealrecommendationapp.ui.home;

import android.content.Context;
import com.example.mealrecommendationapp.data.MealRepository;
import com.example.mealrecommendationapp.data.RepositoryCallback;
import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.model.FoodItem;
import com.example.mealrecommendationapp.model.TimeMeal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MealPlannerPresenter {

    private final MealPlannerView view;
    private final MealRepository mealRepository;

    public MealPlannerPresenter(MealPlannerView view) {
        this.view = view;
        this.mealRepository = MealRepository.getInstance();
    }

    public String getDateOfIndex(int dayIndex) {
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_WEEK);
        int currentDayIndex = (today == Calendar.SUNDAY) ? 6 : today - 2;
        int diff = dayIndex - currentDayIndex;
        cal.add(Calendar.DAY_OF_YEAR, diff);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(cal.getTime());
    }

    private boolean isHourMatch(String scheduledAt, String time) {
        if (scheduledAt == null) return false;
        try {
            String cleaned = scheduledAt.replace("T", " ");
            if (cleaned.endsWith("Z")) {
                cleaned = cleaned.substring(0, cleaned.length() - 1);
            }
            if (cleaned.contains(".")) {
                cleaned = cleaned.split("\\.")[0];
            }
            
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            if (scheduledAt.contains("Z") || scheduledAt.contains("T")) {
                parser.setTimeZone(TimeZone.getTimeZone("UTC"));
            } else {
                parser.setTimeZone(TimeZone.getDefault());
            }
            
            Date date = parser.parse(cleaned);
            if (date == null) return false;
            
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.US);
            formatter.setTimeZone(TimeZone.getDefault());
            String localHourMin = formatter.format(date);
            
            return localHourMin.equals(time);
        } catch (Exception e) {
            return false;
        }
    }

    public void loadTimeline(Context context, int selectedDayIndex) {
        view.showLoading();
        String dateStr = getDateOfIndex(selectedDayIndex);

        mealRepository.getMeals(context, dateStr, new RepositoryCallback<List<ApiService.MealResponse>>() {
            @Override
            public void onSuccess(List<ApiService.MealResponse> mealsList) {
                view.hideLoading();
                List<TimeMeal> timeline = new ArrayList<>();
                for (int hour = 9; hour <= 21; hour++) {
                    String time = (hour < 10) ? "0" + hour + ":00" : hour + ":00";
                    TimeMeal timeMeal = new TimeMeal(selectedDayIndex, time);

                    for (ApiService.MealResponse meal : mealsList) {
                        if (isHourMatch(meal.getScheduledAt(), time)) {
                            FoodItem foodItem = new FoodItem(
                                    meal.getFoodId(),
                                    meal.getFoodName(),
                                    (int) meal.getCaloriesSnap(),
                                    (int) meal.getProteinSnap(),
                                    (int) meal.getFatSnap(),
                                    (int) meal.getCarbsSnap(),
                                    meal.getFoodImageUrl()
                            );
                            timeMeal.setFoodItem(foodItem);
                            timeMeal.setMealId(meal.getId());
                            timeMeal.setQuantityG(meal.getQuantityG());
                            break;
                        }
                    }
                    timeline.add(timeMeal);
                }
                view.onTimelineLoaded(timeline);
            }

            @Override
            public void onError(String errorMessage) {
                view.hideLoading();
                // Load fallback empty timeline
                List<TimeMeal> timeline = new ArrayList<>();
                for (int hour = 9; hour <= 21; hour++) {
                    String time = (hour < 10) ? "0" + hour + ":00" : hour + ":00";
                    timeline.add(new TimeMeal(selectedDayIndex, time));
                }
                view.onTimelineLoaded(timeline);
                view.onTimelineFailed(errorMessage);
            }
        });
    }

    public void deleteMeal(Context context, String mealId) {
        if (mealId == null) return;
        view.showLoading();
        mealRepository.deleteMeal(context, mealId, new RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                view.hideLoading();
                view.onMealDeletedSuccessfully();
            }

            @Override
            public void onError(String errorMessage) {
                view.hideLoading();
                view.onMealDeleteFailed(errorMessage);
            }
        });
    }

    public void updateMealQuantity(Context context, String mealId, int quantity) {
        if (mealId == null) return;
        view.showLoading();
        mealRepository.updateMeal(context, mealId, quantity, new RepositoryCallback<ApiService.MealResponse>() {
            @Override
            public void onSuccess(ApiService.MealResponse result) {
                view.hideLoading();
                view.onMealUpdatedSuccessfully();
            }

            @Override
            public void onError(String errorMessage) {
                view.hideLoading();
                view.onMealUpdateFailed(errorMessage);
            }
        });
    }

    public void addMeal(Context context, String foodId, String scheduledAt, int quantity) {
        view.showLoading();
        mealRepository.addMeal(context, foodId, scheduledAt, quantity, new RepositoryCallback<ApiService.MealResponse>() {
            @Override
            public void onSuccess(ApiService.MealResponse result) {
                view.hideLoading();
                view.onMealUpdatedSuccessfully();
            }

            @Override
            public void onError(String errorMessage) {
                view.hideLoading();
                view.onMealUpdateFailed(errorMessage);
            }
        });
    }
}
