package com.keeghan.traidr.utils

import android.content.Context
import com.keeghan.traidr.utils.Constants.Companion.AUTH_PREFERENCE
import com.keeghan.traidr.utils.Constants.Companion.SIGNIN_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
* Helper Class that tracks signIn Status
* */
@Singleton
class Auth @Inject constructor(@ApplicationContext private val context: Context) {
    private val prefs = context.getSharedPreferences(AUTH_PREFERENCE, Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(SIGNIN_KEY, false)
    }

    fun setLoggedIn(signedIn: Boolean) {
        prefs.edit().putBoolean(SIGNIN_KEY, signedIn).apply()
    }

    fun saveAuthToken(authToken: String) {
        prefs.edit().putString("auth_token", authToken).apply()
    }

}