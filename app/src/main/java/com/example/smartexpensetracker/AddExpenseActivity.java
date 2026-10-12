package com.example.smartexpensetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartexpensetracker.data.repository.ExpenseRepository;
import com.example.smartexpensetracker.model.ExpenseDao;
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel;
import com.example.smartexpensetracker.viewmodel.ExpenseViewModelFactory;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartexpensetracker.model.Expense;
import java.util.Calendar;
import com.example.smartexpensetracker.model.ExpenseDatabase;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etAmount;
    private Spinner spinnerCategory;
    private EditText etDescription;
    private EditText etDate;
    private Button btnSaveExpense;
    private ExpenseViewModel expenseViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_expense);
        ExpenseDatabase database =
                ExpenseDatabase.getInstance(
                        getApplicationContext()
                );

        ExpenseDao expenseDao =
                database.expenseDao();

        ExpenseRepository repository =
                new ExpenseRepository(expenseDao);

        ExpenseViewModelFactory factory =
                new ExpenseViewModelFactory(repository);

        expenseViewModel =
                new ViewModelProvider(
                        this,
                        factory
                ).get(ExpenseViewModel.class);

        // Find views
        etAmount = findViewById(R.id.etAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);

        // Categories
        String[] categories = {
                "Food",
                "Shopping",
                "Travel",
                "Bills",
                "Health",
                "Entertainment",
                "Education",
                "Other"
        };

        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        categoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCategory.setAdapter(categoryAdapter);

        // Date picker
        etDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            AddExpenseActivity.this,

                            (view, selectedYear, selectedMonth, selectedDay) -> {

                                String date =
                                        selectedDay + "/"
                                                + (selectedMonth + 1) + "/"
                                                + selectedYear;

                                etDate.setText(date);
                            },

                            year,
                            month,
                            day
                    );

            datePickerDialog.show();
        });

        // Save expense
        btnSaveExpense.setOnClickListener(v -> {

            String amountText =
                    etAmount.getText().toString().trim();

            String category =
                    spinnerCategory.getSelectedItem().toString();

            String description =
                    etDescription.getText().toString().trim();

            String date =
                    etDate.getText().toString().trim();

            // Validate amount
            if (amountText.isEmpty()) {
                etAmount.setError("Enter amount");
                etAmount.requestFocus();
                return;
            }

            // Validate description
            if (description.isEmpty()) {
                etDescription.setError("Enter description");
                etDescription.requestFocus();
                return;
            }

            // Validate date
            if (date.isEmpty()) {
                etDate.setError("Select date");
                etDate.requestFocus();
                return;
            }

            // Convert amount from String to double
            double amount = Double.parseDouble(amountText);

            // Create Expense object
            Expense expense = new Expense(
                    amount,
                    category,
                    description,
                    date
            );

            // Get Room database
            expenseViewModel.insertExpense(expense);

            Toast.makeText(
                    AddExpenseActivity.this,
                    "Expense saved successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });
    }
}