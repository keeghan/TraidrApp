package com.keeghan.traidr.utils

class Constants {
    companion object {

        const val CHECK_CAPITAL = "[A-Z]"
        const val CHECK_SYMBOL_DIGIT = "[!@#\\\$%^&*(),.?\\\":{}|<>0-9]"


        const val BASE_URL = "https://traidr.onrender.com/api/v1/"

        //PREFS
        const val AUTH_PREFERENCE = "authPreference"
        const val AUTH_TOKEN_KEY = "auth_token"
        const val SIGNIN_KEY = "signed_in"
        const val USER_ID_KEY = "user_Id"
        const val USER_ID_DEFAULT_KEY = 77777777
        const val AUTH_DEFAULT_TOKEN_KEY = "no stored token, you shouldn't be here"


        //values
        const val NETWORK_TIMEOUT: Long = 11000
        const val RETROFIT_TIMEOUT: Long = 11

        //Settings
        const val SETTINGS_PREFERENCE = "settingsPreference"
        const val DARK_THEME_KEY = "dark_theme_enabled"


    }
}