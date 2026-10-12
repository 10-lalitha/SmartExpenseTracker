package com.example.smartexpensetracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartexpensetracker.data.repository.BudgetRepository;
import com.example.smartexpensetracker.model.Budget;
import com.example.smartexpensetracker.model.BudgetDao;
import com.example.smartexpensetracker.model.ExpenseDatabase;
import com.example.smartexpensetracker.viewmodel.BudgetViewModel;
import com.example.smartexpensetracker.viewmodel.BudgetViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BudgetActivity extends AppCompatActivity {

    private EditText etBudgetAmount;
    private Button btnSaveBudget;
    private TextView tvBudgetStatus;

    private BudgetViewModel budgetViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_budget);

        // Connect XML views
        etBudgetAmount =
                findViewById(R.id.etBudgetAmount);

        btnSaveBudget =
                findViewById(R.id.btnSaveBudget);

        tvBudgetStatus =
                findViewById(R.id.tvBudgetStatus);

        // Get Room database
        ExpenseDatabase database =
                ExpenseDatabase.getInstance(
                        getApplicationContext()
                );

        // Get Budget DAO
        BudgetDao budgetDao =
                database.budgetDao();

        // Create repository
        BudgetRepository repository =
                new BudgetRepository(budgetDao);

        // Create ViewModel factory
        BudgetViewModelFactory factory =
                new BudgetViewModelFactory(repository);

        // Create ViewModel
        budgetViewModel =
                new ViewModelProvider(
                        this,
                        factory
                ).get(BudgetViewModel.class);

        // Get current month
        String currentMonth =
                new SimpleDateFormat(
                        "MMMM yyyy",
                        Locale.getDefault()
                ).format(new Date());

        // Load existing budget
        budgetViewModel.getBudgetByMonth(
                currentMonth,
                budget -> {

                    runOnUiThread(() -> {

                        if (budget != null) {

                            tvBudgetStatus.setText(
                                    "Current Budget: ₹"
                                            + String.format(
                                            Locale.getDefault(),
                                            "%.2f",
                                            budget.getAmount()
                                    )
                            );

                            etBudgetAmount.setText(
                                    String.valueOf(
                                            budget.getAmount()
                                    )
                            );

                        } else {

                            tvBudgetStatus.setText(
                                    "No budget set"
                            );
                        }
                    });
                }
        );

        // Save budget
        btnSaveBudget.setOnClickListener(v -> {

            String amountText =
                    etBudgetAmount
                            .getText()
                            .toString()
                            .trim();

            // Empty validation
            if (amountText.isEmpty()) {

                etBudgetAmount.setError(
                        "Enter budget amount"
                );

                etBudgetAmount.requestFocus();

                return;
            }

            double amount;

            try {

                amount =
                        Double.parseDouble(
                                amountText
                        );

            } catch (NumberFormatException e) {

                etBudgetAmount.setError(
                        "Enter a valid amount"
                );

                etBudgetAmount.requestFocus();

                return;
            }

            // Positive amount validation
            if (amount <= 0) {

                etBudgetAmount.setError(
                        "Enter a valid amount"
                );

                etBudgetAmount.requestFocus();

                return;
            }

            // Create Budget object
            Budget budget =
                    new Budget(
                            amount,
                            currentMonth
                    );

            // Save to Room
            budgetViewModel.saveBudget(
                    budget
            );

            // Update UI
            tvBudgetStatus.setText(
                    "Current Budget: ₹"
                            + String.format(
                            Locale.getDefault(),
                            "%.2f",
                            amount
                    )
            );

            Toast.makeText(
                    BudgetActivity.this,
                    "Budget saved successfully",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
}