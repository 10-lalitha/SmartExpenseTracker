package com.example.smartexpensetracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService
        extends FirebaseMessagingService {

    private static final String TAG =
            "MyFirebaseMessagingService";

    private static final String CHANNEL_ID =
            "expense_tracker_notifications";

    @Override
    public void onNewToken(@NonNull String token) {

        super.onNewToken(token);

        Log.d(
                TAG,
                "FCM Token: " + token
        );
    }

    @Override
    public void onMessageReceived(
            @NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        Log.d(
                TAG,
                "Message received from: " +
                        remoteMessage.getFrom()
        );

        String title = "Smart Expense Tracker";
        String body = "You have a new notification.";

        if (remoteMessage.getNotification() != null) {

            if (remoteMessage
                    .getNotification()
                    .getTitle() != null) {

                title =
                        remoteMessage
                                .getNotification()
                                .getTitle();
            }

            if (remoteMessage
                    .getNotification()
                    .getBody() != null) {

                body =
                        remoteMessage
                                .getNotification()
                                .getBody();
            }
        }

        showNotification(title, body);
    }

    private void showNotification(
            String title,
            String body) {

        createNotificationChannel();

        Intent intent =
                new Intent(
                        this,
                        MainActivity.class
                );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                                PendingIntent.FLAG_IMMUTABLE
                );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        this,
                        CHANNEL_ID
                )
                        .setSmallIcon(
                                R.drawable.ic_launcher_foreground
                        )
                        .setContentTitle(title)
                        .setContentText(body)
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(body)
                        )
                        .setPriority(
                                NotificationCompat.PRIORITY_HIGH
                        )
                        .setAutoCancel(true)
                        .setContentIntent(
                                pendingIntent
                        );

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT < 33 ||
                androidx.core.content.ContextCompat
                        .checkSelfPermission(
                                this,
                                android.Manifest.permission.POST_NOTIFICATIONS
                        ) ==
                        android.content.pm.PackageManager.PERMISSION_GRANTED) {

            notificationManager.notify(
                    1001,
                    builder.build()
            );
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "Expense Tracker Notifications",
                            NotificationManager.IMPORTANCE_HIGH
                    );

            channel.setDescription(
                    "Notifications from Smart Expense Tracker"
            );

            NotificationManager notificationManager =
                    getSystemService(
                            NotificationManager.class
                    );

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(
                        channel
                );
            }
        }
    }
}