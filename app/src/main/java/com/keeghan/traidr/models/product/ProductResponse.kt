package com.keeghan.traidr.models.product

import java.math.BigDecimal

data class ProductResponse(
    val data: Data,
    val included: List<Included>?
) {
    data class Data(
        val id: Int,
        val type: String,
        val attributes: Attributes,
        val relationships: Relationships
    ) {
        data class Attributes(
            val title: String,
            val price: BigDecimal,
            val quantity: Int,
            val published: Boolean
        )

        data class Relationships(
            val user: User
        ) {
            data class User(
                val data: UserData
            ) {
                data class UserData(
                    val id: Int,
                    val type: String
                )
            }
        }
    }

    data class Included(
        val id: Int,
        val type: String,
        val attributes: IncludedAttributes,
        val relationships: IncludedRelationships
    ) {
        data class IncludedAttributes(
            val email: String
        )

        data class IncludedRelationships(
            val products: Products
        ) {
            data class Products(
                val data: List<ProductData>
            ) {
                data class ProductData(
                    val id: Int,
                    val type: String
                )
            }
        }
    }
}
