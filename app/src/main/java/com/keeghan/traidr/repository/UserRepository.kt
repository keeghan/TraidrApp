package com.keeghan.traidr.repository

import com.keeghan.traidr.models.user.NewUser
import com.keeghan.traidr.models.user.User
import com.keeghan.traidr.models.user.UserX
import com.keeghan.traidr.models.user.loginUser.UserCredentials
import com.keeghan.traidr.network.TradirApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val api: TradirApi) {

    suspend fun createUserWithEmail(email: String, password: String): Response<User> {
        val userX = UserX(email, password)
        return api.signUpWithEmail(NewUser(userX))
    }

    suspend fun loginWithEmail(email: String, password: String): Response<UserCredentials> {
        val userX = UserX(email, password)
        return api.logInWithEmail(NewUser(userX))
    }
}