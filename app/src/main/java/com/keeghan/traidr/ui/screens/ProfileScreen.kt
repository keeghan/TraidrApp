package com.keeghan.traidr.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.keeghan.traidr.utils.Auth
import com.keeghan.traidr.viewmodels.LogOutViewModel
import kotlinx.coroutines.launch

//Decide on whether to delete settings screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: LogOutViewModel = hiltViewModel(),
    onMyAdsClick: () -> Unit,
    onSignOutClick: () -> Unit,
) {
    val context = LocalContext.current
    val auth = Auth(context)

    val errorMsg by viewModel.errorMsg.observeAsState("")
    val isSignOutSuccess by viewModel.signOutState.observeAsState(false)

    var logOutLoading by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

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
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                scope.launch {
                    sheetState.expand()
                }
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
            Button(onClick = {
                onMyAdsClick()
            }) {
                Text(text = "My Ads")
            }
        }
    }

    //Show settings or myAds on bottomScreen Depending on which button in clicked
    if (sheetState.isVisible) {
        ModalBottomSheet(sheetState = sheetState, onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }
        }) {
            Column(
                Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SettingsScreen()
            }
        }
    }
}
