package com.example.mealrecommendationapp.data.network;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalCacheManager {

    private static final String PREF_NAME = "tastee_app_cache";
    private static final String KEY_VERSION = "cache_version";
    private static final String KEY_CUISINES = "cache_cuisines";
    private static final String KEY_ALLERGIES = "cache_allergies";
    private static final String KEY_DIET_TAGS = "cache_diet_tags";
    private static final String KEY_POPULAR_INGREDIENTS = "cache_popular_ingredients";

    // --- MẢNG HARDCODE DO AI CHỐT ĐỂ CHẠY CỨNG TỨC THÌ ---
    private static final List<String> HARDCODED_CUISINES = Arrays.asList(
        "american", "asian", "italian", "mexican", "indian", "greek", "french", "chinese", "thai", "spanish", "japanese", "vietnamese", "korean"
    );
    private static final List<String> HARDCODED_ALLERGIES = Arrays.asList(
        "seafood", "fish", "nuts", "shellfish", "shrimp", "crab", "eggs", "eggs-dairy", "soy-tofu", "peanut-butter"
    );
    private static final List<String> HARDCODED_DIET_TAGS = Arrays.asList(
        "low-carb", "healthy", "vegetarian", "low-fat", "vegan", "very-low-carbs", "high-protein", "gluten-free"
    );
    private static final List<String> HARDCODED_INGREDIENTS = Arrays.asList(
        "Chicken", "Beef", "Pork", "Egg", "Rice", "Onion", "Garlic", "Tomato", "Potato", 
        "Carrot", "Shrimp", "Fish", "Tofu", "Mushroom", "Cheese", "Milk", "Cabbage", 
        "Spinach", "Cucumber", "Ginger", "Chili", "Bell Pepper", "Lemon", "Butter", 
        "Noodle", "Pork Belly", "Salmon", "Broccoli", "Sausage", "Bacon"
    );

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public LocalCacheManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void saveOptions(ApiService.RecommendOptionsResponse response) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_VERSION, response.getVersion());
        editor.putString(KEY_CUISINES, gson.toJson(response.getCuisines()));
        editor.putString(KEY_ALLERGIES, gson.toJson(response.getAllergies()));
        editor.putString(KEY_DIET_TAGS, gson.toJson(response.getDietTags()));
        editor.putString(KEY_POPULAR_INGREDIENTS, gson.toJson(response.getPopularIngredients()));
        editor.apply();
    }

    public int getCachedVersion() {
        return sharedPreferences.getInt(KEY_VERSION, 0);
    }

    public List<String> getCuisines() {
        String json = sharedPreferences.getString(KEY_CUISINES, null);
        if (json == null) return HARDCODED_CUISINES; // Nạp cứng ngay lập tức nếu chưa sync
        return gson.fromJson(json, new TypeToken<List<String>>(){}.getType());
    }

    public List<String> getAllergies() {
        String json = sharedPreferences.getString(KEY_ALLERGIES, null);
        if (json == null) return HARDCODED_ALLERGIES;
        return gson.fromJson(json, new TypeToken<List<String>>(){}.getType());
    }

    public List<String> getDietTags() {
        String json = sharedPreferences.getString(KEY_DIET_TAGS, null);
        if (json == null) return HARDCODED_DIET_TAGS;
        return gson.fromJson(json, new TypeToken<List<String>>(){}.getType());
    }

    public List<String> getPopularIngredients() {
        String json = sharedPreferences.getString(KEY_POPULAR_INGREDIENTS, null);
        if (json == null) return HARDCODED_INGREDIENTS;
        return gson.fromJson(json, new TypeToken<List<String>>(){}.getType());
    }

    public boolean hasCache() {
        return getCachedVersion() > 0;
    }
}
