package com.example.smartexpensetracker.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {

    // --------------------------------
    // INSERT EXPENSE
    // --------------------------------

    @Insert
    void insertExpense(Expense expense);


    // --------------------------------
    // UPDATE EXPENSE
    // --------------------------------

    @Update
    void updateExpense(Expense expense);


    // --------------------------------
    // DELETE EXPENSE
    // --------------------------------

    @Delete
    void deleteExpense(Expense expense);


    // --------------------------------
    // GET ALL EXPENSES
    // --------------------------------

    @Query("SELECT * FROM expenses")
    LiveData<List<Expense>> getAllExpenses();


    // --------------------------------
    // GET EXPENSE BY ID
    // --------------------------------

    @Query(
            "SELECT * FROM expenses " +
                    "WHERE id = :expenseId"
    )
    LiveData<Expense> getExpenseById(
            int expenseId
    );


    // --------------------------------
    // CATEGORY-WISE EXPENSES
    // --------------------------------

    @Query(
            "SELECT category, SUM(amount) AS total " +
                    "FROM expenses " +
                    "GROUP BY category " +
                    "ORDER BY total DESC"
    )
    LiveData<List<CategoryExpense>> getCategoryExpenses();


    // --------------------------------
    // MONTHLY CATEGORY EXPENSES
    // --------------------------------

    @Query(
            "SELECT category, SUM(amount) AS total " +
                    "FROM expenses " +
                    "WHERE date LIKE '%/' || :month || '/' || :year " +
                    "GROUP BY category " +
                    "ORDER BY total DESC"
    )
    LiveData<List<CategoryExpense>>
    getCategoryExpensesForMonth(
            String month,
            String year
    );


    // --------------------------------
    // TOTAL EXPENSES FOR MONTH
    // --------------------------------

    @Query(
            "SELECT SUM(amount) " +
                    "FROM expenses " +
                    "WHERE date LIKE '%/' || :month || '/' || :year"
    )
    Double getTotalExpensesForMonth(
            String month,
            String year
    );


    // --------------------------------
    // SEARCH EXPENSES
    // --------------------------------
    // Searches both category and description

    @Query(
            "SELECT * FROM expenses " +
                    "WHERE category LIKE '%' || :searchText || '%' " +
                    "OR description LIKE '%' || :searchText || '%' " +
                    "ORDER BY id DESC"
    )
    LiveData<List<Expense>> searchExpenses(
            String searchText
    );
}