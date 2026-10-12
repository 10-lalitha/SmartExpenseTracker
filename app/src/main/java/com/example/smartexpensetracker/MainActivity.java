package com.example.smartexpensetracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartexpensetracker.data.repository.BudgetRepository;
import com.example.smartexpensetracker.data.repository.ExpenseRepository;
import com.example.smartexpensetracker.data.repository.IncomeRepository;
import com.example.smartexpensetracker.model.BudgetDao;
import com.example.smartexpensetracker.model.CategoryExpense;
import com.example.smartexpensetracker.model.Expense;
import com.example.smartexpensetracker.model.ExpenseDao;
import com.example.smartexpensetracker.model.ExpenseDatabase;
import com.example.smartexpensetracker.model.IncomeDao;
import com.example.smartexpensetracker.ui.ExpenseAdapter;
import com.example.smartexpensetracker.viewmodel.BudgetViewModel;
import com.example.smartexpensetracker.viewmodel.BudgetViewModelFactory;
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel;
import com.example.smartexpensetracker.viewmodel.ExpenseViewModelFactory;
import com.example.smartexpensetracker.viewmodel.IncomeViewModel;
import com.example.smartexpensetracker.viewmodel.IncomeViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;

    private Button btnAddExpense;
    private Button btnBudget;
    private Button btnAnalytics;
    private Button btnIncome;
    private Button btnLogout;

    private TextView tvBalance;
    private TextView tvIncome;
    private TextView tvExpenses;

    private TextView tvBudgetProgressStatus;
    private TextView tvBudgetSpent;
    private TextView tvBudgetPercentage;
    private TextView tvBudgetRemaining;
    private TextView tvBudgetInsight;

    private TextView tvMonthlySummaryTotal;
    private TextView tvHighestSpendingCategory;
    private TextView tvMonthlyComparison;

    private ProgressBar progressBudget;

    private EditText etSearchExpense;
    private RecyclerView recyclerViewExpenses;

    private ExpenseAdapter expenseAdapter;

    private ExpenseViewModel expenseViewModel;
    private IncomeViewModel incomeViewModel;
    private BudgetViewModel budgetViewModel;

    private FirebaseAuth mAuth;

    private double currentIncome = 0.0;
    private double currentExpenses = 0.0;
    private double currentBudget = 0.0;

    private boolean hasMonthlyBudget = false;

    private String currentMonth;
    private String currentMonthNumber;
    private String currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        initializeViews();
        setCurrentMonthValues();
        initializeViewModels();

        recyclerViewExpenses.setLayoutManager(
                new LinearLayoutManager(this)
        );

        requestNotificationPermission();

        ExpenseReminderScheduler.scheduleDailyReminder(
                getApplicationContext()
        );

        setupButtons();
        setupSearch();

        loadAllExpenses();
        loadIncome();
        loadMonthlyExpenses();
        loadBudget();
        loadMonthlySummary();
    }

    private void initializeViews() {

        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnBudget = findViewById(R.id.btnBudget);
        btnAnalytics = findViewById(R.id.btnAnalytics);
        btnIncome = findViewById(R.id.btnIncome);
        btnLogout = findViewById(R.id.btnLogout);

        tvBalance = findViewById(R.id.tvBalance);
        tvIncome = findViewById(R.id.tvIncome);
        tvExpenses = findViewById(R.id.tvExpenses);

        etSearchExpense = findViewById(R.id.etSearchExpense);
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);

        progressBudget = findViewById(R.id.progressBudget);
        tvBudgetProgressStatus =
                findViewById(R.id.tvBudgetProgressStatus);
        tvBudgetSpent = findViewById(R.id.tvBudgetSpent);
        tvBudgetPercentage = findViewById(R.id.tvBudgetPercentage);
        tvBudgetRemaining = findViewById(R.id.tvBudgetRemaining);
        tvBudgetInsight = findViewById(R.id.tvBudgetInsight);

        tvMonthlySummaryTotal =
                findViewById(R.id.tvMonthlySummaryTotal);
        tvHighestSpendingCategory =
                findViewById(R.id.tvHighestSpendingCategory);
        tvMonthlyComparison =
                findViewById(R.id.tvMonthlyComparison);
    }

    private void setCurrentMonthValues() {

        Date today = new Date();

        currentMonth = new SimpleDateFormat(
                "MMMM yyyy",
                Locale.getDefault()
        ).format(today);

        currentMonthNumber = new SimpleDateFormat(
                "MM",
                Locale.getDefault()
        ).format(today);

        currentYear = new SimpleDateFormat(
                "yyyy",
                Locale.getDefault()
        ).format(today);
    }

    private void initializeViewModels() {

        ExpenseDatabase database =
                ExpenseDatabase.getInstance(
                        getApplicationContext()
                );

        ExpenseDao expenseDao = database.expenseDao();

        ExpenseRepository expenseRepository =
                new ExpenseRepository(expenseDao);

        ExpenseViewModelFactory expenseFactory =
                new ExpenseViewModelFactory(expenseRepository);

        expenseViewModel = new ViewModelProvider(
                this,
                expenseFactory
        ).get(ExpenseViewModel.class);

        IncomeDao incomeDao = database.incomeDao();

        IncomeRepository incomeRepository =
                new IncomeRepository(incomeDao);

        IncomeViewModelFactory incomeFactory =
                new IncomeViewModelFactory(incomeRepository);

        incomeViewModel = new ViewModelProvider(
                this,
                incomeFactory
        ).get(IncomeViewModel.class);

        BudgetDao budgetDao = database.budgetDao();

        BudgetRepository budgetRepository =
                new BudgetRepository(budgetDao);

        BudgetViewModelFactory budgetFactory =
                new BudgetViewModelFactory(budgetRepository);

        budgetViewModel = new ViewModelProvider(
                this,
                budgetFactory
        ).get(BudgetViewModel.class);
    }

    private void setupButtons() {

        btnAddExpense.setOnClickListener(v ->
                startActivity(new Intent(
                        MainActivity.this,
                        AddExpenseActivity.class
                ))
        );

        btnBudget.setOnClickListener(v ->
                startActivity(new Intent(
                        MainActivity.this,
                        BudgetActivity.class
                ))
        );

        btnAnalytics.setOnClickListener(v ->
                startActivity(new Intent(
                        MainActivity.this,
                        AnalyticsActivity.class
                ))
        );

        btnIncome.setOnClickListener(v ->
                startActivity(new Intent(
                        MainActivity.this,
                        IncomeActivity.class
                ))
        );

        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void setupSearch() {

        etSearchExpense.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        String searchText = s.toString().trim();

                        if (searchText.isEmpty()) {
                            loadAllExpenses();
                        } else {
                            searchExpenses(searchText);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                }
        );
    }

    private void requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.POST_NOTIFICATIONS
                    },
                    NOTIFICATION_PERMISSION_REQUEST_CODE
            );
        }
    }

    private void loadAllExpenses() {

        expenseViewModel.getAllExpenses().observe(
                this,
                this::setupExpenseAdapter
        );
    }

    private void searchExpenses(String searchText) {

        expenseViewModel.searchExpenses(searchText).observe(
                this,
                this::setupExpenseAdapter
        );
    }

    private void setupExpenseAdapter(List<Expense> expenses) {

        if (expenses == null) {
            expenses = new ArrayList<>();
        }

        expenseAdapter = new ExpenseAdapter(
                expenses,
                expense -> {

                    Intent intent = new Intent(
                            MainActivity.this,
                            EditExpenseActivity.class
                    );

                    intent.putExtra("expenseId", expense.getId());
                    startActivity(intent);
                }
        );

        recyclerViewExpenses.setAdapter(expenseAdapter);
    }

    private void loadIncome() {

        incomeViewModel.getIncomeByMonth(
                currentMonth,
                income -> runOnUiThread(() -> {

                    currentIncome = income != null
                            ? income.getAmount()
                            : 0.0;

                    updateDashboard();
                })
        );
    }

    private void loadMonthlyExpenses() {

        expenseViewModel.getTotalExpensesForMonth(
                currentMonthNumber,
                currentYear,
                total -> runOnUiThread(() -> {

                    currentExpenses = total;

                    updateDashboard();
                    updateBudgetProgress();
                })
        );
    }

    private void updateDashboard() {

        tvIncome.setText(formatRupees(currentIncome));
        tvExpenses.setText(formatRupees(currentExpenses));

        tvBalance.setText(
                formatRupees(currentIncome - currentExpenses)
        );
    }

    private void loadBudget() {

        budgetViewModel.getBudgetByMonth(
                currentMonth,
                budget -> runOnUiThread(() -> {

                    if (budget == null) {
                        hasMonthlyBudget = false;
                        currentBudget = 0.0;
                    } else {
                        hasMonthlyBudget = true;
                        currentBudget = budget.getAmount();
                    }

                    updateBudgetProgress();
                })
        );
    }

    private void updateBudgetProgress() {

        progressBudget.setMax(100);

        if (!hasMonthlyBudget || currentBudget <= 0) {

            progressBudget.setProgress(0);

            tvBudgetProgressStatus.setText(
                    "No monthly budget set"
            );

            tvBudgetSpent.setText(
                    "Spent: " + formatRupees(currentExpenses)
            );

            tvBudgetPercentage.setText("0% used");

            tvBudgetRemaining.setText(
                    "Set a budget to track your spending"
            );

            tvBudgetInsight.setText(
                    "Create a monthly budget to monitor your expenses."
            );

            progressBudget.setProgressTintList(
                    ColorStateList.valueOf(Color.GRAY)
            );

            return;
        }

        int percentage = (int) (
                currentExpenses / currentBudget * 100
        );

        progressBudget.setProgress(
                Math.min(percentage, 100)
        );

        double remaining = currentBudget - currentExpenses;

        tvBudgetSpent.setText(
                "Spent: " + formatRupees(currentExpenses)
        );

        tvBudgetPercentage.setText(
                percentage + "% used"
        );

        if (remaining >= 0) {
            tvBudgetRemaining.setText(
                    "Remaining: " + formatRupees(remaining)
            );
        } else {
            tvBudgetRemaining.setText(
                    "Exceeded by: " + formatRupees(
                            Math.abs(remaining)
                    )
            );
        }

        if (percentage >= 100) {

            tvBudgetProgressStatus.setText(
                    "Monthly budget exceeded!"
            );

            tvBudgetInsight.setText(
                    "You have reached or exceeded your monthly budget. "
                            + "Try to limit additional spending."
            );

            progressBudget.setProgressTintList(
                    ColorStateList.valueOf(Color.RED)
            );

        } else if (percentage >= 80) {

            tvBudgetProgressStatus.setText(
                    "Budget warning"
            );

            tvBudgetInsight.setText(
                    "You have used most of your budget. "
                            + "Review your remaining expenses."
            );

            progressBudget.setProgressTintList(
                    ColorStateList.valueOf(Color.rgb(255, 152, 0))
            );

        } else {

            tvBudgetProgressStatus.setText(
                    "You are within your budget"
            );

            tvBudgetInsight.setText(
                    "Keep tracking your spending to stay within budget."
            );

            progressBudget.setProgressTintList(
                    ColorStateList.valueOf(Color.rgb(33, 150, 243))
            );
        }
    }

    private void loadMonthlySummary() {

        expenseViewModel.getCategoryExpensesForMonth(
                currentMonthNumber,
                currentYear
        ).observe(this, categoryExpenses -> {

            if (categoryExpenses == null
                    || categoryExpenses.isEmpty()) {

                tvMonthlySummaryTotal.setText(
                        "Monthly spending: ₹0.00"
                );

                tvHighestSpendingCategory.setText(
                        "Highest spending category: None"
                );

                loadPreviousMonthComparison(0.0);
                return;
            }

            double total = 0.0;
            double highestAmount = -1.0;
            String highestCategory = "None";

            for (CategoryExpense item : categoryExpenses) {

                double amount = item.getTotal();
                total += amount;

                if (amount > highestAmount) {
                    highestAmount = amount;
                    highestCategory = item.getCategory();
                }
            }

            tvMonthlySummaryTotal.setText(
                    "Monthly spending: " + formatRupees(total)
            );

            tvHighestSpendingCategory.setText(
                    "Highest spending category: "
                            + highestCategory
                            + " (" + formatRupees(highestAmount) + ")"
            );

            loadPreviousMonthComparison(total);
        });
    }

    private void loadPreviousMonthComparison(
            double currentMonthTotal) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);

        String previousMonthNumber = new SimpleDateFormat(
                "MM",
                Locale.getDefault()
        ).format(calendar.getTime());

        String previousYear = new SimpleDateFormat(
                "yyyy",
                Locale.getDefault()
        ).format(calendar.getTime());

        expenseViewModel.getTotalExpensesForMonth(
                previousMonthNumber,
                previousYear,
                previousTotal -> runOnUiThread(() -> {
                    if(previousTotal == 0) {

                        tvMonthlyComparison.setText(
                                "Previous month: No spending data available"
                        );

                        return;
                    }

                    double difference =
                            currentMonthTotal - previousTotal;

                    double percentageChange =
                            difference / previousTotal * 100;

                    if (difference > 0) {

                        tvMonthlyComparison.setText(
                                "Spending increased by "
                                        + formatRupees(difference)
                                        + " ("
                                        + String.format(
                                        Locale.getDefault(),
                                        "%.1f",
                                        percentageChange
                                )
                                        + "%) compared with last month."
                        );

                    } else if (difference < 0) {

                        tvMonthlyComparison.setText(
                                "Spending decreased by "
                                        + formatRupees(Math.abs(difference))
                                        + " ("
                                        + String.format(
                                        Locale.getDefault(),
                                        "%.1f",
                                        Math.abs(percentageChange)
                                )
                                        + "%) compared with last month."
                        );

                    } else {

                        tvMonthlyComparison.setText(
                                "Spending is the same as last month."
                        );
                    }
                })
        );
    }

    private String formatRupees(double amount) {

        return "₹" + String.format(
                Locale.getDefault(),
                "%.2f",
                amount
        );
    }

    private void logoutUser() {

        mAuth.signOut();

        Intent intent = new Intent(
                MainActivity.this,
                LoginActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (expenseViewModel != null
                && incomeViewModel != null
                && budgetViewModel != null) {

            loadAllExpenses();
            loadIncome();
            loadMonthlyExpenses();
            loadBudget();
            loadMonthlySummary();
        }
    }
}