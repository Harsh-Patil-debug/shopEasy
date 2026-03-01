package com.example.shopeasy;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    LiveData<List<NotificationEntity>> getAllNotificationsLive();

    @Insert
    void insert(NotificationEntity notification);

    @Query("DELETE FROM notifications")
    void clearAll();
}
