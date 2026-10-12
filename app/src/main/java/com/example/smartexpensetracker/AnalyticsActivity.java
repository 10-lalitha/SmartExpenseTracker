package com.example.smartexpensetracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartexpensetracker.data.repository.ExpenseRepository;
import com.example.smartexpensetracker.model.CategoryExpense;
import com.example.smartexpensetracker.model.ExpenseDao;
import com.example.smartexpensetracker.model.ExpenseDatabase;
import com.example.smartexpensetracker.ui.AnalyticsAdapter;
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel;
import com.example.smartexpensetracker.viewmodel.ExpenseViewModelFactory;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AnalyticsActivity extends AppCompatActivity {

    private TextView tvTotalAnalytics;
    private TextView tvNoAnalytics;
    private RecyclerView recyclerViewAnalytics;
    private PieChart pieChartExpenses;
    private Spinner spinnerMonth;

    private ExpenseViewModel expenseViewModel;

    private ArrayList<String> monthNames;
    private ArrayList<String> monthNumbers;
    private ArrayList<String> years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_analytics);

        // --------------------------------
        // FIND VIEWS
        // --------------------------------

        tvTotalAnalytics =
                findViewById(R.id.tvTotalAnalytics);

        tvNoAnalytics =
                findViewById(R.id.tvNoAnalytics);

        recyclerViewAnalytics =
                findViewById(R.id.recyclerViewAnalytics);

        pieChartExpenses =
                findViewById(R.id.pieChartExpenses);

        spinnerMonth =
                findViewById(R.id.spinnerMonth);

        // --------------------------------
        // RECYCLER VIEW
        // --------------------------------

        recyclerViewAnalytics.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // --------------------------------
        // ROOM DATABASE
        // --------------------------------

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

        // --------------------------------
        // VIEW MODEL
        // --------------------------------

        expenseViewModel =
                new ViewModelProvider(
                        this,
                        factory
                ).get(ExpenseViewModel.class);

        // --------------------------------
        // CREATE MONTH LIST
        // --------------------------------

        setupMonths();

        // --------------------------------
        // SPINNER SELECTION
        // --------------------------------

        spinnerMonth.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        String month =
                                monthNumbers.get(position);

                        String year =
                                years.get(position);

                        loadMonthlyAnalytics(
                                month,
                                year
                        );
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {
                    }
                }
        );
    }

    // ========================================
    // SETUP MONTHS
    // ========================================

    private void setupMonths() {

        monthNames = new ArrayList<>();
        monthNumbers = new ArrayList<>();
        years = new ArrayList<>();

        Calendar calendar =
                Calendar.getInstance();

        // Add the current month and
        // previous 11 months

        for (int i = 0; i < 12; i++) {

            Calendar monthCalendar =
                    (Calendar) calendar.clone();

            monthCalendar.add(
                    Calendar.MONTH,
                    -i
            );

            String monthName =
                    new SimpleDateFormat(
                            "MMMM yyyy",
                            Locale.getDefault()
                    ).format(
                            monthCalendar.getTime()
                    );

            String monthNumber =
                    new SimpleDateFormat(
                            "MM",
                            Locale.getDefault()
                    ).format(
                            monthCalendar.getTime()
                    );

            String year =
                    new SimpleDateFormat(
                            "yyyy",
                            Locale.getDefault()
                    ).format(
                            monthCalendar.getTime()
                    );

            monthNames.add(monthName);
            monthNumbers.add(monthNumber);
            years.add(year);
        }

        // Create Spinner adapter

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        monthNames
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerMonth.setAdapter(adapter);

        // Current month is first item
        spinnerMonth.setSelection(0);
    }

    // ========================================
    // LOAD MONTHLY ANALYTICS
    // ========================================

    private void loadMonthlyAnalytics(
            String month,
            String year) {

        expenseViewModel
                .getCategoryExpensesForMonth(
                        month,
                        year
                )
                .observe(
                        this,
                        categoryExpenses -> {

                            if (categoryExpenses == null
                                    || categoryExpenses.isEmpty()) {

                                showNoData();

                            } else {

                                showAnalytics(
                                        categoryExpenses
                                );
                            }
                        }
                );
    }

    // ========================================
    // SHOW ANALYTICS
    // ========================================

    private void showAnalytics(
            List<CategoryExpense> categoryExpenses) {

        tvNoAnalytics.setVisibility(
                View.GONE
        );

        recyclerViewAnalytics.setVisibility(
                View.VISIBLE
        );

        pieChartExpenses.setVisibility(
                View.VISIBLE
        );

        // Calculate total

        double total = 0;

        for (CategoryExpense item :
                categoryExpenses) {

            total += item.getTotal();
        }

        // Show total

        tvTotalAnalytics.setText(
                "₹" + String.format(
                        Locale.getDefault(),
                        "%.2f",
                        total
                )
        );

        // Category list

        AnalyticsAdapter adapter =
                new AnalyticsAdapter(
                        categoryExpenses,
                        total
                );

        recyclerViewAnalytics.setAdapter(
                adapter
        );

        // Pie chart

        createPieChart(
                categoryExpenses
        );
    }

    // ========================================
    // CREATE PIE CHART
    // ========================================

    private void createPieChart(
            List<CategoryExpense> categoryExpenses) {

        ArrayList<PieEntry> entries =
                new ArrayList<>();

        for (CategoryExpense item :
                categoryExpenses) {

            if (item.getTotal() > 0) {

                entries.add(
                        new PieEntry(
                                (float) item.getTotal(),
                                item.getCategory()
                        )
                );
            }
        }

        if (entries.isEmpty()) {

            pieChartExpenses.clear();

            pieChartExpenses.setNoDataText(
                    "No chart data available."
            );

            pieChartExpenses.invalidate();

            return;
        }

        PieDataSet dataSet =
                new PieDataSet(
                        entries,
                        "Expenses"
                );

        // Chart colors

        ArrayList<Integer> colors =
                new ArrayList<>();

        colors.add(Color.rgb(76, 175, 80));
        colors.add(Color.rgb(33, 150, 243));
        colors.add(Color.rgb(255, 152, 0));
        colors.add(Color.rgb(244, 67, 54));
        colors.add(Color.rgb(156, 39, 176));
        colors.add(Color.rgb(0, 150, 136));
        colors.add(Color.rgb(121, 85, 72));
        colors.add(Color.rgb(63, 81, 181));

        dataSet.setColors(colors);

        dataSet.setValueTextSize(14f);

        dataSet.setValueTextColor(
                Color.WHITE
        );

        PieData pieData =
                new PieData(dataSet);

        pieChartExpenses.setData(
                pieData
        );

        // Chart settings

        pieChartExpenses.setUsePercentValues(
                true
        );

        pieChartExpenses.getDescription()
                .setEnabled(false);

        pieChartExpenses.setCenterText(
                "Expenses"
        );

        pieChartExpenses.setCenterTextSize(
                18f
        );

        pieChartExpenses.setHoleRadius(
                45f
        );

        pieChartExpenses.setTransparentCircleRadius(
                50f
        );

        pieChartExpenses.setDrawEntryLabels(
                true
        );

        pieChartExpenses.setEntryLabelColor(
                Color.WHITE
        );

        pieChartExpenses.setEntryLabelTextSize(
                12f
        );

        pieChartExpenses.getLegend()
                .setEnabled(true);

        pieChartExpenses.notifyDataSetChanged();

        pieChartExpenses.invalidate();

        pieChartExpenses.animateY(
                1000
        );
    }

    // ========================================
    // NO DATA
    // ========================================

    private void showNoData() {

        tvTotalAnalytics.setText(
                "₹0.00"
        );

        tvNoAnalytics.setVisibility(
                View.VISIBLE
        );

        recyclerViewAnalytics.setVisibility(
                View.GONE
        );

        pieChartExpenses.setVisibility(
                View.GONE
        );
    }
}