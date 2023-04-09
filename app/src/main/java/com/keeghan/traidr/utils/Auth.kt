package com.keeghan.traidr.utils

import android.content.Context
import com.keeghan.traidr.utils.Constants.Companion.AUTH_DEFAULT_TOKEN_KEY
import com.keeghan.traidr.utils.Constants.Companion.AUTH_PREFERENCE
import com.keeghan.traidr.utils.Constants.Companion.AUTH_TOKEN_KEY
import com.keeghan.traidr.utils.Constants.Companion.SIGNIN_KEY
import com.keeghan.traidr.utils.Constants.Companion.USER_ID_DEFAULT_KEY
import com.keeghan.traidr.utils.Constants.Companion.USER_ID_KEY
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

    fun logIn() {
        prefs.edit().putBoolean(SIGNIN_KEY, true).apply()
    }

    fun logOut() {
        prefs.edit().putBoolean(SIGNIN_KEY, false).apply()
        prefs.edit().remove(AUTH_TOKEN_KEY).apply()
    }


    fun saveAuthToken(authToken: String) {
        prefs.edit().putString(AUTH_TOKEN_KEY, authToken).apply()
    }


    fun getToken(): String? {
        return prefs.getString(AUTH_TOKEN_KEY, AUTH_DEFAULT_TOKEN_KEY)
    }

    fun saveUserId(userId: Int) {
        prefs.edit().putInt(USER_ID_KEY, userId).apply()
    }

    fun getUserId(): Int {
        return prefs.getInt(USER_ID_KEY, USER_ID_DEFAULT_KEY)
    }
}