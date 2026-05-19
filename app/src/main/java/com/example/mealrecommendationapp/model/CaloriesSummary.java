package com.example.mealrecommendationapp.model;

public class CaloriesSummary {

    private int currentCalories;
    private int goalCalories;

    private int proteinCurrent;
    private int proteinGoal;

    private int fatsCurrent;
    private int fatsGoal;

    private int carbsCurrent;
    private int carbsGoal;

    public CaloriesSummary(
            int currentCalories,
            int goalCalories,

            int proteinCurrent,
            int proteinGoal,

            int fatsCurrent,
            int fatsGoal,

            int carbsCurrent,
            int carbsGoal
    ) {

        this.currentCalories = currentCalories;
        this.goalCalories = goalCalories;

        this.proteinCurrent = proteinCurrent;
        this.proteinGoal = proteinGoal;

        this.fatsCurrent = fatsCurrent;
        this.fatsGoal = fatsGoal;

        this.carbsCurrent = carbsCurrent;
        this.carbsGoal = carbsGoal;
    }

    public int getCurrentCalories() {
        return currentCalories;
    }

    public int getGoalCalories() {
        return goalCalories;
    }

    public int getProteinCurrent() {
        return proteinCurrent;
    }

    public int getProteinGoal() {
        return proteinGoal;
    }

    public int getFatsCurrent() {
        return fatsCurrent;
    }

    public int getFatsGoal() {
        return fatsGoal;
    }

    public int getCarbsCurrent() {
        return carbsCurrent;
    }

    public int getCarbsGoal() {
        return carbsGoal;
    }
}