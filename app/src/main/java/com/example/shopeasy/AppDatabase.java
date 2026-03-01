package com.example.shopeasy;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// ✅ STEP 1: Add NotificationEntity.class to the entities list
// ✅ STEP 2: Ensure version is set to 2 to trigger the migration
@Database(entities = {CartEntity.class, OrderEntity.class, NotificationEntity.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract CartDao cartDao();
    public abstract OrderDao orderDao();

    // ✅ This method now has a corresponding Entity in the list above
    public abstract NotificationDao notificationDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "shop_easy_db"
                    )
                    .fallbackToDestructiveMigration() // Wipes the old DB and recreates it for version 2
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}