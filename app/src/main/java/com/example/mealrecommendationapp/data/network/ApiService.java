package com.example.mealrecommendationapp.data.network;

import com.example.mealrecommendationapp.model.FoodItem;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PATCH;
import retrofit2.http.Query;

public interface ApiService {

    // ==================== AUTH ====================

    @POST("auth/register")
    Call<ApiResponse<AuthData>> register(@Body RegisterRequest request);

    @POST("auth/login")
    Call<ApiResponse<AuthData>> login(@Body LoginRequest request);

    @POST("auth/refresh")
    Call<ApiResponse<AuthData>> refreshTokenSync(@Body RefreshRequest request);

    // ==================== USERS ====================

    @GET("users/me")
    Call<ApiResponse<ProfileData>> getProfile();

    @POST("users/onboarding")
    Call<ApiResponse<ProfileData>> onboarding(@Body OnboardingRequest request);

    @PATCH("users/me")
    Call<ApiResponse<ProfileData>> updateProfile(@Body UpdateProfileRequest request);

    // ==================== FOODS ====================

    @GET("foods/search")
    Call<ApiResponse<List<FoodItem>>> searchFoods(
            @Query("q") String query,
            @Query("page") int page,
            @Query("limit") int limit
    );

    // ==================== MEALS ====================

    @GET("meals")
    Call<ApiResponse<List<MealResponse>>> getMeals(
            @Query("date") String date
    );

    @POST("meals")
    Call<ApiResponse<MealResponse>> addMeal(
            @Body AddMealRequest request
    );

    // ==================== SUMMARY ====================

    @GET("summary/daily")
    Call<ApiResponse<SummaryData>> getDailySummary(
            @Query("date") String date
    );

    // ==================== RECOMMEND ====================

    @GET("recommend")
    Call<ApiResponse<List<FoodItem>>> getRecommendations(
            @Query("date") String date
    );

    // ==================== DTO CLASSES ====================

    class ApiResponse<T> {
        private boolean success;
        private T data;
        private String message;
        private ErrorDetails error;

        public boolean isSuccess() { return success; }
        public T getData() { return data; }
        public String getMessage() { return message; }
        public ErrorDetails getError() { return error; }
    }

    class ErrorDetails {
        private String code;
        private String message;

        public String getCode() { return code; }
        public String getMessage() { return message; }
    }

    class RegisterRequest {
        private String email;
        private String password;
        private String name;

        public RegisterRequest(String email, String password, String name) {
            this.email = email;
            this.password = password;
            this.name = name;
        }
    }

    class LoginRequest {
        private String email;
        private String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    class RefreshRequest {
        private String refreshToken;

        public RefreshRequest(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    class AuthData {
        private String accessToken;
        private String refreshToken;

        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
    }

    class ProfileData {
        private String id;
        private String name;
        private String email;
        private String gender;
        private String date_of_birth;
        private int height_cm;
        private int weight_kg;
        private double daily_calories;
        private double daily_protein;
        private double daily_carbs;
        private double daily_fat;
        private boolean is_onboarding_complete;

        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getGender() { return gender; }
        public String getDateOfBirth() { return date_of_birth; }
        public int getHeightCm() { return height_cm; }
        public int getWeightKg() { return weight_kg; }
        public double getDailyCalories() { return daily_calories; }
        public double getDailyProtein() { return daily_protein; }
        public double getDailyCarbs() { return daily_carbs; }
        public double getDailyFat() { return daily_fat; }
        public boolean isOnboardingComplete() { return is_onboarding_complete; }
    }

    class OnboardingRequest {
        private String gender;
        private String dateOfBirth;
        private int heightCm;
        private int weightKg;
        private List<String> cuisines;
        private List<String> allergies;
        private List<String> dietTags;

        public OnboardingRequest(String gender, String dateOfBirth, int heightCm, int weightKg, 
                                 List<String> cuisines, List<String> allergies, List<String> dietTags) {
            this.gender = gender;
            this.dateOfBirth = dateOfBirth;
            this.heightCm = heightCm;
            this.weightKg = weightKg;
            this.cuisines = cuisines;
            this.allergies = allergies;
            this.dietTags = dietTags;
        }
    }

    class UpdateProfileRequest {
        private String name;
        private String gender;
        private String dateOfBirth;
        private Integer heightCm;
        private Integer weightKg;
        private List<String> cuisines;
        private List<String> allergies;
        private List<String> dietTags;

        public UpdateProfileRequest(String name, String gender, String dateOfBirth, Integer heightCm, Integer weightKg) {
            this.name = name;
            this.gender = gender;
            this.dateOfBirth = dateOfBirth;
            this.heightCm = heightCm;
            this.weightKg = weightKg;
        }

        public UpdateProfileRequest(List<String> cuisines, List<String> allergies, List<String> dietTags) {
            this.cuisines = cuisines;
            this.allergies = allergies;
            this.dietTags = dietTags;
        }
    }

    class AddMealRequest {
        private String foodId;
        private String scheduledAt;
        private int quantityG;

        public AddMealRequest(String foodId, String scheduledAt, int quantityG) {
            this.foodId = foodId;
            this.scheduledAt = scheduledAt;
            this.quantityG = quantityG;
        }
    }

    class MealResponse {
        private String id;
        private String food_id;
        private String scheduled_at;
        private int quantity_g;
        private double calories_snap;
        private double protein_snap;
        private double carbs_snap;
        private double fat_snap;
        private String food_name;
        private String food_image_url;

        public String getId() { return id; }
        public String getFoodId() { return food_id; }
        public String getScheduledAt() { return scheduled_at; }
        public int getQuantityG() { return quantity_g; }
        public double getCaloriesSnap() { return calories_snap; }
        public double getProteinSnap() { return protein_snap; }
        public double getCarbsSnap() { return carbs_snap; }
        public double getFatSnap() { return fat_snap; }
        public String getFoodName() { return food_name; }
        public String getFoodImageUrl() { return food_image_url; }
    }

    class SummaryData {
        private String date;
        private int mealCount;
        private NutritionValues actual;
        private NutritionValues target;
        private NutritionValues remaining;
        private NutritionValues percentage;

        public String getDate() { return date; }
        public int getMealCount() { return mealCount; }
        public NutritionValues getActual() { return actual; }
        public NutritionValues getTarget() { return target; }
        public NutritionValues getRemaining() { return remaining; }
        public NutritionValues getPercentage() { return percentage; }
    }

    class NutritionValues {
        private double calories;
        private double protein;
        private double carbs;
        private double fat;

        public double getCalories() { return calories; }
        public double getProtein() { return protein; }
        public double getCarbs() { return carbs; }
        public double getFat() { return fat; }
    }
}
