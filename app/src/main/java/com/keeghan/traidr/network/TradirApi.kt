package com.keeghan.traidr.network

import com.keeghan.traidr.models.product.ProductReqRes
import com.keeghan.traidr.models.product.ProductReq
import com.keeghan.traidr.models.product.ProductResponse
import com.keeghan.traidr.models.product.ProductsResponse
import com.keeghan.traidr.models.user.NewUser
import com.keeghan.traidr.models.user.UserResponse
import com.keeghan.traidr.models.user.loginUser.UserCredentials
import com.keeghan.traidr.models.user.logout.LogoutResponse
import retrofit2.Response
import retrofit2.http.*


interface TradirApi {

    //User
    @POST("users")
    suspend fun signUpWithEmail(@Body newUser: NewUser): Response<UserResponse>

    @POST("auth/sessions")
    suspend fun logInWithEmail(@Body newUser: NewUser): Response<UserCredentials>

    @DELETE("auth/sessions/{id}")
    suspend fun signOut(
        @Header("Authorization") token: String,
        @Path("id") userId: Int,
    ): Response<LogoutResponse>

    //Product
    @GET("products/{id}")
    suspend fun getProduct(@Path("id") productId: Int): Response<ProductResponse>

    @GET("products")
    suspend fun getAllProduct(): Response<ProductsResponse>

    @POST("products")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body productRequest: ProductReq,
    ): Response<ProductReqRes>
}