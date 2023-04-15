package com.keeghan.traidr.navigation

import android.provider.DocumentsContract.Root
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keeghan.traidr.navigation.AuthScreen.*
import com.keeghan.traidr.ui.screens.MainScreen
import com.keeghan.traidr.utils.Auth

/**
 * Main Graph, containing AuthGraph, and MainScreen with BottomNavigationBar
 * */
@Composable
fun RootNavGraph(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val auth = Auth(context) //Manage Login and SignOuts

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        //Send to main Screen if logged In
        startDestination = if (auth.isLoggedIn()) Graph.MAIN else Graph.AUTHENTICATION
    ) {
        authNavGraph(navController = navController)

        //Hoist SignOut up to it gets to root
        composable(route = Graph.MAIN) {
            MainScreen(onSignOut = {
                navController.navigate(Graph.AUTHENTICATION) {
                    popUpTo(Graph.MAIN) { inclusive = true }
                }
            })
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
