package com.example.smartexpensetracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartexpensetracker.data.repository.IncomeRepository;
import com.example.smartexpensetracker.model.ExpenseDatabase;
import com.example.smartexpensetracker.model.Income;
import com.example.smartexpensetracker.model.IncomeDao;
import com.example.smartexpensetracker.viewmodel.IncomeViewModel;
import com.example.smartexpensetracker.viewmodel.IncomeViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IncomeActivity extends AppCompatActivity {

    private EditText etIncomeAmount;
    private Button btnSaveIncome;
    private Button btnDeleteIncome;
    private TextView tvIncomeStatus;

    private IncomeViewModel incomeViewModel;

    private Income currentIncome;

    private String currentMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_income);


        // --------------------------------
        // FIND VIEWS
        // --------------------------------

        etIncomeAmount =
                findViewById(R.id.etIncomeAmount);

        btnSaveIncome =
                findViewById(R.id.btnSaveIncome);

        btnDeleteIncome =
                findViewById(R.id.btnDeleteIncome);

        tvIncomeStatus =
                findViewById(R.id.tvIncomeStatus);


        // --------------------------------
        // CURRENT MONTH
        // --------------------------------

        currentMonth =
                new SimpleDateFormat(
                        "MMMM yyyy",
                        Locale.getDefault()
                ).format(new Date());


        // --------------------------------
        // ROOM DATABASE
        // --------------------------------

        ExpenseDatabase database =
                ExpenseDatabase.getInstance(
                        getApplicationContext()
                );

        IncomeDao incomeDao =
                database.incomeDao();


        // --------------------------------
        // REPOSITORY
        // --------------------------------

        IncomeRepository repository =
                new IncomeRepository(
                        incomeDao
                );


        // --------------------------------
        // VIEWMODEL FACTORY
        // --------------------------------

        IncomeViewModelFactory factory =
                new IncomeViewModelFactory(
                        repository
                );


        // --------------------------------
        // VIEWMODEL
        // --------------------------------

        incomeViewModel =
                new ViewModelProvider(
                        this,
                        factory
                ).get(IncomeViewModel.class);


        // --------------------------------
        // LOAD CURRENT MONTH INCOME
        // --------------------------------

        loadCurrentIncome();


        // --------------------------------
        // SAVE BUTTON
        // --------------------------------

        btnSaveIncome.setOnClickListener(
                v -> saveIncome()
        );


        // --------------------------------
        // DELETE BUTTON
        // --------------------------------

        btnDeleteIncome.setOnClickListener(
                v -> deleteIncome()
        );
    }


    // ========================================
    // LOAD CURRENT INCOME
    // ========================================

    private void loadCurrentIncome() {

        incomeViewModel.getIncomeByMonth(
                currentMonth,
                income -> {

                    runOnUiThread(() -> {

                        if (income != null) {

                            currentIncome = income;

                            tvIncomeStatus.setText(
                                    "Current Income: ₹" +
                                            String.format(
                                                    Locale.getDefault(),
                                                    "%.2f",
                                                    income.getAmount()
                                            )
                            );

                            etIncomeAmount.setText(
                                    String.valueOf(
                                            income.getAmount()
                                    )
                            );

                        } else {

                            currentIncome = null;

                            tvIncomeStatus.setText(
                                    "No income set"
                            );

                            etIncomeAmount.setText("");
                        }
                    });
                }
        );
    }


    // ========================================
    // SAVE INCOME
    // ========================================

    private void saveIncome() {

        String amountText =
                etIncomeAmount
                        .getText()
                        .toString()
                        .trim();


        // --------------------------------
        // VALIDATE EMPTY
        // --------------------------------

        if (amountText.isEmpty()) {

            etIncomeAmount.setError(
                    "Enter income amount"
            );

            etIncomeAmount.requestFocus();

            return;
        }


        // --------------------------------
        // CONVERT TO DOUBLE
        // --------------------------------

        double amount;

        try {

            amount =
                    Double.parseDouble(
                            amountText
                    );

        } catch (NumberFormatException e) {

            etIncomeAmount.setError(
                    "Enter a valid amount"
            );

            etIncomeAmount.requestFocus();

            return;
        }


        // --------------------------------
        // VALIDATE AMOUNT
        // --------------------------------

        if (amount <= 0) {

            etIncomeAmount.setError(
                    "Enter a valid amount"
            );

            etIncomeAmount.requestFocus();

            return;
        }


        // --------------------------------
        // CREATE INCOME
        // --------------------------------

        Income income =
                new Income(
                        amount,
                        currentMonth
                );


        // --------------------------------
        // SAVE TO ROOM
        // --------------------------------

        incomeViewModel.saveIncome(
                income
        );


        Toast.makeText(
                this,
                "Income saved successfully",
                Toast.LENGTH_SHORT
        ).show();


        // Reload after saving
        loadCurrentIncome();
    }


    // ========================================
    // DELETE INCOME
    // ========================================

    private void deleteIncome() {

        if (currentIncome == null) {

            Toast.makeText(
                    this,
                    "No income to delete",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        Income incomeToDelete =
                currentIncome;


        incomeViewModel.deleteIncome(
                incomeToDelete
        );


        currentIncome = null;

        etIncomeAmount.setText("");

        tvIncomeStatus.setText(
                "No income set"
        );


        Toast.makeText(
                this,
                "Income deleted successfully",
                Toast.LENGTH_SHORT
        ).show();
    }
}