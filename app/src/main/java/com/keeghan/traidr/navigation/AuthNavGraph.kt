package com.keeghan.traidr.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.keeghan.traidr.navigation.AuthScreen.*
import com.keeghan.traidr.ui.screens.LoginScreen
import com.keeghan.traidr.ui.screens.ResetScreen
import com.keeghan.traidr.ui.screens.SignUpScreen

/**
 * AuthNavGraph with contains Login,SignUp and Reset Screens
 * This is contained with the RootNav AlongSide the MainScreen
 * */
fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = Login.route
    ) {
        composable(route = Login.route) {
            LoginScreen(onLoginClick = {
                if (it){
                    navController.navigate(Graph.MAIN) {
                        popUpTo(Login.route) { inclusive = true }
                    }
                }
            },
                onSignUpClick = {
                    navController.navigate(SignUp.route)
                })
        }
        composable(route = SignUp.route) {
            SignUpScreen(
                onSignUpClick = {
                    navController.navigate(Graph.MAIN)
                },
                onLoginClick = {
                    navController.navigate(Login.route)  {
                        popUpTo(SignUp.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = Reset.route) {
            ResetScreen()
        }
    }
}


sealed class AuthScreen(val route: String) {
    object Login : AuthScreen("login_screen")
    object SignUp : AuthScreen("signup_screen")
    object Reset : AuthScreen("reset_screen")
}
