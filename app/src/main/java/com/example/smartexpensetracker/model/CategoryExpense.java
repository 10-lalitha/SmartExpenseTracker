package com.example.smartexpensetracker.model;

public class CategoryExpense {

    private String category;
    private double total;

    public CategoryExpense(
            String category,
            double total) {

        this.category = category;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public double getTotal() {
        return total;
    }
}