package com.keeghan.traidr.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.keeghan.traidr.ui.screens.LoginScreen
import com.keeghan.traidr.ui.screens.ResetScreen
import com.keeghan.traidr.ui.screens.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {
        composable(route = AuthScreen.Login.route) {
            LoginScreen(onLoginClick = {
                navController.navigate(Graph.MAIN)
            })
        }
        composable(route = AuthScreen.SignUp.route) {
            SignUpScreen(onSignUpClick = {
                navController.navigate(AuthScreen.Login.route)
            })
        }
        composable(route = AuthScreen.Reset.route) {
            ResetScreen()
        }
    }
}


sealed class AuthScreen(val route: String) {
    object Login : AuthScreen("login_screen")
    object SignUp : AuthScreen("signup_screen")
    object Reset : AuthScreen("reset_screen")
}
