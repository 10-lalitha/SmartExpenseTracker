package com.example.smartexpensetracker.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.smartexpensetracker.data.repository.BudgetRepository;
import com.example.smartexpensetracker.model.Budget;

public class BudgetViewModel extends ViewModel {

    private final BudgetRepository repository;

    public BudgetViewModel(BudgetRepository repository) {
        this.repository = repository;
    }

    public void saveBudget(Budget budget) {

        repository.saveBudget(budget);
    }

    public void getBudgetByMonth(
            String month,
            BudgetRepository.BudgetCallback callback) {

        repository.getBudgetByMonth(
                month,
                callback
        );
    }

    public void deleteBudget(Budget budget) {

        repository.deleteBudget(budget);
    }
}