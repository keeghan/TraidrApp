package com.keeghan.traidr.models.product

data class ProductReq(
    val product: Product,
) {
    data class Product(
        val price: String,
        val published: String,
        val quantity: String,
        val title: String,
    )
}