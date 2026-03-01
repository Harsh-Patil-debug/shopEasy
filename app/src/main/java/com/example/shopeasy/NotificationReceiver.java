package com.example.shopeasy;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import java.text.DateFormat;
import java.util.Date;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String channelId = "offers_channel";
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // --- 1. Save to Database (CO4 - Persistence) ---
        String title = "Limited Time Offer! 🔥";
        String message = "Exclusive Weekend Sale! Get massive discounts on S23 and iPhone 14. Code: EASY20";
        String currentTime = DateFormat.getTimeInstance().format(new Date());

        // Save to Room - This triggers the LiveData observer in MainActivity to update the badge
        NotificationEntity newNotif = new NotificationEntity(title, message, currentTime);
        AppDatabase.getInstance(context).notificationDao().insert(newNotif);

        // --- 2. Create Channel (Android 8.0+) ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "ShopEasy Offers",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        // --- 3. Click Intent ---
        Intent clickIntent = new Intent(context, MainActivity.class);
        clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // --- 4. Build and Send Notification ---
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_menu_home)
                .setContentTitle(title)
                .setContentText("Grab your iPhone 14 at 20% off. Use: EASY20")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message + "\n\nTap to Shop Now!"))
                .setColor(android.graphics.Color.parseColor("#FFD700"));

        manager.notify((int) System.currentTimeMillis(), builder.build());

        // --- 5. NEW: Self-Scheduling Loop (The 30-Second Fix) ---
        // This ensures the next notification triggers even on modern Android versions
        scheduleNextNotification(context);
    }

    private void scheduleNextNotification(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent nextIntent = new Intent(context, NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            long triggerTime = System.currentTimeMillis() + (30 * 1000); // 30 Seconds

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Use setExactAndAllowWhileIdle to bypass battery optimization for the demo
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                );
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }
        }
    }
}