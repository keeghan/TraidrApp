package com.keeghan.traidr.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.keeghan.traidr.navigation.OrderScreen.MakeOrder
import com.keeghan.traidr.navigation.OrderScreen.ViewAds
import com.keeghan.traidr.ui.screens.MakeOrderScreen
import com.keeghan.traidr.ui.screens.ViewAdsScreen

/**
 * Sub Graph of the HomeScreen, pertaining to making orders
 * */
fun NavGraphBuilder.orderNavGraph(navController: NavController) {
    navigation(
        route = Graph.ORDER,
        startDestination = ViewAds.route
    ) {
//        composable(route = OrderScreen.Categories.route) {
//            CategoryScreen()
//        }
        //Receive category id from HomeScreen and display ads in that category
        composable(
            route = "${ViewAds.route}/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) {
            ViewAdsScreen(categoryId = it.arguments?.getInt("categoryId")!!)
        }
        composable(route = MakeOrder.route) {
            MakeOrderScreen()
        }
    }
}

sealed class OrderScreen(val route: String) {
    object Categories : OrderScreen(route = "categories")
    object ViewAds : OrderScreen(route = "viewAds")
    object MakeOrder : OrderScreen(route = "makeOrder")
}