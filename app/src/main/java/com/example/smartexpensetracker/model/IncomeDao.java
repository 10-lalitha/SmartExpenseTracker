package com.example.smartexpensetracker.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface IncomeDao {

    @Insert
    long insertIncome(Income income);

    @Update
    void updateIncome(Income income);

    @Delete
    void deleteIncome(Income income);

    @Query(
            "SELECT * FROM incomes " +
                    "WHERE month = :month " +
                    "LIMIT 1"
    )
    Income getIncomeByMonth(String month);
}