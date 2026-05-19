package com.example.mealrecommendationapp.data;

import com.example.mealrecommendationapp.model.CaloriesSummary;
import com.example.mealrecommendationapp.model.FoodItem;
import com.example.mealrecommendationapp.model.TimeMeal;

import java.util.ArrayList;
import java.util.List;

public class FakeData {

    // =========================
    // CALORIES SUMMARY
    // =========================

    public static CaloriesSummary getSummary() {

        return new CaloriesSummary(

                1721,
                2213,

                78,
                90,

                45,
                70,

                95,
                110
        );
    }

    // =========================
    // SELECTED FOODS
    // =========================

    private static List<FoodItem> selectedFoods =
            new ArrayList<>();

    public static List<FoodItem> getFoods() {

        return selectedFoods;
    }

    public static void addFood(FoodItem foodItem) {

        selectedFoods.add(foodItem);
    }

    // =========================
    // RECOMMENDED FOODS
    // =========================

    public static List<FoodItem> getRecommendedFoods() {

        List<FoodItem> list = new ArrayList<>();

        list.add(new FoodItem(
                "Salad with eggs",
                294,
                12,
                22,
                42
        ));

        list.add(new FoodItem(
                "Avocado Dish",
                320,
                18,
                15,
                30
        ));

        list.add(new FoodItem(
                "Pancakes",
                450,
                10,
                50,
                60
        ));

        list.add(new FoodItem(
                "Chicken Bowl",
                390,
                30,
                12,
                28
        ));

        list.add(new FoodItem(
                "Beef Steak",
                520,
                42,
                25,
                10
        ));

        return list;
    }

    // =========================
    // SAVED MEALS
    // =========================

    private static List<TimeMeal> savedMeals =
            new ArrayList<>();

    public static List<TimeMeal> getSavedMeals() {

        return savedMeals;
    }

    // =========================
    // FIND MEAL
    // =========================

    public static TimeMeal findMeal(
            int dayIndex,
            String time
    ) {

        for (TimeMeal item : savedMeals) {

            if (
                    item.getDayIndex() == dayIndex
                            &&
                            item.getTime().equals(time)
            ) {

                return item;
            }
        }

        return null;
    }

    // =========================
    // ADD FOOD TO TIME
    // =========================

    public static void addFoodToTime(
            int dayIndex,
            String time,
            FoodItem foodItem
    ) {

        TimeMeal existingMeal =
                findMeal(dayIndex, time);

        if (existingMeal != null) {

            existingMeal.setFoodItem(foodItem);

        } else {

            TimeMeal newMeal =
                    new TimeMeal(
                            dayIndex,
                            time
                    );

            newMeal.setFoodItem(foodItem);

            savedMeals.add(newMeal);
        }

        addFood(foodItem);
    }
}