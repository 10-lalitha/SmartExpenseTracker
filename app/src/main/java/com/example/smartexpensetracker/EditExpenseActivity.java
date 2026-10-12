package com.example.smartexpensetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartexpensetracker.data.repository.ExpenseRepository;
import com.example.smartexpensetracker.model.Expense;
import com.example.smartexpensetracker.model.ExpenseDatabase;
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel;
import com.example.smartexpensetracker.viewmodel.ExpenseViewModelFactory;

import java.util.Calendar;

public class EditExpenseActivity extends AppCompatActivity {

    private EditText etEditAmount;
    private Spinner spinnerEditCategory;
    private EditText etEditDescription;
    private EditText etEditDate;

    private Button btnUpdateExpense;
    private Button btnDeleteExpense;

    private ExpenseViewModel expenseViewModel;
    private Expense currentExpense;
    private int expenseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        etEditAmount = findViewById(R.id.etEditAmount);
        spinnerEditCategory = findViewById(R.id.spinnerEditCategory);
        etEditDescription = findViewById(R.id.etEditDescription);
        etEditDate = findViewById(R.id.etEditDate);
        btnUpdateExpense = findViewById(R.id.btnUpdateExpense);
        btnDeleteExpense = findViewById(R.id.btnDeleteExpense);

        ExpenseDatabase database =
                ExpenseDatabase.getInstance(getApplicationContext());

        ExpenseRepository repository =
                new ExpenseRepository(database.expenseDao());

        ExpenseViewModelFactory factory =
                new ExpenseViewModelFactory(repository);

        expenseViewModel = new ViewModelProvider(
                this, factory
        ).get(ExpenseViewModel.class);

        // Read the ID passed by MainActivity.
        expenseId = getIntent().getIntExtra("expenseId", -1);

        // Support the alternative key too.
        if (expenseId == -1) {
            expenseId = getIntent().getIntExtra("expense_id", -1);
        }

        if (expenseId == -1) {
            Toast.makeText(
                    this,
                    "Expense ID missing. Please reopen the expense.",
                    Toast.LENGTH_LONG
            ).show();
            finish();
            return;
        }

        String[] categories = {
                "Food", "Shopping", "Travel", "Bills",
                "Health", "Entertainment", "Education", "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerEditCategory.setAdapter(adapter);

        // Load the selected expense.
        expenseViewModel.getExpenseById(expenseId)
                .observe(this, expense -> {
                    if (expense == null) {
                        currentExpense = null;
                        return;
                    }

                    currentExpense = expense;

                    etEditAmount.setText(
                            String.valueOf(expense.getAmount())
                    );

                    etEditDescription.setText(
                            expense.getDescription()
                    );

                    etEditDate.setText(expense.getDate());

                    for (int i = 0; i < categories.length; i++) {
                        if (categories[i].equals(expense.getCategory())) {
                            spinnerEditCategory.setSelection(i);
                            break;
                        }
                    }
                });

        etEditDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            new DatePickerDialog(
                    EditExpenseActivity.this,
                    (view, year, month, day) -> {
                        String date = day + "/" + (month + 1) + "/" + year;
                        etEditDate.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        btnUpdateExpense.setOnClickListener(v -> updateExpense());

        btnDeleteExpense.setOnClickListener(v -> deleteExpense());
    }

    private void updateExpense() {
        if (currentExpense == null) {
            Toast.makeText(
                    this,
                    "Expense is not loaded yet.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String amountText = etEditAmount.getText().toString().trim();
        String description = etEditDescription.getText().toString().trim();
        String date = etEditDate.getText().toString().trim();

        if (amountText.isEmpty()) {
            etEditAmount.setError("Enter amount");
            etEditAmount.requestFocus();
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            etEditAmount.setError("Enter a valid amount");
            etEditAmount.requestFocus();
            return;
        }

        if (amount <= 0) {
            etEditAmount.setError("Amount must be greater than zero");
            etEditAmount.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            etEditDescription.setError("Enter description");
            etEditDescription.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            etEditDate.setError("Select date");
            etEditDate.requestFocus();
            return;
        }

        String category = spinnerEditCategory.getSelectedItem().toString();

        Expense updatedExpense = new Expense(
                amount, category, description, date
        );

        updatedExpense.setId(expenseId);
        expenseViewModel.updateExpense(updatedExpense);

        Toast.makeText(
                this,
                "Expense updated successfully",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }

    private void deleteExpense() {
        if (currentExpense == null) {
            Toast.makeText(
                    this,
                    "Expense is not loaded yet. Try again.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        btnDeleteExpense.setEnabled(false);

        expenseViewModel.deleteExpense(currentExpense);

        Toast.makeText(
                this,
                "Expense deleted",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}