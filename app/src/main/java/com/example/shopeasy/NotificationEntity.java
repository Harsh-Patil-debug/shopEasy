package com.example.shopeasy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class NotificationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String userEmail;
    public String title;
    public String message;
    public String time;

    public NotificationEntity(String userEmail, String title, String message, String time) {
        this.userEmail = userEmail;
        this.title = title;
        this.message = message;
        this.time = time;
    }
}