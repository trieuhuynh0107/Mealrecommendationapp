package com.example.mealrecommendationapp.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.data.MealRepository;
import com.example.mealrecommendationapp.data.RepositoryCallback;
import com.example.mealrecommendationapp.databinding.ActivityFoodDetailBinding;
import com.example.mealrecommendationapp.model.FoodItem;
import com.google.android.flexbox.FlexboxLayout;

public class FoodDetailActivity extends AppCompatActivity {

    private ActivityFoodDetailBinding binding;
    private MealRepository mealRepository;

    private String foodId;
    private String scheduledAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mealRepository = MealRepository.getInstance();

        foodId = getIntent().getStringExtra("food_id");
        scheduledAt = getIntent().getStringExtra("scheduled_at");

        setupToolbar();

        // API Call Optimization: Check for preloaded FoodItem from Intent to render instantly
        FoodItem preloadedFood = (FoodItem) getIntent().getSerializableExtra("food_item");
        if (preloadedFood != null) {
            bindFoodData(preloadedFood);
        }

        loadFoodDetail();
    }

    private void setupToolbar() {
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void loadFoodDetail() {
        if (foodId == null || foodId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy món ăn!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mealRepository.getFoodDetail(this, foodId, new RepositoryCallback<FoodItem>() {
            @Override
            public void onSuccess(FoodItem food) {
                bindFoodData(food);
            }

            @Override
            public void onError(String errorMessage) {
                // If we already have preloaded data, fail silently on network errors to maintain resilient offline usability
                if (getIntent().getSerializableExtra("food_item") == null) {
                    Toast.makeText(FoodDetailActivity.this, "Lỗi tải thông tin: " + errorMessage, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void bindFoodData(FoodItem food) {
        // Name & translation if available
        String name = food.getName();
        if (food.getNameVi() != null && !food.getNameVi().isEmpty()) {
            name = food.getNameVi() + " (" + food.getName() + ")";
        }
        binding.txtFoodName.setText(name);

        // Nutrition numbers
        binding.txtValKcal.setText((int) food.getCalories() + " Kcal");
        binding.txtValCarbs.setText((int) food.getCarbs() + "g");
        binding.txtValProtein.setText((int) food.getProtein() + "g");
        binding.txtValFats.setText((int) food.getFats() + "g");

        // Dynamic ingredients loading logic
        displayIngredients(food.getIngredients());

        // Setup bottom action bar adding mechanism
        if (scheduledAt != null && !scheduledAt.isEmpty()) {
            binding.bottomActionBar.setVisibility(View.VISIBLE);
            binding.btnAddToPlan.setOnClickListener(v -> addMealToPlanner(food.getId()));
        } else {
            binding.bottomActionBar.setVisibility(View.GONE);
        }
    }

    private void displayIngredients(String foodIngredients) {
        binding.flexboxIngredients.removeAllViews();
        if (foodIngredients == null || foodIngredients.trim().isEmpty()) {
            return;
        }

        String[] items = foodIngredients.split(",");
        for (String item : items) {
            String name = item.trim();
            if (name.isEmpty()) continue;

            String formattedName = formatIngredientWithEmoji(name);

            TextView chip = new TextView(this);
            chip.setText(formattedName);
            chip.setTextSize(14);
            chip.setPadding(28, 14, 28, 14);
            chip.setBackgroundResource(R.drawable.bg_survey_selected); // Style as a beautiful active premium yellow chip
            chip.setTextColor(Color.parseColor("#222222"));

            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 8, 8, 8);
            chip.setLayoutParams(params);

            binding.flexboxIngredients.addView(chip);
        }
    }

    private String formatIngredientWithEmoji(String ingredient) {
        String lower = ingredient.toLowerCase();
        String capitalized = capitalize(ingredient);

        if (lower.contains("pepperoni")) return "🍕 " + capitalized;
        if (lower.contains("mushroom")) return "🍄 " + capitalized;
        if (lower.contains("onion")) return "🧅 " + capitalized;
        if (lower.contains("sausage")) return "🌭 " + capitalized;
        if (lower.contains("cheese") || lower.contains("mozzarella") || lower.contains("cheddar")) return "🧀 " + capitalized;
        if (lower.contains("olive")) return "🫒 " + capitalized;
        if (lower.contains("pepper")) return "🫑 " + capitalized;
        if (lower.contains("pineapple")) return "🍍 " + capitalized;
        if (lower.contains("spinach")) return "🥬 " + capitalized;
        if (lower.contains("chicken")) return "🍗 " + capitalized;
        if (lower.contains("beef") || lower.contains("steak")) return "🥩 " + capitalized;
        if (lower.contains("pork") || lower.contains("bacon")) return "🥓 " + capitalized;
        if (lower.contains("egg")) return "🥚 " + capitalized;
        if (lower.contains("tomato")) return "🍅 " + capitalized;
        if (lower.contains("garlic")) return "🧄 " + capitalized;
        if (lower.contains("shrimp")) return "🍤 " + capitalized;
        if (lower.contains("fish")) return "🐟 " + capitalized;
        if (lower.contains("bread") || lower.contains("flour")) return "🍞 " + capitalized;
        if (lower.contains("butter")) return "🧈 " + capitalized;
        if (lower.contains("milk")) return "🥛 " + capitalized;
        if (lower.contains("honey")) return "🍯 " + capitalized;
        if (lower.contains("apple")) return "🍎 " + capitalized;
        if (lower.contains("banana")) return "🍌 " + capitalized;
        if (lower.contains("potato")) return "🥔 " + capitalized;
        if (lower.contains("carrot")) return "🥕 " + capitalized;
        if (lower.contains("lemon")) return "🍋 " + capitalized;

        return capitalized;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private void addMealToPlanner(String targetFoodId) {
        binding.btnAddToPlan.setEnabled(false);
        mealRepository.addMeal(this, targetFoodId, scheduledAt, 100, new RepositoryCallback<com.example.mealrecommendationapp.data.network.ApiService.MealResponse>() {
            @Override
            public void onSuccess(com.example.mealrecommendationapp.data.network.ApiService.MealResponse data) {
                Toast.makeText(FoodDetailActivity.this, "Đã thêm món ăn vào thực đơn!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(FoodDetailActivity.this, MealPlannerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                binding.btnAddToPlan.setEnabled(true);
                Toast.makeText(FoodDetailActivity.this, "Lỗi thêm món ăn: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
