package com.example.smartexpensetracker.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.smartexpensetracker.data.repository.IncomeRepository;
import com.example.smartexpensetracker.model.Income;

public class IncomeViewModel extends ViewModel {

    private final IncomeRepository repository;

    public IncomeViewModel(
            IncomeRepository repository) {

        this.repository = repository;
    }


    // --------------------------------
    // SAVE / UPDATE INCOME
    // --------------------------------

    public void saveIncome(Income income) {

        repository.saveIncome(income);
    }


    // --------------------------------
    // GET INCOME BY MONTH
    // --------------------------------

    public void getIncomeByMonth(
            String month,
            IncomeRepository.IncomeCallback callback) {

        repository.getIncomeByMonth(
                month,
                callback
        );
    }


    // --------------------------------
    // DELETE INCOME
    // --------------------------------

    public void deleteIncome(Income income) {

        repository.deleteIncome(income);
    }
}