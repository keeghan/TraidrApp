package com.keeghan.traidr.models.user

data class UserResponse(
    val data: Data
) {
    data class Data(
        val id: Int,
        val type: String,
        val attributes: Attributes,
        val relationships: Relationships
    ) {
        data class Attributes(
            val email: String
        )

        data class Relationships(
            val products: Products
        ) {
            data class Products(
                val data: List<Any>
            )
        }
    }
}
