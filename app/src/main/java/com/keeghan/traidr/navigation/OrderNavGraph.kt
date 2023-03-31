package com.keeghan.traidr.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.keeghan.traidr.ui.screens.CategoryScreen
import com.keeghan.traidr.ui.screens.MakeOrderScreen
import com.keeghan.traidr.ui.screens.ViewAdsScreen

/**
* Sub Graph of the HomeScreen, pertaining to making orders
* */
fun NavGraphBuilder.orderNavGraph(navController: NavController) {
    navigation(
        route = Graph.ORDER,
        startDestination = OrderScreen.Categories.route
    ) {
        composable(route = OrderScreen.Categories.route) {
            CategoryScreen()
        }
        composable(route = OrderScreen.ViewAds.route) {
            ViewAdsScreen()
        }
        composable(route = OrderScreen.MakeOrder.route) {
            MakeOrderScreen()
        }
    }
}

sealed class OrderScreen(val route: String) {
    object Categories : OrderScreen(route = "categories")
    object ViewAds : OrderScreen(route = "viewAds")
    object MakeOrder : OrderScreen(route = "makeOrder")
}