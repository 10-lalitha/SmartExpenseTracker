package com.example.smartexpensetracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expense {

    // Unique ID for every expense
    @PrimaryKey(autoGenerate = true)
    private int id;

    private double amount;
    private String category;
    private String description;
    private String date;

    // Constructor
    public Expense(double amount,
                   String category,
                   String description,
                   String date) {

        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Setter for ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter for amount
    public double getAmount() {
        return amount;
    }

    // Getter for category
    public String getCategory() {
        return category;
    }

    // Getter for description
    public String getDescription() {
        return description;
    }

    // Getter for date
    public String getDate() {
        return date;
    }
}