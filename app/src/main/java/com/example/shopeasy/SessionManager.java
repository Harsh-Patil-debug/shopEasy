package com.example.shopeasy;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("ShopEasySession", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void loginUser(String email) {
        editor.putString("USER_EMAIL", email);
        editor.putBoolean("IS_LOGGED_IN", true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean("IS_LOGGED_IN", false);
    }

    public String getUserEmail() {
        return prefs.getString("USER_EMAIL", "");
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}