package com.keeghan.traidr

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val icon_focused: ImageVector,
) {

    //for recipe homepage
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Outlined.Home,
        icon_focused = Icons.Filled.Home
    )

    //for recipe homepage
    object Post : BottomBarScreen(
        route = "post",
        title = "Post",
        icon = Icons.Outlined.Add,
        icon_focused = Icons.Filled.Add
    )

    //for plan homepage
    object Profile : BottomBarScreen(
        route = "profile",
        title = "profile",
        icon = Icons.Outlined.Person,
        icon_focused = Icons.Filled.Person
    )
}
