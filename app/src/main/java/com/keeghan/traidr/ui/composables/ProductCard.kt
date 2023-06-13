package com.keeghan.traidr.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.keeghan.traidr.R
import java.net.URI

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
        // shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .clickable { onCardClick(productId) }
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp))
    ) {
        Column(
        ) {
            AsyncImage(  //Coil to load images
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                    "https://images.unsplash.com/photo-1605541885855-88863971e7b0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8fHx8fHx8MTY4NTYyNDQ2Nw&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080"
                    )  //placeholder
                    .crossfade(true)
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                error = (painterResource(R.drawable.home)),
                placeholder = painterResource(R.drawable.home),
                contentDescription = title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp)
            )
            Column(Modifier.padding(start = 5.dp)) {
                Text(
                    text = productId.toString(), textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        lineHeight = 13.sp,
                    )
                )
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 13.sp,
                    )
                )
                Text(
                    text = "GHC $price",
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 13.sp,
                    )
                )
                Spacer(modifier = Modifier.padding(bottom = 2.dp))
            }
        }
    }
}


@Preview
@Composable
fun PreviewProductCard() {
    ProductCard(
        //   imageResId = com.keeghan.traidr.R.drawable.office,
        2, {}, type = "Beauty and personal care", price = "34", title = "pepper"
    )
}


