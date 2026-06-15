package com.example.firebase

import android.content.Context
import android.content.SharedPreferences

class AuthManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("admission_gk_auth", Context.MODE_PRIVATE)

    val isLoggedIn: Boolean get() = prefs.getBoolean("is_logged_in", false)
    val fingerprintReady: Boolean get() = prefs.getBoolean("fingerprint_ready", false)

    fun login() {
        prefs.edit().putBoolean("is_logged_in", true).apply()
    }

    fun loginWithFingerprint() {
        prefs.edit().putBoolean("fingerprint_ready", true).putBoolean("is_logged_in", true).apply()
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    fun getUserId(): String {
        return prefs.getString("user_id", "user_${System.currentTimeMillis()}") ?: "user_unknown"
    }
}
