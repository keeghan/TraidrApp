package com.keeghan.traidr.repository

import com.keeghan.traidr.models.user.NewUser
import com.keeghan.traidr.models.user.User
import com.keeghan.traidr.models.user.UserResponse
import com.keeghan.traidr.models.user.loginUser.UserCredentials
import com.keeghan.traidr.models.user.logout.LogoutResponse
import com.keeghan.traidr.network.TradirApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val api: TradirApi) {

    //todo
    suspend fun createUserWithEmail(email: String, password: String): Response<UserResponse> {
        return api.signUpWithEmail(NewUser(User(email, password)))
    }

    suspend fun loginWithEmail(email: String, password: String): Response<UserCredentials> {
        return api.logInWithEmail(NewUser(User(email, password)))
    }

    suspend fun signOut(token: String, Id: Int): Response<LogoutResponse> {
        return api.signOut(token, Id)
    }

}