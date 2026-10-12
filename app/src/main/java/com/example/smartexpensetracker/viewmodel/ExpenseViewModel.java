package com.example.smartexpensetracker.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartexpensetracker.data.repository.ExpenseRepository;
import com.example.smartexpensetracker.model.CategoryExpense;
import com.example.smartexpensetracker.model.Expense;

import java.util.List;

public class ExpenseViewModel extends ViewModel {

    private final ExpenseRepository repository;

    public ExpenseViewModel(
            ExpenseRepository repository) {

        this.repository = repository;
    }

    // --------------------------------
    // INSERT EXPENSE
    // --------------------------------

    public void insertExpense(Expense expense) {

        repository.insertExpense(expense);
    }

    // --------------------------------
    // UPDATE EXPENSE
    // --------------------------------

    public void updateExpense(Expense expense) {

        repository.updateExpense(expense);
    }

    // --------------------------------
    // DELETE EXPENSE
    // --------------------------------

    public void deleteExpense(Expense expense) {

        repository.deleteExpense(expense);
    }

    // --------------------------------
    // GET ALL EXPENSES
    // --------------------------------

    public LiveData<List<Expense>> getAllExpenses() {

        return repository.getAllExpenses();
    }

    // --------------------------------
    // GET EXPENSE BY ID
    // --------------------------------

    public LiveData<Expense> getExpenseById(
            int expenseId) {

        return repository.getExpenseById(
                expenseId
        );
    }

    // --------------------------------
    // CATEGORY EXPENSES
    // --------------------------------

    public LiveData<List<CategoryExpense>>
    getCategoryExpenses() {

        return repository.getCategoryExpenses();
    }

    // --------------------------------
    // CATEGORY EXPENSES FOR MONTH
    // --------------------------------

    public LiveData<List<CategoryExpense>>
    getCategoryExpensesForMonth(
            String month,
            String year) {

        return repository.getCategoryExpensesForMonth(
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
            ExpenseRepository.TotalExpenseCallback callback) {

        repository.getTotalExpensesForMonth(
                month,
                year,
                callback
        );
    }

    // --------------------------------
    // SEARCH EXPENSES
    // --------------------------------

    public LiveData<List<Expense>> searchExpenses(
            String searchText) {

        return repository.searchExpenses(
                searchText
        );
    }

    // --------------------------------
    // API
    // --------------------------------

    public void getExpensesFromApi() {

        repository.getExpensesFromApi();
    }

    public void createExpenseFromApi(
            Expense expense) {

        repository.createExpenseFromApi(
                expense
        );
    }

    public void updateExpenseFromApi(
            int expenseId,
            Expense expense) {

        repository.updateExpenseFromApi(
                expenseId,
                expense
        );
    }

    public void deleteExpenseFromApi(
            int expenseId) {

        repository.deleteExpenseFromApi(
                expenseId
        );
    }
}