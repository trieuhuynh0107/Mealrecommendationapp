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

    private OnMealClickListener listener;

    // CLICK INTERFACE

    public interface OnMealClickListener {
        void onAddClick(String time);
        void onDeleteClick(TimeMeal item);
        void onEditClick(TimeMeal item);
    }

    public MealTimeAdapter(
            List<TimeMeal> list,
            OnMealClickListener listener
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
            String suffix = item.getQuantityG() != null ? " (" + item.getQuantityG() + "g)" : "";
            holder.txtFoodName.setText(
                    foodItem.getName() + suffix
            );
            holder.btnAdd.setText("x");
            holder.btnAdd.setBackgroundResource(R.drawable.bg_button_red);

            holder.btnAdd.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(item);
                }
            });

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(item);
                }
            });
        } else {
            holder.txtFoodName.setText("");
            holder.btnAdd.setText("+");
            holder.btnAdd.setBackgroundResource(R.drawable.bg_button_green);

            holder.btnAdd.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddClick(
                            item.getTime()
                    );
                }
            });

            holder.itemView.setOnClickListener(null);
        }
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