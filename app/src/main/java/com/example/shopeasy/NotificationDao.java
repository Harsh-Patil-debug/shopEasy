package com.example.shopeasy;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE userEmail = :email ORDER BY id DESC")
    LiveData<List<NotificationEntity>> getNotificationsByUserLive(String email);

    @Insert
    void insert(NotificationEntity notification);

    @Query("DELETE FROM notifications WHERE userEmail = :email")
    void clearAllByUser(String email);
}