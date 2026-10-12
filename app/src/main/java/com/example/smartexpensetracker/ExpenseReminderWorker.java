
package com.example.smartexpensetracker;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ExpenseReminderWorker extends Worker {

    private static final String CHANNEL_ID =
            "expense_reminder_channel";

    public ExpenseReminderWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {

        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        createNotificationChannel();
        showReminderNotification();

        return Result.success();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "Expense Reminders",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );

            channel.setDescription(
                    "Reminders to record daily expenses"
            );

            NotificationManager manager =
                    (NotificationManager)
                            getApplicationContext()
                                    .getSystemService(
                                            Context.NOTIFICATION_SERVICE
                                    );

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void showReminderNotification() {

        Context context = getApplicationContext();

        if (Build.VERSION.SDK_INT >= 33 &&
                ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        CHANNEL_ID
                )
                        .setSmallIcon(
                                R.drawable.ic_launcher_foreground
                        )
                        .setContentTitle(
                                "Smart Expense Tracker"
                        )
                        .setContentText(
                                "Don't forget to record today's expenses."
                        )
                        .setPriority(
                                NotificationCompat.PRIORITY_DEFAULT
                        )
                        .setAutoCancel(true);

        NotificationManagerCompat.from(context)
                .notify(2001, builder.build());
    }
}
