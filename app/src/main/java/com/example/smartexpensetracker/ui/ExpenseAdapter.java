package com.example.smartexpensetracker.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartexpensetracker.R;
import com.example.smartexpensetracker.model.Expense;

import java.util.List;

public class ExpenseAdapter
        extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private OnExpenseClickListener listener;

    // Click listener interface
    public interface OnExpenseClickListener {
        void onExpenseClick(Expense expense);
    }

    // Constructor
    public ExpenseAdapter(
            List<Expense> expenseList,
            OnExpenseClickListener listener) {

        this.expenseList = expenseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_expense,
                        parent,
                        false
                );

        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ExpenseViewHolder holder,
            int position) {

        Expense expense = expenseList.get(position);

        // Display category
        holder.tvExpenseCategory.setText(
                expense.getCategory()
        );

        // Display description
        holder.tvExpenseDescription.setText(
                expense.getDescription()
        );

        // Display amount
        holder.tvExpenseAmount.setText(
                "₹" + expense.getAmount()
        );

        // Display date
        holder.tvExpenseDate.setText(
                expense.getDate()
        );

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            listener.onExpenseClick(expense);
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // ViewHolder
    public static class ExpenseViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvExpenseCategory;
        TextView tvExpenseDescription;
        TextView tvExpenseAmount;
        TextView tvExpenseDate;

        public ExpenseViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvExpenseCategory =
                    itemView.findViewById(
                            R.id.tvExpenseCategory
                    );

            tvExpenseDescription =
                    itemView.findViewById(
                            R.id.tvExpenseDescription
                    );

            tvExpenseAmount =
                    itemView.findViewById(
                            R.id.tvExpenseAmount
                    );

            tvExpenseDate =
                    itemView.findViewById(
                            R.id.tvExpenseDate
                    );
        }
    }
}