package com.keeghan.traidr.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keeghan.traidr.ui.composables.ProductCard
import com.keeghan.traidr.viewmodels.ProductsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ViewAdsScreen(
    categoryId: Int,
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val allProductsResponse by remember { viewModel.allProductsRes }.observeAsState()
    val errorMsg by viewModel.message.observeAsState("")
    val isLoading by remember { viewModel.isLoading }.observeAsState(false)

    val context = LocalContext.current

    val bottomBarHeight = 48.dp
    val bottomBarHeightPx = with(LocalDensity.current) { bottomBarHeight.roundToPx().toFloat() }
    var bottomBarOffsetHeightPx by remember { mutableStateOf(0f) }

    val refreshUrl = remember{ mutableStateOf("products") }

    val pullRefreshState = rememberPullRefreshState(isLoading, {
        viewModel.getAllProducts(refreshUrl.value)
    })

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }

    LaunchedEffect(errorMsg) {
        if (errorMsg.isNotEmpty()) {
            showToast(context, errorMsg)
        }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = bottomBarOffsetHeightPx + delta
                bottomBarOffsetHeightPx = newOffset.coerceIn(-bottomBarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    Scaffold(modifier = Modifier
        .nestedScroll(nestedScrollConnection)
        .fillMaxHeight()
        .fillMaxWidth(),
        bottomBar = {
            BottomBar(
                bottomBarHeight,
                bottomBarOffsetHeightPx,
                allProductsResponse,
                viewModel,
                refreshUrl
            )
        }) {
        Box(Modifier.pullRefresh(pullRefreshState)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(text = categoryId.toString(),Modifier.padding(bottom = 5.dp))
                } //end of top Items

                item {
                    if (allProductsResponse != null) {   //check if items loaded
                        LazyVerticalStaggeredGrid(
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp)
                                .fillParentMaxHeight(),
                            columns = StaggeredGridCells.Fixed(2),
                            verticalItemSpacing = 10.dp,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            allProductsResponse?.let { response ->
                                items(response.data.size) { index ->
                                    val product = response.data[index]
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
            //pullRefresh implementation
            PullRefreshIndicator(
                isLoading,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter),
                contentColor = MaterialTheme.colors.primary,
                scale = true,
            )
        }
    }
}
