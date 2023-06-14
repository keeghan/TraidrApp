package com.keeghan.traidr.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.keeghan.traidr.utils.Auth
import com.keeghan.traidr.viewmodels.ProductsViewModel
import kotlinx.coroutines.flow.collect

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

    val productList = productsViewModel.findAllProducts().collectAsLazyPagingItems()
    val productFlow = productsViewModel.findAllProducts()


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
        AdsLayout(productList, Modifier.padding(it))
    }
}