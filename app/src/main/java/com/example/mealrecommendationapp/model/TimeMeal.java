package com.example.mealrecommendationapp.model;

public class TimeMeal {

    private int dayIndex;

    private String time;

    private FoodItem foodItem;

    public TimeMeal(
            int dayIndex,
            String time
    ) {

        this.dayIndex = dayIndex;

        this.time = time;
    }

    public int getDayIndex() {

        return dayIndex;
    }

    public String getTime() {

        return time;
    }

    public FoodItem getFoodItem() {

        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {

        this.foodItem = foodItem;
    }
}