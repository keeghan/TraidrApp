package com.keeghan.traidr.network

import com.keeghan.traidr.models.user.NewUser
import com.keeghan.traidr.models.user.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TradirApi {

    @POST("users")
    suspend fun signUpWithEmail(@Body newUser: NewUser): Response<User>

    @GET("users/{userId}")
    suspend fun signInWithEmail(email: String, password: String)

    @GET("users/{id}")
    suspend fun getUser(@Query("id") user_Id: Int)

    @PUT("users/:id")
    suspend fun updateUser(email: String, password: String)


}