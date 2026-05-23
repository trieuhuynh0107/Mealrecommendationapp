package com.example.mealrecommendationapp.ui.auth;

import com.example.mealrecommendationapp.data.network.ApiService;

public interface LoginView {
    void showLoading();
    void hideLoading();
    void onLoginSuccess(ApiService.AuthData authData);
    void onLoginError(String message);
    void onProfileLoaded(ApiService.ProfileData profile);
    void onProfileLoadFailed();
}
