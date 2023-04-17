package com.keeghan.traidr.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keeghan.traidr.R
import com.keeghan.traidr.ui.composables.CategoryCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onCategoryClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Home")
        LazyVerticalGrid(columns = GridCells.Adaptive(120.dp)) {
            items(itemCategories) { it ->
                CategoryCard(imageResId = it.resId,
                    itemId = it.id,
                    type = it.type,
                    onCardClick = { id ->
                        onCategoryClick(id) //pass What button was Click
                    })
            }
        }
    }
}


data class ItemCategory(val id: Int, val type: String, val resId: Int)

val itemCategories = listOf(
    ItemCategory(1, "Electronics", R.drawable.electronics),
    ItemCategory(2, "Clothing and fashion", R.drawable.clothing),
    ItemCategory(3, "Home and garden", R.drawable.home),
    ItemCategory(4, "Beauty and personal care", R.drawable.beauty),
    ItemCategory(5, "Sports and outdoor gear", R.drawable.sports),
    ItemCategory(6, "Jewelry and watches", R.drawable.jewelry),
    ItemCategory(7, "Toys and hobbies", R.drawable.toys),
    ItemCategory(8, "Office and school", R.drawable.office)
)





