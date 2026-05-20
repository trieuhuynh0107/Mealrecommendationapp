package com.example.mealrecommendationapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.adapter.FoodAdapter;
import com.example.mealrecommendationapp.data.network.ApiClient;
import com.example.mealrecommendationapp.data.network.ApiService;
import com.example.mealrecommendationapp.model.FoodItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationActivity extends AppCompatActivity {

    private RecyclerView recyclerRecommended;

    private String selectedTime;
    private int selectedDayIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        selectedTime = getIntent().getStringExtra("selected_time");
        selectedDayIndex = getIntent().getIntExtra("selected_day", 0);

        initViews();
        setupRecycler();
    }

    private void initViews() {
        recyclerRecommended = findViewById(R.id.recyclerRecommended);
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

    private void setupRecycler() {
        recyclerRecommended.setLayoutManager(new LinearLayoutManager(this));

        String dateStr = getDateOfIndex(selectedDayIndex);

        // Fetch dynamic recommended foods from backend API
        ApiClient.getService(this).getRecommendations(dateStr)
                .enqueue(new Callback<ApiService.ApiResponse<List<FoodItem>>>() {
                    @Override
                    public void onResponse(Call<ApiService.ApiResponse<List<FoodItem>>> call, Response<ApiService.ApiResponse<List<FoodItem>>> response) {
                        List<FoodItem> foods = new ArrayList<>();
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            foods = response.body().getData();
                        }
                        
                        FoodAdapter adapter = new FoodAdapter(foods, foodItem -> {
                            String scheduledAt = dateStr + "T" + selectedTime + ":00.000Z";

                            // Add meal to backend API
                            ApiClient.getService(RecommendationActivity.this)
                                    .addMeal(new ApiService.AddMealRequest(foodItem.getId(), scheduledAt, 100))
                                    .enqueue(new Callback<ApiService.ApiResponse<ApiService.MealResponse>>() {
                                        @Override
                                        public void onResponse(Call<ApiService.ApiResponse<ApiService.MealResponse>> call, Response<ApiService.ApiResponse<ApiService.MealResponse>> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(RecommendationActivity.this, "Đã thêm món ăn!", Toast.LENGTH_SHORT).show();
                                                
                                                Intent intent = new Intent(RecommendationActivity.this, MealPlannerActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(RecommendationActivity.this, "Lỗi khi thêm món ăn vào lịch", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ApiService.ApiResponse<ApiService.MealResponse>> call, Throwable t) {
                                            Toast.makeText(RecommendationActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        });
                        recyclerRecommended.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<ApiService.ApiResponse<List<FoodItem>>> call, Throwable t) {
                        Toast.makeText(RecommendationActivity.this, "Lỗi tải gợi ý: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}