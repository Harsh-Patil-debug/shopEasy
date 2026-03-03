package com.example.shopeasy;

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
        SessionManager session = new SessionManager(context);
        if (!session.isLoggedIn()) return;

        String channelId = "shopeasy_channel";
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String title = intent.getStringExtra("TITLE");
        String message = intent.getStringExtra("MESSAGE");
        if (title == null) title = "ShopEasy Alert";
        if (message == null) message = "You have a new update.";

        String currentTime = DateFormat.getTimeInstance().format(new Date());

        // Save to DB so it appears in the in-app Notification Center [cite: 815-825]
        NotificationEntity newNotif = new NotificationEntity(session.getUserEmail(), title, message, currentTime);
        AppDatabase.getInstance(context).notificationDao().insert(newNotif);

        // Build and display the actual system popup notification [cite: 826-875]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "ShopEasy Alerts", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        Intent clickIntent = new Intent(context, MainActivity.class);
        clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_menu_home)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}