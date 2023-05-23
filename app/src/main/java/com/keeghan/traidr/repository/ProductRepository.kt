package com.keeghan.traidr.repository

import com.keeghan.traidr.models.product.ProductReq
import com.keeghan.traidr.models.product.ProductReqRes
import com.keeghan.traidr.models.product.ProductResponse
import com.keeghan.traidr.models.product.ProductsResponse
import com.keeghan.traidr.models.user.logout.LogoutResponse
import com.keeghan.traidr.network.TradirApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(private val api: TradirApi) {

    suspend fun getProduct(productId: Int): Response<ProductResponse> {
        return api.getProduct(productId)
    }

    suspend fun deleteProduct(token: String, productId: Int): Response<Unit> {
        return api.deleteProduct(token, productId)
    }

    suspend fun getAllProduct(url: String): Response<ProductsResponse> {
        return api.getAllProduct(url)
    }

    suspend fun createProduct(
        token: String, productReq: ProductReq,
    ): Response<ProductReqRes> {
        return api.createProduct(token, productReq)
    }


}