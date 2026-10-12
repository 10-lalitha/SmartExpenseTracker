package com.example.smartexpensetracker.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartexpensetracker.R;
import com.example.smartexpensetracker.model.CategoryExpense;

import java.util.List;
import java.util.Locale;

public class AnalyticsAdapter
        extends RecyclerView.Adapter<
        AnalyticsAdapter.AnalyticsViewHolder> {

    private final List<CategoryExpense> categoryExpenses;

    private final double totalExpense;

    public AnalyticsAdapter(
            List<CategoryExpense> categoryExpenses,
            double totalExpense) {

        this.categoryExpenses =
                categoryExpenses;

        this.totalExpense =
                totalExpense;
    }

    @NonNull
    @Override
    public AnalyticsViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_analytics,
                        parent,
                        false
                );

        return new AnalyticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull AnalyticsViewHolder holder,
            int position) {

        CategoryExpense item =
                categoryExpenses.get(position);

        holder.tvCategory.setText(
                item.getCategory()
        );

        holder.tvAmount.setText(
                "₹"
                        + String.format(
                        Locale.getDefault(),
                        "%.2f",
                        item.getTotal()
                )
        );

        double percentage = 0;

        if (totalExpense > 0) {

            percentage =
                    (item.getTotal()
                            / totalExpense)
                            * 100;
        }

        holder.tvPercentage.setText(
                String.format(
                        Locale.getDefault(),
                        "%.1f%%",
                        percentage
                )
        );
    }

    @Override
    public int getItemCount() {

        return categoryExpenses.size();
    }

    public static class AnalyticsViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvCategory;
        TextView tvAmount;
        TextView tvPercentage;

        public AnalyticsViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvCategory =
                    itemView.findViewById(
                            R.id.tvAnalyticsCategory
                    );

            tvAmount =
                    itemView.findViewById(
                            R.id.tvAnalyticsAmount
                    );

            tvPercentage =
                    itemView.findViewById(
                            R.id.tvAnalyticsPercentage
                    );
        }
    }
}