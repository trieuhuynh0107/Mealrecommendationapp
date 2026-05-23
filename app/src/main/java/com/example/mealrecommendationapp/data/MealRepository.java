package com.example.mealrecommendationapp.data;

import android.content.Context;
import com.example.mealrecommendationapp.data.network.ApiClient;
import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.model.FoodItem;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealRepository {

    private static MealRepository instance;

    private MealRepository() {}

    public static synchronized MealRepository getInstance() {
        if (instance == null) {
            instance = new MealRepository();
        }
        return instance;
    }

    public void getDailySummary(Context context, String date, RepositoryCallback<ApiService.SummaryData> callback) {
        ApiClient.getService(context).getDailySummary(date)
                .enqueue(new Callback<ApiService.ApiResponse<ApiService.SummaryData>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<ApiService.SummaryData>> call,
                                           Response<ApiService.ApiResponse<ApiService.SummaryData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiService.ApiResponse<ApiService.SummaryData> body = response.body();
                            if (body.isSuccess()) {
                                callback.onSuccess(body.getData());
                            } else {
                                String errorMsg = "Lỗi khi tải thông tin dinh dưỡng";
                                String errorCode = null;
                                if (body.getError() != null) {
                                    errorMsg = body.getError().getMessage();
                                    errorCode = body.getError().getCode();
                                }
                                // We can pass structured errors or a custom exception message
                                if ("ONBOARDING_REQUIRED".equals(errorCode)) {
                                    callback.onError("ONBOARDING_REQUIRED");
                                } else {
                                    callback.onError(errorMsg);
                                }
                            }
                        } else {
                            if (response.code() == 400) {
                                callback.onError("ONBOARDING_REQUIRED");
                            } else if (response.code() == 401) {
                                callback.onError("UNAUTHORIZED");
                            } else {
                                callback.onError("Lỗi kết nối máy chủ: " + response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<ApiService.SummaryData>> call, Throwable t) {
                        callback.onError("Lỗi mạng: " + t.getMessage());
                    }
                });
    }

    public void getMeals(Context context, String date, RepositoryCallback<List<ApiService.MealResponse>> callback) {
        ApiClient.getService(context).getMeals(date)
                .enqueue(new Callback<ApiService.ApiResponse<List<ApiService.MealResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<List<ApiService.MealResponse>>> call,
                                           Response<ApiService.ApiResponse<List<ApiService.MealResponse>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiService.ApiResponse<List<ApiService.MealResponse>> body = response.body();
                            if (body.isSuccess()) {
                                callback.onSuccess(body.getData());
                            } else {
                                String errorMsg = "Lỗi tải danh sách bữa ăn";
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
                    public void onFailure(Call<ApiService.ApiResponse<List<ApiService.MealResponse>>> call, Throwable t) {
                        callback.onError("Lỗi mạng: " + t.getMessage());
                    }
                });
    }

    public void addMeal(Context context, String foodId, String scheduledAt, int quantityG, RepositoryCallback<ApiService.MealResponse> callback) {
        ApiClient.getService(context).addMeal(new ApiService.AddMealRequest(foodId, scheduledAt, quantityG))
                .enqueue(new Callback<ApiService.ApiResponse<ApiService.MealResponse>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<ApiService.MealResponse>> call,
                                           Response<ApiService.ApiResponse<ApiService.MealResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiService.ApiResponse<ApiService.MealResponse> body = response.body();
                            if (body.isSuccess()) {
                                callback.onSuccess(body.getData());
                            } else {
                                String errorMsg = "Lỗi khi thêm món ăn";
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
                    public void onFailure(Call<ApiService.ApiResponse<ApiService.MealResponse>> call, Throwable t) {
                        callback.onError("Lỗi mạng: " + t.getMessage());
                    }
                });
    }

    public void updateMeal(Context context, String mealId, int quantityG, RepositoryCallback<ApiService.MealResponse> callback) {
        ApiClient.getService(context).updateMeal(mealId, new ApiService.UpdateMealRequest(null, quantityG))
                .enqueue(new Callback<ApiService.ApiResponse<ApiService.MealResponse>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<ApiService.MealResponse>> call,
                                           Response<ApiService.ApiResponse<ApiService.MealResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiService.ApiResponse<ApiService.MealResponse> body = response.body();
                            if (body.isSuccess()) {
                                callback.onSuccess(body.getData());
                            } else {
                                String errorMsg = "Lỗi khi cập nhật số lượng";
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
                    public void onFailure(Call<ApiService.ApiResponse<ApiService.MealResponse>> call, Throwable t) {
                        callback.onError("Lỗi mạng: " + t.getMessage());
                    }
                });
    }

    public void deleteMeal(Context context, String mealId, RepositoryCallback<Void> callback) {
        ApiClient.getService(context).deleteMeal(mealId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess(null);
                        } else {
                            callback.onError("Lỗi khi xóa bữa ăn: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onError("Lỗi mạng: " + t.getMessage());
                    }
                });
    }

    public void searchFoods(Context context, String query, int page, int limit, RepositoryCallback<List<FoodItem>> callback) {
        ApiClient.getService(context).searchFoods(query, page, limit)
                .enqueue(new Callback<ApiService.ApiResponse<List<FoodItem>>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<List<FoodItem>>> call, Response<ApiService.ApiResponse<List<FoodItem>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiService.ApiResponse<List<FoodItem>> body = response.body();
                            if (body.isSuccess()) {
                                callback.onSuccess(body.getData());
                            } else {
                                callback.onError("Không thể tìm kiếm món ăn");
                            }
                        } else {
                            callback.onError("Lỗi máy chủ: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<List<FoodItem>>> call, Throwable t) {
                        callback.onError("Lỗi mạng: " + t.getMessage());
                    }
                });
    }

    public void getRecommendations(Context context, String date, String ingredients, RepositoryCallback<List<FoodItem>> callback) {
        ApiClient.getService(context).getRecommendations(date, ingredients)
                .enqueue(new Callback<ApiService.ApiResponse<List<FoodItem>>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<List<FoodItem>>> call, Response<ApiService.ApiResponse<List<FoodItem>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiService.ApiResponse<List<FoodItem>> body = response.body();
                            if (body.isSuccess()) {
                                callback.onSuccess(body.getData());
                            } else {
                                callback.onError("Không thể tải gợi ý món ăn");
                            }
                        } else {
                            callback.onError("Lỗi máy chủ: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<List<FoodItem>>> call, Throwable t) {
                        callback.onError("Lỗi mạng: " + t.getMessage());
                    }
                });
    }
}
