package com.keeghan.traidr.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper Class that tracks signIn Status
 * */
@Singleton
class SettingsPreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val prefs =
        context.getSharedPreferences(Constants.SETTINGS_PREFERENCE, Context.MODE_PRIVATE)

    fun isDarkTheme(): Boolean {
        return prefs.getBoolean(Constants.DARK_THEME_KEY, true)
    }

    fun saveThemeState(darkThemeState: Boolean) {
        prefs.edit().putBoolean(Constants.DARK_THEME_KEY, darkThemeState).apply()
    }
}