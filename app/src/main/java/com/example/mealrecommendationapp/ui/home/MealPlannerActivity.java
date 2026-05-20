package com.example.mealrecommendationapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.adapter.FoodAdapter;
import com.example.mealrecommendationapp.adapter.MealTimeAdapter;
import com.example.mealrecommendationapp.data.network.ApiClient;
import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.model.FoodItem;
import com.example.mealrecommendationapp.model.TimeMeal;
import com.example.mealrecommendationapp.ui.survey.SurveyCuisineActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealPlannerActivity extends AppCompatActivity {

    private RecyclerView recyclerMealTime;

    private TextView btnHome;
    private TextView btnMenu;

    private TextView btnMon, btnTue, btnWed, btnThu, btnFri, btnSat, btnSun;

    private int selectedDayIndex = 0;
    private String selectedTime = "09:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_planner);

        initViews();
        setupDays();
        setupBottomNav();
        setupClicks();
        setupRecycler();
    }

    private void initViews() {
        recyclerMealTime = findViewById(R.id.recyclerMealTime);
        btnHome = findViewById(R.id.btnHome);
        btnMenu = findViewById(R.id.btnMenu);

        btnMon = findViewById(R.id.dayMon);
        btnTue = findViewById(R.id.dayTue);
        btnWed = findViewById(R.id.dayWed);
        btnThu = findViewById(R.id.dayThu);
        btnFri = findViewById(R.id.dayFri);
        btnSat = findViewById(R.id.daySat);
        btnSun = findViewById(R.id.daySun);
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

        btnMon.setOnClickListener(v -> handleTabClick(0));
        btnTue.setOnClickListener(v -> handleTabClick(1));
        btnWed.setOnClickListener(v -> handleTabClick(2));
        btnThu.setOnClickListener(v -> handleTabClick(3));
        btnFri.setOnClickListener(v -> handleTabClick(4));
        btnSat.setOnClickListener(v -> handleTabClick(5));
        btnSun.setOnClickListener(v -> handleTabClick(6));
    }

    private void handleTabClick(int index) {
        selectedDayIndex = index;
        updateTabs(index);
        setupRecycler();
    }

    private void updateTabs(int index) {
        resetTabColors();

        switch (index) {
            case 0: setTabActive(btnMon); break;
            case 1: setTabActive(btnTue); break;
            case 2: setTabActive(btnWed); break;
            case 3: setTabActive(btnThu); break;
            case 4: setTabActive(btnFri); break;
            case 5: setTabActive(btnSat); break;
            case 6: setTabActive(btnSun); break;
        }
    }

    private void resetTabColors() {
        TextView[] tabs = {btnMon, btnTue, btnWed, btnThu, btnFri, btnSat, btnSun};
        for (TextView tab : tabs) {
            tab.setBackgroundResource(0);
            tab.setTextColor(android.graphics.Color.parseColor("#666666"));
        }
    }

    private void setTabActive(TextView selected) {
        selected.setBackgroundResource(R.drawable.bg_button_yellow);
        selected.setTextColor(getResources().getColor(android.R.color.black));
    }

    private String getDateOfIndex(int dayIndex) {
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_WEEK);
        int currentDayIndex = (today == Calendar.SUNDAY) ? 6 : today - 2;
        int diff = dayIndex - currentDayIndex;
        cal.add(Calendar.DAY_OF_YEAR, diff);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(cal.getTime());
    }

    private boolean isHourMatch(String scheduledAt, String time) {
        if (scheduledAt == null) return false;
        try {
            String cleaned = scheduledAt.replace("T", " ");
            if (cleaned.endsWith("Z")) {
                cleaned = cleaned.substring(0, cleaned.length() - 1);
            }
            if (cleaned.contains(".")) {
                cleaned = cleaned.split("\\.")[0];
            }
            
            java.text.SimpleDateFormat parser = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            if (scheduledAt.contains("Z") || scheduledAt.contains("T")) {
                parser.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            } else {
                parser.setTimeZone(java.util.TimeZone.getDefault());
            }
            
            java.util.Date date = parser.parse(cleaned);
            if (date == null) return false;
            
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("HH:mm", Locale.US);
            formatter.setTimeZone(java.util.TimeZone.getDefault());
            String localHourMin = formatter.format(date);
            
            return localHourMin.equals(time);
        } catch (Exception e) {
            return false;
        }
    }

    private void setupRecycler() {
        recyclerMealTime.setLayoutManager(new LinearLayoutManager(this));

        String dateStr = getDateOfIndex(selectedDayIndex);

        // Fetch meals from backend API
        ApiClient.getService(this).getMeals(dateStr)
                .enqueue(new Callback<ApiService.ApiResponse<List<ApiService.MealResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<List<ApiService.MealResponse>>> call,
                                           Response<ApiService.ApiResponse<List<ApiService.MealResponse>>> response) {
                        List<ApiService.MealResponse> mealsList = new ArrayList<>();
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            mealsList = response.body().getData();
                        }

                        List<TimeMeal> timeline = new ArrayList<>();
                        for (int hour = 9; hour <= 21; hour++) {
                            String time = (hour < 10) ? "0" + hour + ":00" : hour + ":00";

                            TimeMeal timeMeal = new TimeMeal(selectedDayIndex, time);

                            // Search for a matching meal inside the API response
                            for (ApiService.MealResponse meal : mealsList) {
                                if (isHourMatch(meal.getScheduledAt(), time)) {
                                    FoodItem foodItem = new FoodItem(
                                            meal.getFoodId(),
                                            meal.getFoodName(),
                                            (int) meal.getCaloriesSnap(),
                                            (int) meal.getProteinSnap(),
                                            (int) meal.getFatSnap(),
                                            (int) meal.getCarbsSnap(),
                                            meal.getFoodImageUrl()
                                    );
                                    timeMeal.setFoodItem(foodItem);
                                    break;
                                }
                            }

                            timeline.add(timeMeal);
                        }

                        MealTimeAdapter adapter = new MealTimeAdapter(timeline, time -> {
                            selectedTime = time;
                            showFoodBottomSheet();
                        });
                        recyclerMealTime.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<List<ApiService.MealResponse>>> call, Throwable t) {
                        // Safe fallback using empty list
                        List<TimeMeal> timeline = new ArrayList<>();
                        for (int hour = 9; hour <= 21; hour++) {
                            String time = (hour < 10) ? "0" + hour + ":00" : hour + ":00";
                            timeline.add(new TimeMeal(selectedDayIndex, time));
                        }
                        MealTimeAdapter adapter = new MealTimeAdapter(timeline, time -> {
                            selectedTime = time;
                            showFoodBottomSheet();
                        });
                        recyclerMealTime.setAdapter(adapter);
                    }
                });
    }

    private void setupBottomNav() {
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(MealPlannerActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupClicks() {
        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(MealPlannerActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void showFoodBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_food, null);
        dialog.setContentView(view);

        RecyclerView recyclerSearchFood = view.findViewById(R.id.recyclerSearchFood);
        Button btnRecommend = view.findViewById(R.id.btnRecommend);
        EditText edtSearch = view.findViewById(R.id.edtSearch);

        recyclerSearchFood.setLayoutManager(new LinearLayoutManager(this));

        // Initial state: Load smart recommendations
        loadRecommendations(recyclerSearchFood, dialog);

        // Dynamic typing search using a TextWatcher
        edtSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    searchFoods(query, recyclerSearchFood, dialog);
                } else {
                    loadRecommendations(recyclerSearchFood, dialog);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        btnRecommend.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(MealPlannerActivity.this, SurveyCuisineActivity.class);
            intent.putExtra("selected_time", selectedTime);
            intent.putExtra("selected_day", selectedDayIndex);
            startActivity(intent);
        });

        dialog.show();
    }

    private void searchFoods(String query, RecyclerView recyclerView, BottomSheetDialog dialog) {
        ApiClient.getService(this).searchFoods(query, 1, 20)
                .enqueue(new Callback<ApiService.ApiResponse<List<FoodItem>>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<List<FoodItem>>> call, Response<ApiService.ApiResponse<List<FoodItem>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            List<FoodItem> foods = response.body().getData();
                            updateBottomSheetRecycler(foods, recyclerView, dialog);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<List<FoodItem>>> call, Throwable t) {}
                });
    }

    private void loadRecommendations(RecyclerView recyclerView, BottomSheetDialog dialog) {
        String dateStr = getDateOfIndex(selectedDayIndex);
        ApiClient.getService(this).getRecommendations(dateStr)
                .enqueue(new Callback<ApiService.ApiResponse<List<FoodItem>>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<List<FoodItem>>> call, Response<ApiService.ApiResponse<List<FoodItem>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            List<FoodItem> foods = response.body().getData();
                            updateBottomSheetRecycler(foods, recyclerView, dialog);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<List<FoodItem>>> call, Throwable t) {}
                });
    }

    private void updateBottomSheetRecycler(List<FoodItem> foods, RecyclerView recyclerView, BottomSheetDialog dialog) {
        FoodAdapter adapter = new FoodAdapter(foods, foodItem -> {
            String dateStr = getDateOfIndex(selectedDayIndex);
            String scheduledAt = dateStr + "T" + selectedTime + ":00.000Z";

            // Add meal to backend API
            ApiClient.getService(MealPlannerActivity.this)
                    .addMeal(new ApiService.AddMealRequest(foodItem.getId(), scheduledAt, 100))
                    .enqueue(new Callback<ApiService.ApiResponse<ApiService.MealResponse>>() {
                        @Override
                        public void onResponse(Call<ApiService.ApiResponse<ApiService.MealResponse>> call, Response<ApiService.ApiResponse<ApiService.MealResponse>> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(MealPlannerActivity.this, "Đã thêm món ăn!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                setupRecycler();
                            } else {
                                Toast.makeText(MealPlannerActivity.this, "Lỗi khi thêm món ăn", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiService.ApiResponse<ApiService.MealResponse>> call, Throwable t) {
                            Toast.makeText(MealPlannerActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        recyclerView.setAdapter(adapter);
    }
}