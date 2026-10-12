package com.example.smartexpensetracker.data.repository;

import com.example.smartexpensetracker.model.Income;
import com.example.smartexpensetracker.model.IncomeDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IncomeRepository {

    private final IncomeDao incomeDao;

    private final ExecutorService executorService =
            Executors.newSingleThreadExecutor();

    public IncomeRepository(IncomeDao incomeDao) {
        this.incomeDao = incomeDao;
    }


    // --------------------------------
    // SAVE / UPDATE INCOME
    // --------------------------------

    public void saveIncome(Income income) {

        executorService.execute(() -> {

            Income existingIncome =
                    incomeDao.getIncomeByMonth(
                            income.getMonth()
                    );

            if (existingIncome == null) {

                long id =
                        incomeDao.insertIncome(income);

                income.setId((int) id);

            } else {

                income.setId(
                        existingIncome.getId()
                );

                incomeDao.updateIncome(income);
            }
        });
    }


    // --------------------------------
    // GET INCOME BY MONTH
    // --------------------------------

    public void getIncomeByMonth(
            String month,
            IncomeCallback callback) {

        executorService.execute(() -> {

            Income income =
                    incomeDao.getIncomeByMonth(
                            month
                    );

            callback.onResult(income);
        });
    }


    // --------------------------------
    // DELETE INCOME
    // --------------------------------

    public void deleteIncome(Income income) {

        executorService.execute(() ->
                incomeDao.deleteIncome(income)
        );
    }


    // --------------------------------
    // CALLBACK
    // --------------------------------

    public interface IncomeCallback {

        void onResult(Income income);
    }
}