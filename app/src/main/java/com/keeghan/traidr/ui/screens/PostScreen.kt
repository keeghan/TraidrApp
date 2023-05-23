package com.keeghan.traidr.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.keeghan.traidr.models.product.ProductReq
import com.keeghan.traidr.utils.Auth
import com.keeghan.traidr.viewmodels.CreateProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: CreateProductViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val auth = Auth(context)

    val message by viewModel.message.observeAsState("")

    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf(0.0) }
    var quantity by remember { mutableStateOf(0) }
    // var published by remember { mutableStateOf(false) }

    val validTitle by remember(title) {
        mutableStateOf(title.isNotBlank())
    }
    val validPrice by remember(price) {
        mutableStateOf(price > 0)
    }
    val validQuantity by remember(quantity) {
        mutableStateOf(quantity > 0)
    }

    val isLoading by remember { viewModel.isLoading }.observeAsState(false)
    val formIsValid = validTitle && validPrice && validQuantity

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            if (message != "") {
                showToast(context, message)
            }
        }
    }

    Scaffold(
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
                if (isLoading) {
                    CircularProgressIndicator()
                }
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                if (title.isNotBlank() && !validTitle) {
                    Text("Please enter a valid title.", color = MaterialTheme.colorScheme.error)
                }
                OutlinedTextField(
                    value = price.toString(),
                    onValueChange = { price = it.toDoubleOrNull() ?: 0.0 },
                    label = { Text("Price") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                if (price > 0 && !validPrice) {
                    Text("Please enter a valid price.", color = MaterialTheme.colorScheme.error)
                }
                OutlinedTextField(
                    value = quantity.toString(),
                    onValueChange = { quantity = it.toIntOrNull() ?: 0 },
                    label = { Text("Quantity") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
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
                    Text("Post")
                }
            }
        }
    }
}