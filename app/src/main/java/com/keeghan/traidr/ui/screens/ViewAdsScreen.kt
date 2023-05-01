package com.keeghan.traidr.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keeghan.traidr.ui.composables.ProductCard
import com.keeghan.traidr.viewmodels.ProductsViewModel
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ViewAdsScreen(
    categoryId: Int,
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val allProductsResponse by remember { viewModel.allProductsRes }.observeAsState()
    val errorMsg by viewModel.errorMsg.observeAsState("")
    val isLoading by remember { viewModel.isLoading }.observeAsState(false)

    val context = LocalContext.current

    val bottomBarHeight = 48.dp
    val bottomBarHeightPx = with(LocalDensity.current) { bottomBarHeight.roundToPx().toFloat() }
    val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }


    val pullRefreshState = rememberPullRefreshState(isLoading, {
        viewModel.getAllProducts()
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
                val newOffset = bottomBarOffsetHeightPx.value + delta
                bottomBarOffsetHeightPx.value = newOffset.coerceIn(-bottomBarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    Scaffold(modifier = Modifier
        .nestedScroll(nestedScrollConnection)
        .fillMaxHeight()
        .fillMaxWidth(),
        bottomBar = {
            BottomAppBar(modifier = Modifier
                .height(bottomBarHeight)
                .offset { IntOffset(x = 0, y = -bottomBarOffsetHeightPx.value.roundToInt()) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    allProductsResponse?.links?.let { links ->
                        val linkButtons = mapOf(
                            "first" to links.first,
                            "prev" to links.prev,
                            "next" to links.next,
                            "last" to links.last
                        )
                        linkButtons.forEach { (key, value) ->
                            Button(
                                onClick = {
                                    val url = removeFirstSlash(value)
                                    viewModel.getAllProducts(url)
                                },
                                shape = CircleShape,
                            ) {
                                Icon(
                                    imageVector = when (key) {
                                        "first" -> Icons.Filled.ArrowBack
                                        "prev" -> Icons.Filled.KeyboardArrowLeft
                                        "next" -> Icons.Filled.KeyboardArrowRight
                                        "last" -> Icons.Filled.ArrowForward
                                        else -> Icons.Default.Menu // Shouldn't happen
                                    }, contentDescription = key
                                )
                            }
                        }
                    }
                }
            }
        }) {
        Box(Modifier.pullRefresh(pullRefreshState)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Items on Top
                item {
                    Text(text = categoryId.toString())
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

fun removeFirstSlash(path: String): String {
    return path.replaceFirst(Regex("^/v1/"), "")
}