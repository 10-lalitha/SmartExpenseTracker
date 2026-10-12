package com.example.smartexpensetracker.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                Expense.class,
                Budget.class,
                Income.class
        },
        version = 3,
        exportSchema = false
)
public abstract class ExpenseDatabase
        extends RoomDatabase {

    private static ExpenseDatabase instance;

    public abstract ExpenseDao expenseDao();

    public abstract BudgetDao budgetDao();

    public abstract IncomeDao incomeDao();


    public static synchronized ExpenseDatabase getInstance(
            Context context) {

        if (instance == null) {

            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ExpenseDatabase.class,
                            "expense_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}