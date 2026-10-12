package com.example.smartexpensetracker.data.repository;

import com.example.smartexpensetracker.model.Budget;
import com.example.smartexpensetracker.model.BudgetDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BudgetRepository {

    private final BudgetDao budgetDao;

    private final ExecutorService executorService =
            Executors.newSingleThreadExecutor();

    public BudgetRepository(BudgetDao budgetDao) {
        this.budgetDao = budgetDao;
    }

    public void saveBudget(Budget budget) {

        executorService.execute(() -> {

            Budget existingBudget =
                    budgetDao.getBudgetByMonth(
                            budget.getMonth()
                    );

            if (existingBudget == null) {

                budgetDao.insertBudget(budget);

            } else {

                budget.setId(
                        existingBudget.getId()
                );

                budgetDao.updateBudget(budget);
            }
        });
    }

    public void getBudgetByMonth(
            String month,
            BudgetCallback callback) {

        executorService.execute(() -> {

            Budget budget =
                    budgetDao.getBudgetByMonth(month);

            callback.onResult(budget);
        });
    }

    public void deleteBudget(
            Budget budget) {

        executorService.execute(() ->
                budgetDao.deleteBudget(budget)
        );
    }

    public interface BudgetCallback {

        void onResult(Budget budget);
    }
}