package com.keeghan.traidr.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keeghan.traidr.utils.Auth
import com.keeghan.traidr.viewmodels.LogOutViewModel

@Composable
fun ProfileScreen(
    viewModel: LogOutViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit,
    onSignOutClick: () -> Unit,
) {
    val context = LocalContext.current
    val auth = Auth(context)

    val errorMsg by viewModel.errorMsg.observeAsState("")
    val isSignOutSuccess by viewModel.signOutState.observeAsState(false)

    var logOutLoading by remember { mutableStateOf(false) }

    LaunchedEffect(isSignOutSuccess) {
        if (isSignOutSuccess) {
            showToast(context, "Signing Out")
            auth.logOut()
            onSignOutClick()
        }
    }

    LaunchedEffect(errorMsg) {
        if (errorMsg.isNotEmpty() && errorMsg != "") {
            showToast(context, errorMsg)
            logOutLoading = false
        }
    }

    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (logOutLoading) {
            CircularProgressIndicator()
        }
        Text(text = "Profile")
        Button(onClick = {
            onSettingsClick()
        }) {
            Text(text = "Settings")
        }
        //signOut
        Button(onClick = {
            logOutLoading = true
            val userId = auth.getUserId()
            val token = auth.getToken()
            viewModel.signOut(token!!, userId)
        }) {
            Text(text = "Sign Out")
        }
    }
}