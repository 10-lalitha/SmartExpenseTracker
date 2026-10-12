package com.example.smartexpensetracker.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartexpensetracker.data.repository.BudgetRepository;

public class BudgetViewModelFactory
        implements ViewModelProvider.Factory {

    private final BudgetRepository repository;

    public BudgetViewModelFactory(
            BudgetRepository repository) {

        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(
            @NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(
                BudgetViewModel.class)) {

            return (T) new BudgetViewModel(
                    repository
            );
        }

        throw new IllegalArgumentException(
                "Unknown ViewModel class"
        );
    }
}