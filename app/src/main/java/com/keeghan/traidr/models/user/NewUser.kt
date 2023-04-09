package com.keeghan.traidr.models.user


data class NewUser(
    val user: User,
)

data class User(
    val email: String,
    val password: String,
)