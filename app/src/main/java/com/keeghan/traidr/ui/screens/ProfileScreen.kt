package com.keeghan.traidr.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.keeghan.traidr.utils.Auth
import com.keeghan.traidr.viewmodels.LogOutViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: LogOutViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit,
    onSignOutClick: () -> Unit,
) {
    val context = LocalContext.current
    val auth = Auth(context)


    val errorMsg by viewModel.errorMsg.observeAsState("")
    val isSignOutSuccess by viewModel.isSignOutSuccess.observeAsState(false)
    val coroutineScope = rememberCoroutineScope()
    var errorMsgShown by remember { mutableStateOf(false) }

    LaunchedEffect(isSignOutSuccess) {
        if (isSignOutSuccess) {
            showToast(context, "Signing Out")
            auth.logOut()
            onSignOutClick()
        }
    }

    LaunchedEffect(errorMsg) {
        errorMsgShown = true //change errorMsgState once shown
        if (errorMsg != "") {
            showToast(context, errorMsg)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Profile")
        Button(onClick = {
            onSettingsClick()
        }) {
            Text(text = "Settings")
        }
        //signOut
        Button(onClick = {
            errorMsgShown = false
            val userId = auth.getUserId()
            val token = auth.getToken()
            if (token != null) {
                viewModel.signOut(token, userId)
            }
            coroutineScope.launch {
                delay(10000)
                if (!errorMsgShown) { //if errorMsg is not shown , show it again
                    showToast(context, errorMsg)
                    errorMsgShown = true
                }
            }
        }) {
            Text(text = "Sign Out")
        }
    }
}