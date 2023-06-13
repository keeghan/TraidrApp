package com.keeghan.traidr.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.keeghan.traidr.ui.composables.ProductCard
import com.keeghan.traidr.utils.Auth
import com.keeghan.traidr.viewmodels.ProductsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAdsScreen(
    navController: NavController = rememberNavController(),
    productsViewModel: ProductsViewModel = hiltViewModel(),
    categoryId: Int,
) {
    val context = LocalContext.current
    val auth = Auth(context)

    //  val token = auth.getToken().toString()
    // val allProductsResponse by remember { productsViewModel.allProductsRes }.observeAsState()
    val errorMsg by productsViewModel.message.observeAsState("")
    val isLoading by remember { productsViewModel.isLoading }.observeAsState(false)

    val productList = productsViewModel.findAllProducts().collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        productsViewModel.getAllProducts()
    }

    LaunchedEffect(errorMsg) {
        if (errorMsg.isNotEmpty()) {
            showToast(context, errorMsg)
        }
    }
    val refreshUrl = remember { mutableStateOf("products") }

    Scaffold(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = { Text("ViewAds") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Favorite",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                },
            )
        },
    ) {
        Column(verticalArrangement = Arrangement.SpaceAround) {

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
            item {
                when (val state = productList.loadState.append) {
                    is LoadState.Error -> {
                        Toast.makeText(
                            LocalContext.current,
                            state.error.message ?: "error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is LoadState.Loading -> {
                        CircularProgressIndicator()
                    }

                    else -> {}
                }
            }
        }}
//        AdsScreen(
//            modifier = Modifier.padding(it),
//            isLoading = isLoading,
//            productsViewModel = productsViewModel,
//            token = token,
//            refreshUrl = refreshUrl,
//            showDelete = false,
//            productList = allProductsResponse?.data,
//            links = allProductsResponse?.links
//        )
    }
}