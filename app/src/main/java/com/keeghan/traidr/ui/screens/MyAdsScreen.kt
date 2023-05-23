package com.keeghan.traidr.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.keeghan.traidr.utils.Auth


@Composable
fun MyAdsScreen(
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val auth = Auth(context)

    val userId = auth.getUserId().toString()
    val token = auth.getToken().toString()
    AdsScreen(navController, userId = userId, token = token)
}


