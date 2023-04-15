package com.keeghan.traidr.models.product

data class ProductReqRes(
    val id: Int,
    val type: String,
    val attributes: Attributes,
    val relationships: Relationships
) {
    data class Attributes(
        val title: String,
        val price: Double,
        val quantity: Int,
        val published: Boolean
    )

    data class Relationships(
        val user: User
    )

    data class User(
        val data: UserData
    )

    data class UserData(
        val id: Int,
        val type: String
    )
}
