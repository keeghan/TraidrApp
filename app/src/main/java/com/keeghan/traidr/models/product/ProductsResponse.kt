package com.keeghan.traidr.models.product

data class ProductsResponse(
    val data: List<Product>,
    val links: Links
)

data class Product(
    val id: String,
    val type: String,
    val attributes: ProductAttributes,
    val relationships: Relationships
)

data class ProductAttributes(
    val title: String,
    val price: String,
    val quantity: Int,
    val published: Boolean
)

data class Relationships(
    val user: UserData
)

data class UserData(
    val data: UserDataAttributes
)

data class UserDataAttributes(
    val id: String,
    val type: String
)

data class Links(
    val first: String,
    val last: String,
    val prev: String,
    val next: String
)
