package com.example.smartexpensetracker.data.repository;

import androidx.lifecycle.LiveData;

import com.example.smartexpensetracker.api.ExpenseApi;
import com.example.smartexpensetracker.api.RetrofitClient;
import com.example.smartexpensetracker.model.CategoryExpense;
import com.example.smartexpensetracker.model.Expense;
import com.example.smartexpensetracker.model.ExpenseDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseRepository {

    private final ExpenseDao expenseDao;
    private final ExpenseApi expenseApi;

    private final ExecutorService executorService =
            Executors.newSingleThreadExecutor();

    public ExpenseRepository(ExpenseDao expenseDao) {

        this.expenseDao = expenseDao;

        expenseApi = RetrofitClient.getExpenseApi();
    }

    // --------------------------------
    // INSERT EXPENSE
    // --------------------------------

    public void insertExpense(Expense expense) {

        executorService.execute(() ->
                expenseDao.insertExpense(expense)
        );
    }

    // --------------------------------
    // UPDATE EXPENSE
    // --------------------------------

    public void updateExpense(Expense expense) {

        executorService.execute(() ->
                expenseDao.updateExpense(expense)
        );
    }

    // --------------------------------
    // DELETE EXPENSE
    // --------------------------------

    public void deleteExpense(Expense expense) {

        executorService.execute(() ->
                expenseDao.deleteExpense(expense)
        );
    }

    // --------------------------------
    // GET ALL EXPENSES
    // --------------------------------

    public LiveData<List<Expense>> getAllExpenses() {

        return expenseDao.getAllExpenses();
    }

    // --------------------------------
    // GET EXPENSE BY ID
    // --------------------------------

    public LiveData<Expense> getExpenseById(
            int expenseId) {

        return expenseDao.getExpenseById(expenseId);
    }

    // --------------------------------
    // CATEGORY EXPENSES
    // --------------------------------

    public LiveData<List<CategoryExpense>> getCategoryExpenses() {

        return expenseDao.getCategoryExpenses();
    }

    // --------------------------------
    // CATEGORY EXPENSES FOR MONTH
    // --------------------------------

    public LiveData<List<CategoryExpense>>
    getCategoryExpensesForMonth(
            String month,
            String year) {

        return expenseDao.getCategoryExpensesForMonth(
                month,
                year
        );
    }

    // --------------------------------
    // TOTAL EXPENSES FOR MONTH
    // --------------------------------

    public void getTotalExpensesForMonth(
            String month,
            String year,
            TotalExpenseCallback callback) {

        executorService.execute(() -> {

            Double total =
                    expenseDao.getTotalExpensesForMonth(
                            month,
                            year
                    );

            if (total == null) {
                total = 0.0;
            }

            callback.onResult(total);
        });
    }

    public interface TotalExpenseCallback {

        void onResult(double total);
    }

    // --------------------------------
    // SEARCH EXPENSES
    // --------------------------------

    public LiveData<List<Expense>> searchExpenses(
            String searchText) {

        return expenseDao.searchExpenses(searchText);
    }

    // --------------------------------
    // GET EXPENSES FROM API
    // --------------------------------

    public void getExpensesFromApi() {

        expenseApi.getExpenses().enqueue(
                new Callback<List<Expense>>() {

                    @Override
                    public void onResponse(
                            Call<List<Expense>> call,
                            Response<List<Expense>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            List<Expense> expenses =
                                    response.body();

                            executorService.execute(() -> {

                                for (Expense expense : expenses) {

                                    expenseDao.insertExpense(
                                            expense
                                    );
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Expense>> call,
                            Throwable t) {
                    }
                }
        );
    }

    // --------------------------------
    // CREATE EXPENSE USING API
    // --------------------------------

    public void createExpenseFromApi(
            Expense expense) {

        expenseApi.createExpense(expense).enqueue(
                new Callback<Expense>() {

                    @Override
                    public void onResponse(
                            Call<Expense> call,
                            Response<Expense> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            Expense createdExpense =
                                    response.body();

                            executorService.execute(() ->
                                    expenseDao.insertExpense(
                                            createdExpense
                                    )
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Expense> call,
                            Throwable t) {
                    }
                }
        );
    }

    // --------------------------------
    // UPDATE EXPENSE USING API
    // --------------------------------

    public void updateExpenseFromApi(
            int expenseId,
            Expense expense) {

        expenseApi.updateExpense(
                expenseId,
                expense
        ).enqueue(
                new Callback<Expense>() {

                    @Override
                    public void onResponse(
                            Call<Expense> call,
                            Response<Expense> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            Expense updatedExpense =
                                    response.body();

                            executorService.execute(() ->
                                    expenseDao.updateExpense(
                                            updatedExpense
                                    )
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Expense> call,
                            Throwable t) {
                    }
                }
        );
    }

    // --------------------------------
    // DELETE EXPENSE USING API
    // --------------------------------

    public void deleteExpenseFromApi(
            int expenseId) {

        expenseApi.deleteExpense(
                expenseId
        ).enqueue(
                new Callback<Void>() {

                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response) {

                        // API deletion completed.
                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t) {
                    }
                }
        );
    }
}