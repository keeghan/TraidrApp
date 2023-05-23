package com.keeghan.traidr.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.ui.SettingsSwitch
import com.keeghan.traidr.utils.SettingsPreferenceManager


@Composable
fun SettingsScreen(
    context: Context = LocalContext.current,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val settingsPref = SettingsPreferenceManager(context)
        val isDarkThemeEnabled = settingsPref.isDarkTheme()
        val state = rememberBooleanSettingState(isDarkThemeEnabled)

        SettingsSwitch(
            Modifier, //todo: user Proper Icons
            icon = { Icon(imageVector = Icons.Filled.Create, contentDescription = "Theme") },
            title = { Text(text = "Change Theme") },
            subtitle = { Text(text = "Restart App to Apply") },
            enabled = true,
            state = state,
            onCheckedChange = {

                settingsPref.saveThemeState(it)
                //state.value = isDarkThemeEnabled
            }
        )
    }
}

