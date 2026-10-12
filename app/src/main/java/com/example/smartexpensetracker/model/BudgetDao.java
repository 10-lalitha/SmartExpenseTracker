package com.example.smartexpensetracker.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface BudgetDao {

    @Insert
    void insertBudget(Budget budget);

    @Update
    void updateBudget(Budget budget);

    @Delete
    void deleteBudget(Budget budget);

    @Query("SELECT * FROM budgets WHERE month = :month LIMIT 1")
    Budget getBudgetByMonth(String month);
}