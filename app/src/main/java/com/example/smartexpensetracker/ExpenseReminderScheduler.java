
package com.example.smartexpensetracker;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class ExpenseReminderScheduler {

    private static final String WORK_NAME =
            "daily_expense_reminder";

    public static void scheduleDailyReminder(
            Context context) {

        Constraints constraints =
                new Constraints.Builder()
                        .setRequiredNetworkType(
                                NetworkType.NOT_REQUIRED
                        )
                        .build();

        PeriodicWorkRequest reminderRequest =
                new PeriodicWorkRequest.Builder(
                        ExpenseReminderWorker.class,
                        24,
                        TimeUnit.HOURS
                )
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(
                context.getApplicationContext()
        ).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                reminderRequest
        );
    }

    public static void cancelDailyReminder(
            Context context) {

        WorkManager.getInstance(
                context.getApplicationContext()
        ).cancelUniqueWork(WORK_NAME);
    }
}
