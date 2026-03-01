package com.example.shopeasy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications") // 👈 Must have this!
public class NotificationEntity {
    @PrimaryKey(autoGenerate = true) // 👈 Must have this!
    public int id;

    public String title;
    public String message;
    public String time;

    public NotificationEntity(String title, String message, String time) {
        this.title = title;
        this.message = message;
        this.time = time;
    }
}