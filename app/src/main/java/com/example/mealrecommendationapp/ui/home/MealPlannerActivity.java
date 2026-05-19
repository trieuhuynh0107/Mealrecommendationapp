package com.example.mealrecommendationapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.adapter.FoodAdapter;
import com.example.mealrecommendationapp.adapter.MealTimeAdapter;
import com.example.mealrecommendationapp.data.FakeData;
import com.example.mealrecommendationapp.model.TimeMeal;
import com.example.mealrecommendationapp.ui.survey.SurveyCuisineActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MealPlannerActivity
        extends AppCompatActivity {

    private RecyclerView recyclerMealTime;

    private TextView btnHome;

    private TextView btnMenu;

    private TextView dayMon;
    private TextView dayTue;
    private TextView dayWed;
    private TextView dayThu;
    private TextView dayFri;
    private TextView daySat;
    private TextView daySun;

    private int selectedDayIndex;

    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meal_planner);

        initViews();

        Calendar calendar =
                Calendar.getInstance();

        int today =
                calendar.get(Calendar.DAY_OF_WEEK);

        if (today == Calendar.SUNDAY) {

            selectedDayIndex = 6;

        } else {

            selectedDayIndex = today - 2;
        }

        setupDays();

        setupRecycler();

        setupBottomNav();

        setupClicks();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupRecycler();
    }

    private void initViews() {

        recyclerMealTime =
                findViewById(R.id.recyclerMealTime);

        btnHome =
                findViewById(R.id.btnHome);

        btnMenu =
                findViewById(R.id.btnMenu);

        dayMon =
                findViewById(R.id.dayMon);

        dayTue =
                findViewById(R.id.dayTue);

        dayWed =
                findViewById(R.id.dayWed);

        dayThu =
                findViewById(R.id.dayThu);

        dayFri =
                findViewById(R.id.dayFri);

        daySat =
                findViewById(R.id.daySat);

        daySun =
                findViewById(R.id.daySun);
    }

    private void setupDays() {

        TextView[] days = {
                dayMon,
                dayTue,
                dayWed,
                dayThu,
                dayFri,
                daySat,
                daySun
        };

        selectDay(days[selectedDayIndex]);

        dayMon.setOnClickListener(v -> {

            selectedDayIndex = 0;

            selectDay(dayMon);

            setupRecycler();
        });

        dayTue.setOnClickListener(v -> {

            selectedDayIndex = 1;

            selectDay(dayTue);

            setupRecycler();
        });

        dayWed.setOnClickListener(v -> {

            selectedDayIndex = 2;

            selectDay(dayWed);

            setupRecycler();
        });

        dayThu.setOnClickListener(v -> {

            selectedDayIndex = 3;

            selectDay(dayThu);

            setupRecycler();
        });

        dayFri.setOnClickListener(v -> {

            selectedDayIndex = 4;

            selectDay(dayFri);

            setupRecycler();
        });

        daySat.setOnClickListener(v -> {

            selectedDayIndex = 5;

            selectDay(daySat);

            setupRecycler();
        });

        daySun.setOnClickListener(v -> {

            selectedDayIndex = 6;

            selectDay(daySun);

            setupRecycler();
        });
    }

    private void selectDay(TextView selected) {

        TextView[] days = {
                dayMon,
                dayTue,
                dayWed,
                dayThu,
                dayFri,
                daySat,
                daySun
        };

        for (TextView day : days) {

            day.setBackground(null);

            day.setTextColor(
                    getResources().getColor(
                            android.R.color.darker_gray
                    )
            );
        }

        selected.setBackgroundResource(
                R.drawable.bg_button_yellow
        );

        selected.setTextColor(
                getResources().getColor(
                        android.R.color.black
                )
        );
    }

    private void setupRecycler() {

        recyclerMealTime.setLayoutManager(
                new LinearLayoutManager(this)
        );

        List<TimeMeal> timeline =
                new ArrayList<>();

        for (int hour = 9; hour <= 21; hour++) {

            String time;

            if (hour < 10) {

                time = "0" + hour + ":00";

            } else {

                time = hour + ":00";
            }

            TimeMeal savedMeal =
                    FakeData.findMeal(
                            selectedDayIndex,
                            time
                    );

            if (savedMeal == null) {

                savedMeal =
                        new TimeMeal(
                                selectedDayIndex,
                                time
                        );
            }

            timeline.add(savedMeal);
        }

        MealTimeAdapter adapter =
                new MealTimeAdapter(

                        timeline,

                        time -> {

                            selectedTime = time;

                            showFoodBottomSheet();
                        }
                );

        recyclerMealTime.setAdapter(adapter);
    }

    private void setupBottomNav() {

        btnHome.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MealPlannerActivity.this,
                            HomeActivity.class
                    );

            startActivity(intent);

            finish();
        });
    }

    private void setupClicks() {

        btnMenu.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MealPlannerActivity.this,
                            ProfileActivity.class
                    );

            startActivity(intent);
        });
    }

    private void showFoodBottomSheet() {

        BottomSheetDialog dialog =
                new BottomSheetDialog(this);

        View view = getLayoutInflater()
                .inflate(
                        R.layout.bottom_sheet_food,
                        null
                );

        dialog.setContentView(view);

        RecyclerView recyclerSearchFood =
                view.findViewById(
                        R.id.recyclerSearchFood
                );

        Button btnRecommend =
                view.findViewById(
                        R.id.btnRecommend
                );

        EditText edtSearch =
                view.findViewById(
                        R.id.edtSearch
                );

        recyclerSearchFood.setLayoutManager(
                new LinearLayoutManager(this)
        );

        FoodAdapter adapter =
                new FoodAdapter(

                        FakeData.getRecommendedFoods(),

                        foodItem -> {

                            FakeData.addFoodToTime(
                                    selectedDayIndex,
                                    selectedTime,
                                    foodItem
                            );

                            dialog.dismiss();

                            setupRecycler();
                        }
                );

        recyclerSearchFood.setAdapter(adapter);

        btnRecommend.setOnClickListener(v -> {

            dialog.dismiss();

            Intent intent =
                    new Intent(
                            MealPlannerActivity.this,
                            SurveyCuisineActivity.class
                    );

            intent.putExtra(
                    "selected_time",
                    selectedTime
            );

            intent.putExtra(
                    "selected_day",
                    selectedDayIndex
            );

            startActivity(intent);
        });

        dialog.show();
    }
}