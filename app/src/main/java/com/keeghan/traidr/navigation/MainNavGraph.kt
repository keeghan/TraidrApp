package com.keeghan.traidr.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keeghan.traidr.BottomBarScreen
import com.keeghan.traidr.navigation.OrderScreen.ViewAds
import com.keeghan.traidr.ui.screens.HomeScreen
import com.keeghan.traidr.ui.screens.PostScreen
import com.keeghan.traidr.ui.screens.ProfileScreen
import com.keeghan.traidr.ui.screens.SettingsScreen


/**
 * Graph for bottomNavigation destinations and SettingsScreen
 **/
@Composable
fun MainNavGraph(
    paddingValues: PaddingValues,
    navController: NavHostController,
    onSignOut: () -> Unit,
) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen() { categoryId ->
                navController.navigate("${ViewAds.route}/$categoryId")  //Reference nested destination directly
            }
        }
        composable(route = BottomBarScreen.Post.route) {
            PostScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(
                onSettingsClick = { navController.navigate(Graph.SETTINGS) },
                onSignOutClick = {
                    onSignOut()
                }
            )

        }
        composable(route = Graph.SETTINGS) {
            SettingsScreen()
        }

        orderNavGraph(navController = navController)   //nested navigation for viewing ads
    }
}