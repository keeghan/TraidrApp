package com.keeghan.traidr.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keeghan.traidr.models.product.Product
import com.keeghan.traidr.ui.composables.ProductCard
import com.keeghan.traidr.viewmodels.ProductsViewModel
import kotlinx.coroutines.delay

@Composable
fun ViewAdsScreen(
    categoryId: Int,
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val allProductsResponse by remember { viewModel.allProductsRes }.observeAsState()
    val allProducts: List<Product> by viewModel.allProducts.observeAsState(listOf())
    val links = remember { allProductsResponse?.links }
    val isRefreshing by viewModel.isLoading.observeAsState()


    //val productList = remember { mutableStateOf(emptyList<Product>()) }


    LaunchedEffect(Unit) {
        while (allProducts.isEmpty()) {
            viewModel.getAllProduct()
            delay(6000)
        }
    }
    Box(Modifier) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.Center
        ) {
            Text(text = categoryId.toString())

            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(allProducts) {
                    ProductCard(
                        productId = it.id.toInt(),
                        onCardClick = {},
                        type = it.type,
                        price = it.attributes.price,
                        title = it.attributes.title
                    )
                }
            }

            if (links != null) {
                val linkBtns = mapOf(
                    "first" to links.first,
                    "prev" to links.prev,
                    "next" to links.next,
                    "last" to links.last
                )

                linkBtns.forEach {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { /* navigate to the corresponding URL */ },
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text(it.key)
                        }

                    }
                }
            }
            Button(
                onClick = { viewModel.getAllProduct() },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text("reload")
            }
        }
    }
}


