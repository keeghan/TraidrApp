package com.keeghan.traidr.ui.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keeghan.traidr.models.product.Links
import com.keeghan.traidr.models.product.Product
import com.keeghan.traidr.ui.composables.AdsBottomBar
import com.keeghan.traidr.ui.composables.AlertDialogBox
import com.keeghan.traidr.ui.composables.ProductCard
import com.keeghan.traidr.viewmodels.ProductsViewModel

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AdsScreen(
    modifier: Modifier,
    productsViewModel: ProductsViewModel = hiltViewModel(),
    token: String,
    isLoading: Boolean,
    showDelete: Boolean,
    refreshUrl: MutableState<String>,
    productList: List<Product>?,
    links: Links?
) {
    val deleteTracker by remember { productsViewModel.deleteTracker }.observeAsState(false)

    val bottomBarHeight = 48.dp
    val bottomBarHeightPx = with(LocalDensity.current) { bottomBarHeight.roundToPx().toFloat() }
    var bottomBarOffsetHeightPx by remember { mutableStateOf(0f) }

    val showDeleteDialog = remember { mutableStateOf(false) }
    val deleteProductId = remember { mutableIntStateOf(0) }

    val pullRefreshState = rememberPullRefreshState(isLoading, onRefresh = {
        productsViewModel.getAllProducts(refreshUrl.value)
    })

    //execute reload successful deletion of item
    LaunchedEffect(key1 = deleteTracker, block = {
        if (deleteTracker) {
            productsViewModel.getAllProducts()
        }
    })

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

    Scaffold(modifier = modifier.then(
        Modifier
            .nestedScroll(nestedScrollConnection)
            .fillMaxHeight()
            .fillMaxWidth()
    ), bottomBar = {   //Bottom Bar Implementation
        AdsBottomBar(
            bottomBarHeight, bottomBarOffsetHeightPx, links, productsViewModel, refreshUrl
        )
    }) {
        Box(
            Modifier
                .fillMaxSize().padding(start = 2.dp, end = 2.dp)
                .pullRefresh(pullRefreshState)
        ) {
            if (showDeleteDialog.value) {  //delete dialog
                AlertDialogBox(
                    showDeleteDialog = showDeleteDialog,
                    deleteProductId = deleteProductId,
                    token = token,
                    productsViewModel = productsViewModel
                )
            }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 5.dp,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .fillMaxSize()
            ) {
                if (productList != null)
                    items(productList, key = { it.id }) { product ->
                    Box(contentAlignment = Alignment.TopEnd) {
                        ProductCard(
                            productId = product.id.toInt(),
                            onCardClick = {},
                            type = product.type,
                            price = product.attributes.price,
                            title = product.attributes.title
                        )
                        if (showDelete) {
                            IconButton(onClick = {
                                deleteProductId.value = product.id.toInt()
                                showDeleteDialog.value = true
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "delete",
                                    modifier = Modifier.padding(8.dp)
                                )
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

