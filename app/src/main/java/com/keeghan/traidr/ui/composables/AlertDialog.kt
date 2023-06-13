package com.keeghan.traidr.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keeghan.traidr.viewmodels.ProductsViewModel

@SuppressLint("AutoboxingStateValueProperty")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogBox(
    showDeleteDialog: MutableState<Boolean>,
    deleteProductId: MutableIntState,
    token: String,
    productsViewModel: ProductsViewModel
) {
    AlertDialog(onDismissRequest = { showDeleteDialog.value = false }) {
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Text(text = "close",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp)
                            .clickable {
                                showDeleteDialog.value = false
                            })
                    Text(
                        text = "confirm",
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp)
                            .clickable {
                                productsViewModel.deleteProduct(
                                    deleteProductId.value, token
                                )
                                deleteProductId.value = 0
                                showDeleteDialog.value = false
                            },
                        color = MaterialTheme.colors.primary
                    )
                }

            }
        }
    }

}