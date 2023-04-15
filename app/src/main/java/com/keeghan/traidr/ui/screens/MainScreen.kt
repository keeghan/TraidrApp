package com.keeghan.traidr.ui.screens

import android.app.Activity
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keeghan.traidr.BottomBarScreen
import com.keeghan.traidr.navigation.MainNavGraph

/**
 * Main Screen composable, containing BottomNavigationBar
 * And hosts the MainNavGraph with the Home, Post, Profile Screens
 * */
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    onSignOut: () -> Unit,
) {
    //Kill activity when back is pressed (Quick Theme Change)
    val backPressedCallback = object : OnBackPressedCallback(true) {
        val context = LocalContext.current
        override fun handleOnBackPressed() {
            // Kill the app when the back button is pressed
            (context as? Activity)?.finishAffinity()

        }
    }
    // Add the OnBackPressedCallback to the back press dispatcher
    LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher?.addCallback(
        backPressedCallback
    )

    Scaffold(bottomBar = { BottomBar(navController) }) {
        MainNavGraph(it, navController) {
            onSignOut()
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Post,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            val icon =
                if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                    screen.icon_focused
                } else {
                    screen.icon
                }
            Icon(
                imageVector = icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        // unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        colors = NavigationBarItemDefaults.colors(
            unselectedIconColor = LocalContentColor.current.copy(alpha = 0.50f)
        ),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}

