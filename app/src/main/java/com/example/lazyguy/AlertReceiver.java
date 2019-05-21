package com.example.lazyguy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        int extra = intent.getIntExtra("type", 0);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(extra);
        //NotificationCompat.Builder nb2 = notificationHelper.getSummaryNotification();
        notificationHelper.getManager().notify(extra, nb.build());
        //notificationHelper.getManager().notify(extra, nb2.build());
        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        //notificationManager.notify(1, nb.build());
    }
}