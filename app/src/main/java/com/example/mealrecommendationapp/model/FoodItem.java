package com.example.mealrecommendationapp.model;

import com.google.gson.annotations.SerializedName;

public class FoodItem {

    private String id;
    private String name;
    private double calories;
    private double protein;
    
    @SerializedName("fat")
    private double fats;
    private double carbs;

    @SerializedName("image_url")
    private String imageUrl;

    public FoodItem(
            String name,
            double calories,
            double protein,
            double fats,
            double carbs
    ) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fats = fats;
        this.carbs = carbs;
    }

    public FoodItem(
            String id,
            String name,
            double calories,
            double protein,
            double fats,
            double carbs,
            String imageUrl
    ) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fats = fats;
        this.carbs = carbs;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getFats() {
        return fats;
    }

    public double getCarbs() {
        return carbs;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}