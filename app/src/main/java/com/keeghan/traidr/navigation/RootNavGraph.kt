package com.keeghan.traidr.navigation

import android.provider.DocumentsContract.Root
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keeghan.traidr.ui.screens.MainScreen

/**
 * Main Graph, containing AuthGraph, and MainScreen with BottomNavigationBar
* */
@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {
        authNavGraph(navController = navController)
        composable(route = Graph.MAIN) {
            MainScreen()
        }
    }
}

//Contains Top level GraphRoutes
object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN = "main_graph"
    const val ORDER = "order_graph"
    const val SETTINGS = "settings"
}
