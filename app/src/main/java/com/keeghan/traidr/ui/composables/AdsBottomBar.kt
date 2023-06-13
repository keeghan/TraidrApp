package com.keeghan.traidr.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.keeghan.traidr.models.product.Links
import com.keeghan.traidr.models.product.ProductsResponse
import com.keeghan.traidr.utils.removeFirstSlash
import com.keeghan.traidr.viewmodels.ProductsViewModel
import kotlin.math.roundToInt

@Composable
fun AdsBottomBar(
    bottomBarHeight: Dp,
    bottomBarOffsetHeightPx: Float,
    links: Links?,
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
            links?.let { links ->
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
