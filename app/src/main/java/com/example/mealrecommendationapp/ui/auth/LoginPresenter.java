package com.example.mealrecommendationapp.ui.auth;

import android.content.Context;
import com.example.mealrecommendationapp.data.AuthRepository;
import com.example.mealrecommendationapp.data.RepositoryCallback;
import com.example.mealrecommendationapp.data.network.ApiService;

public class LoginPresenter {

    private final LoginView view;
    private final AuthRepository authRepository;

    public LoginPresenter(LoginView view) {
        this.view = view;
        this.authRepository = AuthRepository.getInstance();
    }

    public void login(Context context, String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            view.onLoginError("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        view.showLoading();
        authRepository.login(context, email, password, new RepositoryCallback<ApiService.AuthData>() {
            @Override
            public void onSuccess(ApiService.AuthData result) {
                view.onLoginSuccess(result);
            }

            @Override
            public void onError(String errorMessage) {
                view.hideLoading();
                view.onLoginError(errorMessage);
            }
        });
    }

    public void fetchProfile(Context context) {
        authRepository.getProfile(context, new RepositoryCallback<ApiService.ProfileData>() {
            @Override
            public void onSuccess(ApiService.ProfileData result) {
                view.onProfileLoaded(result);
            }

            @Override
            public void onError(String errorMessage) {
                view.onProfileLoadFailed();
            }
        });
    }
}
