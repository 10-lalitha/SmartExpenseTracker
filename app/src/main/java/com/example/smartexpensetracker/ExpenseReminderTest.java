
package com.example.smartexpensetracker;

import android.content.Context;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class ExpenseReminderTest {

    public static void runTest(Context context) {

        OneTimeWorkRequest testRequest =
                new OneTimeWorkRequest.Builder(
                        ExpenseReminderWorker.class
                ).build();

        WorkManager.getInstance(
                context.getApplicationContext()
        ).enqueue(testRequest);
    }
}
