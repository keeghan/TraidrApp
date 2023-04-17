package com.keeghan.traidr.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keeghan.traidr.ui.composables.ProductCard
import com.keeghan.traidr.viewmodels.ProductsViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun ViewAdsScreen(
    categoryId: Int,
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val allProductsResponse by remember { viewModel.allProductsRes }.observeAsState()
    val isRefreshing by viewModel.isLoading.observeAsState()
    val errorMsg by viewModel.errorMsg.observeAsState("")
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }

    LaunchedEffect(errorMsg) {
        if (errorMsg.isNotEmpty()) {
            showToast(context, errorMsg)
        }
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                //   .verticalScroll(scrollState)
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = categoryId.toString())
            Button(
                onClick = { viewModel.getAllProducts() },
            ) {
                Text(
                    text = "reload"
                )
            }

            if (isRefreshing!!) {   //todo: fix circular indicator
                CircularProgressIndicator()
            } else if (allProductsResponse != null) {

                //Buttons
                allProductsResponse?.links?.let { links ->
                    val linkButtons = mapOf(
                        "first" to links.first,
                        "prev" to links.prev,
                        "next" to links.next,
                        "last" to links.last
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        linkButtons.forEach { (key, value) ->
                            Button(
                                onClick = {
                                    val url = removeFirstSlash(value)
                                    viewModel.getAllProducts(url)
                                },
                                modifier = Modifier
                                    .width(72.dp)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                ),
                                contentPadding = PaddingValues(8.dp)
                            ) {
                                Text(
                                    text = key,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }

                //Todo: fix list in column
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(allProductsResponse!!.data.size) { index ->
                        val product = allProductsResponse!!.data[index]
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
}

fun removeFirstSlash(path: String): String {
    return path.replaceFirst(Regex("^/v1/"), "")
}