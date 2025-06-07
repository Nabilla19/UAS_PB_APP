package com.example.proyektorapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {
    private static final String PREF_NAME = "user_pref";
    private final SharedPreferences prefs;

    public SharedPrefsHelper(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        prefs.edit().putString("token", token).apply();
    }

    private static final String KEY_EMAIL = "email";

    // Method untuk email
    public void saveEmail(String email) {
        prefs.edit().putString(KEY_EMAIL, email).apply();
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    public String getToken() {
        return prefs.getString("token", null);
    }

    public void saveName(String name) {
        prefs.edit().putString("name", name).apply();
    }

    public String getName() {
        return prefs.getString("name", null);
    }

    public void saveRole(String role) {
        prefs.edit().putString("role", role).apply();
    }

    public String getRole() {
        return prefs.getString("role", null);
    }

    public void saveId(String id) {
        prefs.edit().putString("user_id", id).apply();
    }

    public String getId() {
        return prefs.getString("user_id", null);
    }


    public void clear() {
        prefs.edit().clear().apply();
    }
}
