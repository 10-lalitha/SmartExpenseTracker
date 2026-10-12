package com.example.smartexpensetracker.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartexpensetracker.data.repository.IncomeRepository;

public class IncomeViewModelFactory
        implements ViewModelProvider.Factory {

    private final IncomeRepository repository;

    public IncomeViewModelFactory(
            IncomeRepository repository) {

        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(
            @NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(
                IncomeViewModel.class)) {

            return (T) new IncomeViewModel(
                    repository
            );
        }

        throw new IllegalArgumentException(
                "Unknown ViewModel class"
        );
    }
}