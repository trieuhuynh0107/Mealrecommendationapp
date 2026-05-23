package com.example.mealrecommendationapp.data;

import android.content.Context;
import com.example.mealrecommendationapp.data.network.ApiClient;
import com.example.mealrecommendationapp.data.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private static AuthRepository instance;

    private AuthRepository() {}

    public static synchronized AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    public void login(Context context, String email, String password, RepositoryCallback<ApiService.AuthData> callback) {
        ApiClient.getService(context).login(new ApiService.LoginRequest(email, password))
                .enqueue(new Callback<ApiService.ApiResponse<ApiService.AuthData>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<ApiService.AuthData>> call,
                                           Response<ApiService.ApiResponse<ApiService.AuthData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiService.ApiResponse<ApiService.AuthData> body = response.body();
                            if (body.isSuccess()) {
                                callback.onSuccess(body.getData());
                            } else {
                                String errorMsg = "Sai email hoặc mật khẩu";
                                if (body.getError() != null && body.getError().getMessage() != null) {
                                    errorMsg = body.getError().getMessage();
                                }
                                callback.onError(errorMsg);
                            }
                        } else {
                            callback.onError("Đăng nhập thất bại. Mã lỗi: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<ApiService.AuthData>> call, Throwable t) {
                        callback.onError("Không thể kết nối đến server: " + t.getMessage());
                    }
                });
    }

    public void register(Context context, String email, String password, String name, RepositoryCallback<ApiService.AuthData> callback) {
        ApiClient.getService(context).register(new ApiService.RegisterRequest(email, password, name))
                .enqueue(new Callback<ApiService.ApiResponse<ApiService.AuthData>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<ApiService.AuthData>> call,
                                           Response<ApiService.ApiResponse<ApiService.AuthData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiService.ApiResponse<ApiService.AuthData> body = response.body();
                            if (body.isSuccess()) {
                                callback.onSuccess(body.getData());
                            } else {
                                String errorMsg = "Email đã tồn tại hoặc không hợp lệ";
                                if (body.getError() != null && body.getError().getMessage() != null) {
                                    errorMsg = body.getError().getMessage();
                                }
                                callback.onError(errorMsg);
                            }
                        } else {
                            callback.onError("Đăng ký thất bại. Mã lỗi: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<ApiService.AuthData>> call, Throwable t) {
                        callback.onError("Lỗi kết nối: " + t.getMessage());
                    }
                });
    }

    public void getProfile(Context context, RepositoryCallback<ApiService.ProfileData> callback) {
        ApiClient.getService(context).getProfile()
                .enqueue(new Callback<ApiService.ApiResponse<ApiService.ProfileData>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<ApiService.ProfileData>> call,
                                           Response<ApiService.ApiResponse<ApiService.ProfileData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiService.ApiResponse<ApiService.ProfileData> body = response.body();
                            if (body.isSuccess()) {
                                callback.onSuccess(body.getData());
                            } else {
                                String errorMsg = "Lỗi khi tải thông tin cá nhân";
                                if (body.getError() != null && body.getError().getMessage() != null) {
                                    errorMsg = body.getError().getMessage();
                                }
                                callback.onError(errorMsg);
                            }
                        } else {
                            callback.onError("Lỗi máy chủ: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<ApiService.ProfileData>> call, Throwable t) {
                        callback.onError("Lỗi kết nối: " + t.getMessage());
                    }
                });
    }

    public void onboarding(Context context, ApiService.OnboardingRequest request, RepositoryCallback<ApiService.ProfileData> callback) {
        ApiClient.getService(context).onboarding(request)
                .enqueue(new Callback<ApiService.ApiResponse<ApiService.ProfileData>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<ApiService.ProfileData>> call,
                                           Response<ApiService.ApiResponse<ApiService.ProfileData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiService.ApiResponse<ApiService.ProfileData> body = response.body();
                            if (body.isSuccess()) {
                                callback.onSuccess(body.getData());
                            } else {
                                String errorMsg = "Lỗi khi lưu thông tin onboarding";
                                if (body.getError() != null && body.getError().getMessage() != null) {
                                    errorMsg = body.getError().getMessage();
                                }
                                callback.onError(errorMsg);
                            }
                        } else {
                            callback.onError("Lỗi máy chủ: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<ApiService.ProfileData>> call, Throwable t) {
                        callback.onError("Lỗi mạng: " + t.getMessage());
                    }
                });
    }

    public void logout(Context context, RepositoryCallback<Void> callback) {
        ApiClient.getService(context).logout()
                .enqueue(new Callback<ApiService.ApiResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<Void>> call, Response<ApiService.ApiResponse<Void>> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess(null);
                        } else {
                            callback.onError("Đăng xuất thất bại trên server");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<Void>> call, Throwable t) {
                        callback.onError("Lỗi kết nối: " + t.getMessage());
                    }
                });
    }
}
