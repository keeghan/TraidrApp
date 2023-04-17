package com.keeghan.traidr.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
            .clickable {
                onCardClick(productId)
            }
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp))
    ) {
        Column(
        ) {
            AsyncImage(  //Coil to load images
                model = ImageRequest.Builder(LocalContext.current).data("recipe.imageUrl")
                    .crossfade(true).networkCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED).memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED).build(),
                placeholder = painterResource(R.drawable.standin),
                error = painterResource(R.drawable.standin),
                contentDescription = title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 250.dp)
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


