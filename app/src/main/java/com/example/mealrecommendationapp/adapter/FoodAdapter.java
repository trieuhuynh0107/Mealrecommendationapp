package com.example.mealrecommendationapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.model.FoodItem;

import java.util.List;

public class FoodAdapter
        extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private List<FoodItem> list;

    private OnFoodClickListener listener;

    // CLICK INTERFACE

    public interface OnFoodClickListener {

        void onFoodClick(FoodItem foodItem);
    }

    // CONSTRUCTOR

    public FoodAdapter(
            List<FoodItem> list,
            OnFoodClickListener listener
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
                        R.layout.item_food,
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

        FoodItem item = list.get(position);

        holder.txtName.setText(
                item.getName()
        );

        if (item.getQuantityG() != null) {
            holder.txtCalories.setText(
                    (int) item.getCalories() + " kcal - " + item.getQuantityG() + "g"
            );
        } else {
            holder.txtCalories.setText(
                    (int) item.getCalories() + " kcal - 100g"
            );
        }

        holder.txtProtein.setText(
                item.getProtein() + "g"
        );

        holder.txtFats.setText(
                item.getFats() + "g"
        );

        holder.txtCarbs.setText(
                item.getCarbs() + "g"
        );

        // CLICK ITEM

        holder.itemView.setOnClickListener(v -> {

            if (listener != null) {

                listener.onFoodClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtCalories;

        TextView txtProtein;
        TextView txtFats;
        TextView txtCarbs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName =
                    itemView.findViewById(R.id.txtName);

            txtCalories =
                    itemView.findViewById(R.id.txtCalories);

            txtProtein =
                    itemView.findViewById(R.id.txtProtein);

            txtFats =
                    itemView.findViewById(R.id.txtFats);

            txtCarbs =
                    itemView.findViewById(R.id.txtCarbs);
        }
    }
}