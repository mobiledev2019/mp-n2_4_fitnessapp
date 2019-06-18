package com.example.lazyguy;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.lazyguy.activity.ExcerciseActivity;
import com.example.lazyguy.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    final static String GROUP_KEY_EMAILS = "group_key_emails";
    String GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL";

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(int type) {
        // When notification is tapped, call MainActivity.
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, mainIntent, 0);

        SharedPreferences pre = getSharedPreferences("SavingPractice", MODE_PRIVATE);
        String explorer = pre.getString("explorer", "");
        String program = pre.getString("program", "");
        Intent exIntent = new Intent(getBaseContext(), ExcerciseActivity.class);
        exIntent.putExtra("explorer", explorer);
        exIntent.putExtra("program", program);
        PendingIntent contentIntent2 = PendingIntent.getActivity(getApplicationContext(), 0, exIntent, 0);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String name = "";
        if (mAuth.getCurrentUser() == null) {
            name = "Stranger";
        } else {
            name = mAuth.getCurrentUser().getDisplayName();
        }
        if (type == 1) {
            return new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle("Yum Yum")
                    .setContentText("Don't forget your breakfast " + name + " :)))")
                    .setSmallIcon(R.drawable.naruto)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setGroup(GROUP_KEY_WORK_EMAIL)
                    .setDefaults(Notification.DEFAULT_ALL);
        }
        if (type == 2) {
            return new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle("Yum Yum")
                    .setContentText("Don't forget your lunch " + name + " :)))")
                    .setSmallIcon(R.drawable.naruto)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setGroup(GROUP_KEY_WORK_EMAIL)
                    .setDefaults(Notification.DEFAULT_ALL);
        }
        if (type == 3) {
            return new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle("Yum Yum")
                    .setContentText("Don't forget your dinner " + name + " :)))")
                    .setSmallIcon(R.drawable.naruto)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setGroup(GROUP_KEY_WORK_EMAIL)
                    .setDefaults(Notification.DEFAULT_ALL);
        }
        if (type == 10) {
            return new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle("Hey Yo")
                    .setContentText("Your excercise is waiting for you, " + name + " :)))")
                    .setSmallIcon(R.drawable.naruto)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent2)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setGroup(GROUP_KEY_WORK_EMAIL)
                    .setDefaults(Notification.DEFAULT_ALL);
        }
        if (type > 4 && type < 30 && type != 10) {
            return new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle("Uc Uc Uc")
                    .setContentText("Don't forget to drink " + name + " :)))")
                    .setSmallIcon(R.drawable.naruto)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setGroup(GROUP_KEY_WORK_EMAIL)
                    .setDefaults(Notification.DEFAULT_ALL);
        }
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Yum Yum")
                .setContentText("Don't forget your remind " + name + " :)))")
                .setSmallIcon(R.drawable.naruto)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .setDefaults(Notification.DEFAULT_ALL);
    }

//    public NotificationCompat.Builder getSummaryNotification() {
//        // When notification is tapped, call MainActivity.
//        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, mainIntent, 0);
//
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        String name = "";
//        if (mAuth.getCurrentUser() == null) {
//            name = "Stranger";
//        } else {
//            name = mAuth.getCurrentUser().getDisplayName();
//        }
//
//        return new NotificationCompat.Builder(getApplicationContext(), channelID)
//                .setSmallIcon(R.drawable.naruto)
//                .setStyle(new NotificationCompat.InboxStyle()
//                    .addLine("title   message 1")
//                    .addLine("title   message 2")
//                    .setBigContentTitle("2 new message")
//                    .setSummaryText("nqc@gmail.com"))
//                .setPriority(Notification.PRIORITY_MAX)
//                .setGroup(GROUP_KEY_WORK_EMAIL)
//                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
//                .setGroupSummary(true)
//                .setDefaults(Notification.DEFAULT_ALL);
//    }
}