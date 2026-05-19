package com.example.mealrecommendationapp.model;

public class FoodItem {

    private String name;
    private int calories;

    private int protein;
    private int fats;
    private int carbs;

    public FoodItem(
            String name,
            int calories,
            int protein,
            int fats,
            int carbs
    ) {

        this.name = name;
        this.calories = calories;

        this.protein = protein;
        this.fats = fats;
        this.carbs = carbs;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public int getProtein() {
        return protein;
    }

    public int getFats() {
        return fats;
    }

    public int getCarbs() {
        return carbs;
    }
}