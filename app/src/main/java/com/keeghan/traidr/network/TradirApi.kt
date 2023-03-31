package com.keeghan.traidr.network

import com.keeghan.traidr.models.user.NewUser
import com.keeghan.traidr.models.user.User
import com.keeghan.traidr.models.user.loginUser.UserCredentials
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TradirApi {

    @POST("users")
    suspend fun signUpWithEmail(@Body newUser: NewUser): Response<User>

    @POST("auth/sessions")
    suspend fun logInWithEmail(@Body newUser: NewUser): Response<UserCredentials>

}