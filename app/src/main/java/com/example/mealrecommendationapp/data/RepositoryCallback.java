package com.example.mealrecommendationapp.data;

public interface RepositoryCallback<T> {
    void onSuccess(T result);
    void onError(String errorMessage);
}
