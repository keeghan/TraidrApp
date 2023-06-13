package com.keeghan.traidr.utils

import com.keeghan.traidr.models.product.Product


//method to truncate navigation links from response to pass to viewModel
fun removeFirstSlash(path: String): String {
    return path.replaceFirst(Regex("^/v1/"), "")
}

//sort a list of products by user who posted it
fun sortProductsByUserId(productsResponse: List<Product>?, userId: String): List<Product>? {
    val products = productsResponse?.filter { it.relationships.user.data.id == userId }
    return products?.sortedByDescending { it.id }
}

fun sortProductsByID(products: List<Product>): List<Product> {
    return products.sortedByDescending { it.id }
}