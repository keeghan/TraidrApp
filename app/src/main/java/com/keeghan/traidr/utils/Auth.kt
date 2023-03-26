package com.keeghan.traidr.utils

import android.content.Context
import com.keeghan.traidr.utils.Constants.Companion.AUTH_PREFERENCE
import com.keeghan.traidr.utils.Constants.Companion.SIGNIN_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Auth @Inject constructor(@ApplicationContext private val context: Context) {
    private val prefs = context.getSharedPreferences(AUTH_PREFERENCE, Context.MODE_PRIVATE)

    public fun isSignedIn(): Boolean {
        return prefs.getBoolean(SIGNIN_KEY, false)
    }

   public fun setSignedIn(signedIn: Boolean) {
        prefs.edit().putBoolean(SIGNIN_KEY, signedIn).apply()
    }
}