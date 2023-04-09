package com.keeghan.traidr.repository

import com.keeghan.traidr.models.ProductResponse
import com.keeghan.traidr.network.TradirApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(private val api: TradirApi) {

    suspend fun getProduct(productId: Int): Response<ProductResponse> {
        return api.getProduct(productId)
    }
}