package com.example.smartexpensetracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "incomes")
public class Income {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private double amount;

    private String month;

    public Income(double amount, String month) {
        this.amount = amount;
        this.month = month;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public String getMonth() {
        return month;
    }
}