package com.keeghan.traidr.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProductCard(
    // imageResId: Int,
    productId: Int,
    onCardClick: (Int) -> Unit,
    type: String,
    price: String,
    title: String,
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.padding(16.dp)
            .height(70.dp)  //width delegated to lazyList
            .width(60.dp)  //width delegated to lazyList
            .clickable {
                onCardClick(productId)
            }
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Image(
//                    painter = painterResource(id = imageResId),
//                    contentDescription = "Item",
//                    modifier = Modifier
//                        .height(50.dp)
//                        .width(50.dp)
//                )
                Text(
                    text = productId.toString(),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 13.sp,
                    )
                )
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 13.sp,
                    )
                )
                Text(
                    text = price,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 13.sp,
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewProductCard() {
    ProductCard(
        //   imageResId = com.keeghan.traidr.R.drawable.office,
        2,
        {},
        type = "Beauty and personal care",
        price = "34",
        title = "pepper"
    )
}