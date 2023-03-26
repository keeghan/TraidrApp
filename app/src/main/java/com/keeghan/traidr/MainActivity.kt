package com.keeghan.traidr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.keeghan.traidr.navigation.RootNavGraph
import com.keeghan.traidr.ui.screens.LoginScreen
import com.keeghan.traidr.ui.screens.SignUpScreen
import com.keeghan.traidr.ui.theme.TraidrTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TraidrTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Storing data into SharedPreferences
                    //  SignUpScreen(this, onSignUpClick = {})
                    RootNavGraph(navController = rememberNavController())
                }
            }
        }
    }
}
