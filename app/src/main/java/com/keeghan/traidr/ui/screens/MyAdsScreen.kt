package com.keeghan.traidr.ui.screens

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.keeghan.traidr.utils.Auth
import com.keeghan.traidr.utils.sortProductsByUserId
import com.keeghan.traidr.viewmodels.ProductsViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MyAdsScreen(
    navController: NavController = rememberNavController(),
    productsViewModel: ProductsViewModel = hiltViewModel(),

    ) {
    val context = LocalContext.current
    val auth = Auth(context)

    val userId = auth.getUserId().toString()
    val token = auth.getToken().toString()
    val allProductsResponse by remember { productsViewModel.allProductsRes }.observeAsState()
    val errorMsg by productsViewModel.message.observeAsState("")
    val isLoading by remember { productsViewModel.isLoading }.observeAsState(false)

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
        val sorted = sortProductsByUserId(allProductsResponse?.data, userId)
        AdsScreen(
            modifier = Modifier.padding(it),
            isLoading = isLoading,
            productsViewModel = productsViewModel,
            token = token,
            refreshUrl = refreshUrl,
            showDelete = true,
            productList = sorted,
            links = allProductsResponse?.links
        )
    }
}
