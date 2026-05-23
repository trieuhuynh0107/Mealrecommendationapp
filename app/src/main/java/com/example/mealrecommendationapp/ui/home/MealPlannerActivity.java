package com.example.mealrecommendationapp.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.adapter.MealTimeAdapter;
import com.example.mealrecommendationapp.databinding.ActivityMealPlannerBinding;
import com.example.mealrecommendationapp.model.FoodItem;
import com.example.mealrecommendationapp.model.TimeMeal;
import com.example.mealrecommendationapp.ui.custom.FoodSearchBottomSheetDialog;
import com.example.mealrecommendationapp.ui.survey.SurveyCuisineActivity;
import java.util.Calendar;
import java.util.List;

public class MealPlannerActivity extends AppCompatActivity implements MealPlannerView, FoodSearchBottomSheetDialog.OnFoodSelectedListener {

    private ActivityMealPlannerBinding binding;
    private MealPlannerPresenter presenter;

    private int selectedDayIndex = 0;
    private String selectedTime = "09:00";
    private FoodSearchBottomSheetDialog searchDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMealPlannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new MealPlannerPresenter(this);

        setupDays();
        setupBottomNav();
        setupClicks();
        setupRecycler();
    }

    private void setupDays() {
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        if (today == Calendar.SUNDAY) {
            selectedDayIndex = 6;
        } else {
            selectedDayIndex = today - 2;
        }

        updateTabs(selectedDayIndex);

        binding.dayMon.setOnClickListener(v -> handleTabClick(0));
        binding.dayTue.setOnClickListener(v -> handleTabClick(1));
        binding.dayWed.setOnClickListener(v -> handleTabClick(2));
        binding.dayThu.setOnClickListener(v -> handleTabClick(3));
        binding.dayFri.setOnClickListener(v -> handleTabClick(4));
        binding.daySat.setOnClickListener(v -> handleTabClick(5));
        binding.daySun.setOnClickListener(v -> handleTabClick(6));
    }

    private void handleTabClick(int index) {
        selectedDayIndex = index;
        updateTabs(index);
        setupRecycler();
    }

    private void updateTabs(int index) {
        resetTabColors();

        switch (index) {
            case 0: setTabActive(binding.dayMon); break;
            case 1: setTabActive(binding.dayTue); break;
            case 2: setTabActive(binding.dayWed); break;
            case 3: setTabActive(binding.dayThu); break;
            case 4: setTabActive(binding.dayFri); break;
            case 5: setTabActive(binding.daySat); break;
            case 6: setTabActive(binding.daySun); break;
        }
    }

    private void resetTabColors() {
        TextView[] tabs = {binding.dayMon, binding.dayTue, binding.dayWed, binding.dayThu, binding.dayFri, binding.daySat, binding.daySun};
        for (TextView tab : tabs) {
            tab.setBackgroundResource(0);
            tab.setTextColor(Color.parseColor("#666666"));
        }
    }

    private void setTabActive(TextView selected) {
        selected.setBackgroundResource(R.drawable.bg_button_yellow);
        selected.setTextColor(Color.BLACK);
    }

    private void setupRecycler() {
        binding.recyclerMealTime.setLayoutManager(new LinearLayoutManager(this));
        presenter.loadTimeline(this, selectedDayIndex);
    }

    @Override
    public void showLoading() {
        // Can show a small loader if needed, or loading progress bar
    }

    @Override
    public void hideLoading() {
        // Hide loader
    }

    @Override
    public void onTimelineLoaded(List<TimeMeal> timeline) {
        MealTimeAdapter adapter = new MealTimeAdapter(timeline, new MealTimeAdapter.OnMealClickListener() {
            @Override
            public void onAddClick(String time) {
                selectedTime = time;
                showFoodBottomSheet();
            }

            @Override
            public void onDeleteClick(TimeMeal item) {
                presenter.deleteMeal(MealPlannerActivity.this, item.getMealId());
            }

            @Override
            public void onEditClick(TimeMeal item) {
                showEditQuantityDialog(item);
            }
        });
        binding.recyclerMealTime.setAdapter(adapter);
    }

    @Override
    public void onTimelineFailed(String message) {
        Toast.makeText(this, "Không thể nạp lịch ăn: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMealDeletedSuccessfully() {
        Toast.makeText(this, "Đã xóa món ăn!", Toast.LENGTH_SHORT).show();
        setupRecycler();
    }

    @Override
    public void onMealDeleteFailed(String message) {
        Toast.makeText(this, "Lỗi khi xóa món ăn: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMealUpdatedSuccessfully() {
        Toast.makeText(this, "Thành công!", Toast.LENGTH_SHORT).show();
        setupRecycler();
    }

    @Override
    public void onMealUpdateFailed(String message) {
        Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
    }

    private void showFoodBottomSheet() {
        String dateStr = presenter.getDateOfIndex(selectedDayIndex);
        searchDialog = new FoodSearchBottomSheetDialog(this, dateStr, this);
        searchDialog.show();
    }

    @Override
    public void onFoodSelected(FoodItem foodItem) {
        String dateStr = presenter.getDateOfIndex(selectedDayIndex);
        String scheduledAt = dateStr + "T" + selectedTime + ":00.000Z";
        
        if (searchDialog != null && searchDialog.isShowing()) {
            searchDialog.dismiss();
        }

        presenter.addMeal(this, foodItem.getId(), scheduledAt, 100);
    }

    @Override
    public void onRecommendClick() {
        Intent intent = new Intent(MealPlannerActivity.this, SurveyCuisineActivity.class);
        intent.putExtra("selected_time", selectedTime);
        intent.putExtra("selected_day", selectedDayIndex);
        startActivity(intent);
    }

    private void showEditQuantityDialog(TimeMeal item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cập nhật số lượng");

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(item.getQuantityG() != null ? item.getQuantityG() : 100));
        input.setSelection(input.getText().length());
        builder.setView(input);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String val = input.getText().toString().trim();
            if (!val.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(val);
                    if (quantity <= 0) {
                        Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    presenter.updateMealQuantity(MealPlannerActivity.this, item.getMealId(), quantity);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void setupBottomNav() {
        binding.btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(MealPlannerActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupClicks() {
        binding.btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(MealPlannerActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}