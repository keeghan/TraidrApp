package com.keeghan.traidr.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.keeghan.traidr.models.product.Product
import com.keeghan.traidr.ui.composables.ProductCard

@Composable
fun AdsLayout(
    productList: LazyPagingItems<Product>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier.then(Modifier.fillMaxSize()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val state = productList.loadState.refresh) {
            is LoadState.Error -> {
                Toast.makeText(
                    LocalContext.current, state.error.message ?: "error", Toast.LENGTH_SHORT
                ).show()
            }
            is LoadState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {}
        }

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 5.dp,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp)
                .fillMaxSize()
        ) {
            if (productList.itemCount > 0) {
                items(count = productList.itemCount,
                    key = productList.itemKey { it.id }) { loc ->
                    Box(contentAlignment = Alignment.TopEnd) {
                        val product = productList[loc]
                        if (product != null) {
                            ProductCard(
                                productId = product.id.toInt(),
                                onCardClick = {},
                                type = product.type,
                                price = product.attributes.price,
                                title = product.attributes.title
                            )
                        }
                    }
                }
            }
        }

        when (val state = productList.loadState.append) {
            is LoadState.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    state.error.message ?: "error",
                    Toast.LENGTH_SHORT
                ).show()
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Reload")
                }
            }
            is LoadState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {}
        }
    }
}