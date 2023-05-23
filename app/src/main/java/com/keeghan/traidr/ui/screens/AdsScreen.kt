package com.keeghan.traidr.ui.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.keeghan.traidr.models.product.Product
import com.keeghan.traidr.models.product.ProductsResponse
import com.keeghan.traidr.ui.composables.ProductCard
import com.keeghan.traidr.viewmodels.ProductsViewModel
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AdsScreen(
    navController: NavHostController = rememberNavController(),
    productsViewModel: ProductsViewModel = hiltViewModel(),
    userId: String,
    token: String
) {
    val allProductsResponse by remember { productsViewModel.allProductsRes }.observeAsState()
    val errorMsg by productsViewModel.message.observeAsState("")
    val isLoading by remember { productsViewModel.isLoading }.observeAsState(false)

    val deleteTracker by remember { productsViewModel.deleteTracker }.observeAsState(false)

    val context = LocalContext.current

    val bottomBarHeight = 48.dp
    val bottomBarHeightPx = with(LocalDensity.current) { bottomBarHeight.roundToPx().toFloat() }
    var bottomBarOffsetHeightPx by remember { mutableStateOf(0f) }

    val refreshUrl = remember { mutableStateOf("products") }

    val pullRefreshState = rememberPullRefreshState(isLoading, {
        productsViewModel.getAllProducts(refreshUrl.value)
    })

    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteProductId by remember { mutableStateOf(0) }

    //execute reload on opening screen
    LaunchedEffect(Unit) {
        productsViewModel.getAllProducts()
    }

    LaunchedEffect(errorMsg) {
        if (errorMsg.isNotEmpty()) {
            showToast(context, errorMsg)
        }
    }

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

    Scaffold(modifier = Modifier
        .nestedScroll(nestedScrollConnection)
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
        bottomBar = {   //Bottom Bar Implementation
            BottomBar(
                bottomBarHeight,
                bottomBarOffsetHeightPx,
                allProductsResponse,
                productsViewModel,
                refreshUrl
            )
        }) {
        Box(
            Modifier
                .padding(top = it.calculateTopPadding())
                .pullRefresh(pullRefreshState)
        ) {
            if (showDeleteDialog) {
                AlertDialog(onDismissRequest = { showDeleteDialog = false }) {
                    Surface(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(text = "Delete Confirmation", style = MaterialTheme.typography.h6)
                            Text(text = "Are you sure you want to delete Ad?")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                            ) {
                                Text(
                                    text = "close",
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .padding(start = 4.dp, end = 4.dp)
                                        .clickable {
                                            showDeleteDialog = false
                                        }
                                )
                                Text(
                                    text = "confirm",
                                    modifier = Modifier
                                        .padding(start = 4.dp, end = 4.dp)
                                        .clickable {
                                            productsViewModel.deleteProduct(
                                                deleteProductId,
                                                token
                                            )
                                            deleteProductId = 0
                                            showDeleteDialog = false
                                        },
                                    color = MaterialTheme.colors.primary
                                )
                            }

                        }
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                            allProductsResponse?.let {
                                val sorted = sortProductsByUserId(
                                    allProductsResponse!!, userId
                                )    //Sort Received List into users own list
                                items(sorted.size) { index ->
                                    val product = sorted[index]
                                    Box(contentAlignment = Alignment.TopEnd) {
                                        ProductCard(
                                            productId = product.id.toInt(),
                                            onCardClick = {},
                                            type = product.type,
                                            price = product.attributes.price,
                                            title = product.attributes.title
                                        )
                                        IconButton(onClick = {
                                            deleteProductId = product.id.toInt()
                                            showDeleteDialog = true
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

//method to truncate navigation links from response to pass to viewModel
fun removeFirstSlash(path: String): String {
    return path.replaceFirst(Regex("^/v1/"), "")
}

fun sortProductsByUserId(productsResponse: ProductsResponse, userId: String): List<Product> {
    val products = productsResponse.data.filter { it.relationships.user.data.id == userId }
    return products.sortedByDescending { it.id }
}

@Composable
fun BottomBar(
    bottomBarHeight: Dp,
    bottomBarOffsetHeightPx: Float,
    allProductsResponse: ProductsResponse?,
    productsViewModel: ProductsViewModel,
    refreshUrl: MutableState<String>
) {
    BottomAppBar(modifier = Modifier
        .height(bottomBarHeight)
        .offset { IntOffset(x = 0, y = -bottomBarOffsetHeightPx.roundToInt()) }) {
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
                //create navigation buttons
                linkButtons.forEach { (key, value) ->
                    Button(
                        onClick = {
                            val url = removeFirstSlash(value)
                            productsViewModel.getAllProducts(url)
                            refreshUrl.value =
                                url  //save current navigation destination for pullRefresh
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
}
