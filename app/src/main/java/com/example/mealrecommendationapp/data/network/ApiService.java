package com.example.mealrecommendationapp.data.network;

import com.example.mealrecommendationapp.model.FoodItem;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ==================== AUTH ====================

    @POST("/auth/register")
    Call<ApiResponse<AuthData>> register(@Body RegisterRequest request);

    @POST("/auth/login")
    Call<ApiResponse<AuthData>> login(@Body LoginRequest request);

    @POST("/auth/refresh")
    Call<ApiResponse<AuthData>> refreshTokenSync(@Body RefreshRequest request);

    @POST("/auth/logout")
    Call<ApiResponse<Void>> logout();

    // ==================== USERS ====================

    @GET("/users/me")
    Call<ApiResponse<ProfileData>> getProfile();

    @POST("/users/onboarding")
    Call<ApiResponse<ProfileData>> onboarding(@Body OnboardingRequest request);

    @PATCH("/users/me")
    Call<ApiResponse<ProfileData>> updateProfile(@Body UpdateProfileRequest request);

    // ==================== FOODS ====================

    @GET("/foods/search")
    Call<ApiResponse<List<FoodItem>>> searchFoods(
            @Query("q") String query,
            @Query("page") int page,
            @Query("limit") int limit
    );

    // ==================== MEALS ====================

    @GET("/meals")
    Call<ApiResponse<List<MealResponse>>> getMeals(
            @Query("date") String date
    );

    @POST("/meals")
    Call<ApiResponse<MealResponse>> addMeal(
            @Body AddMealRequest request
    );

    @PATCH("/meals/{id}")
    Call<ApiResponse<MealResponse>> updateMeal(
            @Path("id") String mealId,
            @Body UpdateMealRequest request
    );

    @DELETE("/meals/{id}")
    Call<Void> deleteMeal(
            @Path("id") String mealId
    );

    // ==================== SUMMARY ====================

    @GET("/summary/daily")
    Call<ApiResponse<SummaryData>> getDailySummary(
            @Query("date") String date
    );

    // ==================== RECOMMEND ====================

    @GET("/metadata/recommend-options")
    Call<ApiResponse<RecommendOptionsResponse>> getRecommendOptions();

    @GET("/recommend")
    Call<ApiResponse<List<FoodItem>>> getRecommendations(
            @Query("date") String date,
            @Query("ingredients") String ingredients
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

        @SerializedName("date_of_birth")
        private String dateOfBirth;

        @SerializedName("height_cm")
        private int heightCm;

        @SerializedName("weight_kg")
        private int weightKg;

        @SerializedName("daily_calories")
        private double dailyCalories;

        @SerializedName("daily_protein")
        private double dailyProtein;

        @SerializedName("daily_carbs")
        private double dailyCarbs;

        @SerializedName("daily_fat")
        private double dailyFat;

        @SerializedName("is_onboarding_complete")
        private boolean isOnboardingComplete;

        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getGender() { return gender; }
        public String getDateOfBirth() { return dateOfBirth; }
        public int getHeightCm() { return heightCm; }
        public int getWeightKg() { return weightKg; }
        public double getDailyCalories() { return dailyCalories; }
        public double getDailyProtein() { return dailyProtein; }
        public double getDailyCarbs() { return dailyCarbs; }
        public double getDailyFat() { return dailyFat; }
        public boolean isOnboardingComplete() { return isOnboardingComplete; }
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

    class UpdateMealRequest {
        private String scheduledAt;
        private Integer quantityG;

        public UpdateMealRequest(String scheduledAt, Integer quantityG) {
            this.scheduledAt = scheduledAt;
            this.quantityG = quantityG;
        }
    }

    class MealResponse {
        private String id;

        @SerializedName("food_id")
        private String foodId;

        @SerializedName("scheduled_at")
        private String scheduledAt;

        @SerializedName("quantity_g")
        private int quantityG;

        @SerializedName("calories_snap")
        private double caloriesSnap;

        @SerializedName("protein_snap")
        private double proteinSnap;

        @SerializedName("carbs_snap")
        private double carbsSnap;

        @SerializedName("fat_snap")
        private double fatSnap;

        @SerializedName("food_name")
        private String foodName;

        @SerializedName("food_image_url")
        private String foodImageUrl;

        public String getId() { return id; }
        public String getFoodId() { return foodId; }
        public String getScheduledAt() { return scheduledAt; }
        public int getQuantityG() { return quantityG; }
        public double getCaloriesSnap() { return caloriesSnap; }
        public double getProteinSnap() { return proteinSnap; }
        public double getCarbsSnap() { return carbsSnap; }
        public double getFatSnap() { return fatSnap; }
        public String getFoodName() { return foodName; }
        public String getFoodImageUrl() { return foodImageUrl; }
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

    class RecommendOptionsResponse {
        private int version;
        private List<String> cuisines;
        private List<String> allergies;
        private List<String> dietTags;
        private List<String> popularIngredients;

        public int getVersion() { return version; }
        public List<String> getCuisines() { return cuisines; }
        public List<String> getAllergies() { return allergies; }
        public List<String> getDietTags() { return dietTags; }
        public List<String> getPopularIngredients() { return popularIngredients; }
    }
}
