package com.example.mealrecommendationapp.ui.custom;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.mealrecommendationapp.adapter.FoodAdapter;
import com.example.mealrecommendationapp.databinding.BottomSheetFoodBinding;
import com.example.mealrecommendationapp.data.MealRepository;
import com.example.mealrecommendationapp.data.RepositoryCallback;
import com.example.mealrecommendationapp.model.FoodItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.List;

public class FoodSearchBottomSheetDialog extends BottomSheetDialog {

    public interface OnFoodSelectedListener {
        void onFoodSelected(FoodItem foodItem);
        void onRecommendClick();
    }

    private final BottomSheetFoodBinding binding;
    private final MealRepository mealRepository;
    private final OnFoodSelectedListener listener;
    private final String dateStr;

    public FoodSearchBottomSheetDialog(@NonNull Context context, String dateStr, OnFoodSelectedListener listener) {
        super(context);
        this.dateStr = dateStr;
        this.listener = listener;
        this.mealRepository = MealRepository.getInstance();

        binding = BottomSheetFoodBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());

        initViews();
    }

    private void initViews() {
        binding.recyclerSearchFood.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initial state: Load smart recommendations
        loadRecommendations();

        // Dynamic typing search using a TextWatcher
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    searchFoods(query);
                } else {
                    loadRecommendations();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnRecommend.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onRecommendClick();
            }
        });
    }

    private void searchFoods(String query) {
        mealRepository.searchFoods(getContext(), query, 1, 20, new RepositoryCallback<List<FoodItem>>() {
            @Override
            public void onSuccess(List<FoodItem> foods) {
                updateBottomSheetRecycler(foods);
            }

            @Override
            public void onError(String errorMessage) {
                // Fail silently
            }
        });
    }

    private void loadRecommendations() {
        mealRepository.getRecommendations(getContext(), dateStr, null, new RepositoryCallback<List<FoodItem>>() {
            @Override
            public void onSuccess(List<FoodItem> foods) {
                updateBottomSheetRecycler(foods);
            }

            @Override
            public void onError(String errorMessage) {
                // Fail silently
            }
        });
    }

    private void updateBottomSheetRecycler(List<FoodItem> foods) {
        FoodAdapter adapter = new FoodAdapter(foods, foodItem -> {
            if (listener != null) {
                listener.onFoodSelected(foodItem);
            }
        });
        binding.recyclerSearchFood.setAdapter(adapter);
    }
}
