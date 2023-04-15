package com.keeghan.traidr.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keeghan.traidr.models.product.ProductReq
import com.keeghan.traidr.utils.Auth
import com.keeghan.traidr.viewmodels.CreateProductViewModel
import com.keeghan.traidr.viewmodels.ProductsViewModel
import kotlinx.coroutines.launch

@Composable
fun PostScreen(
    viewModel: CreateProductViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val auth = Auth(context)


    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf(0.0) }
    var quantity by remember { mutableStateOf(0) }
    var published by remember { mutableStateOf(false) }

    val validTitle by remember(title) {
        mutableStateOf(title.isNotBlank())
    }
    val validPrice by remember(price) {
        mutableStateOf(price > 0)
    }
    val validQuantity by remember(quantity) {
        mutableStateOf(quantity > 0)
    }

    val formIsValid = validTitle && validPrice && validQuantity

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        if (title.isNotBlank() && !validTitle) {
            Text("Please enter a valid title.", color = MaterialTheme.colorScheme.error)
        }
        TextField(
            value = price.toString(),
            onValueChange = { price = it.toDoubleOrNull() ?: 0.0 },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        if (price > 0 && !validPrice) {
            Text("Please enter a valid price.", color = MaterialTheme.colorScheme.error)
        }
        TextField(
            value = quantity.toString(),
            onValueChange = { quantity = it.toIntOrNull() ?: 0 },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth()
        )
        if (quantity > 0 && !validQuantity) {
            Text("Please enter a valid quantity.", color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = {
                if (formIsValid) {
                    val product = ProductReq.Product(
                        price = price.toString(),
                        published = true.toString(),
                        quantity = quantity.toString(),
                        title = title
                    )

                    coroutineScope.launch {
                        viewModel.createProduct(auth.getToken()!!, ProductReq(product))
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Post Product")
        }
    }
}


//Row(modifier = Modifier.padding(vertical = 8.dp)) {
//            Checkbox(
//                checked = published,
//                onCheckedChange = { published = it },
//                modifier = Modifier.padding(end = 8.dp)
//            )
//            Text("Published")
//        }