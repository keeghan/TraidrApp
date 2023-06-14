package com.keeghan.traidr.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.keeghan.traidr.utils.Auth
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
    val userProducts = productsViewModel.findAllProducts(userId).collectAsLazyPagingItems()


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
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            AdsLayout(userProducts, Modifier)
        }
    }
}
