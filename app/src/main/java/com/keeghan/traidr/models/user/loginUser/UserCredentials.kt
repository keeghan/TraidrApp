package com.keeghan.traidr.models.user.loginUser

data class UserCredentials(
    val id: Int,
    val email: String,
    val token: String
)