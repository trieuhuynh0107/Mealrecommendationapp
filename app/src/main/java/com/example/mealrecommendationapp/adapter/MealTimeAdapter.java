package com.example.mealrecommendationapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.model.FoodItem;
import com.example.mealrecommendationapp.model.TimeMeal;

import java.util.List;

public class MealTimeAdapter
        extends RecyclerView.Adapter<MealTimeAdapter.ViewHolder> {

    private List<TimeMeal> list;

    private OnAddClickListener listener;

    // CLICK INTERFACE

    public interface OnAddClickListener {

        void onAddClick(String time);
    }

    public MealTimeAdapter(
            List<TimeMeal> list,
            OnAddClickListener listener
    ) {

        this.list = list;

        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_meal_time,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        TimeMeal item = list.get(position);

        // TIME

        holder.txtTime.setText(
                item.getTime()
        );

        // FOOD NAME

        FoodItem foodItem =
                item.getFoodItem();

        if (foodItem != null) {

            holder.txtFoodName.setText(
                    foodItem.getName()
            );

        } else {

            holder.txtFoodName.setText("");
        }

        // ADD BUTTON

        holder.btnAdd.setOnClickListener(v -> {

            if (listener != null) {

                listener.onAddClick(
                        item.getTime()
                );
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtTime;

        TextView txtFoodName;

        TextView btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTime =
                    itemView.findViewById(R.id.txtTime);

            txtFoodName =
                    itemView.findViewById(R.id.txtFoodName);

            btnAdd =
                    itemView.findViewById(R.id.btnAdd);
        }
    }
}