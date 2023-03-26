package com.keeghan.traidr.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keeghan.traidr.BottomBarScreen
import com.keeghan.traidr.ui.screens.HomeScreen
import com.keeghan.traidr.ui.screens.PostScreen
import com.keeghan.traidr.ui.screens.ProfileScreen

@Composable
fun MainNavGraph(
    paddingValues: PaddingValues,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen()
        }
        composable(route = BottomBarScreen.Post.route) {
            PostScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
    }
}