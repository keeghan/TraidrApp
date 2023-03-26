package com.keeghan.traidr.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keeghan.traidr.utils.Constants.Companion.CHECK_CAPITAL
import com.keeghan.traidr.utils.Constants.Companion.CHECK_SYMBOL_DIGIT


@Composable
fun LoginScreen(onLoginClick: (String) -> Unit) {
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordErrorState = remember { mutableStateOf(false) }


    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = emailState.value,
            onValueChange = {
                emailState.value = it
                emailErrorState.value =
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(it.text).matches()
            },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            isError = emailErrorState.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = {
                passwordState.value = it
                passwordErrorState.value = it.text.length < 8 ||
                        !it.text.contains(Regex(CHECK_CAPITAL))
            },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            isError = passwordErrorState.value
        )
        Button(
            //Input verification for both email and password
            onClick = {
                val email = emailState.value.text
                val password = passwordState.value.text

              //  loginFormVerification(context, email, password)
                // Pass login values to server after verification
                focusManager.clearFocus()
                onLoginClick(email)
                showToast(context, "Login successful")
            }
        ) {
            Text("Login")
        }
    }
}

fun showToast(context: Context, msg: String) =
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

fun loginFormVerification(context: Context, email: String, password: String) {
    if (email.isBlank() || password.isBlank()) {
        showToast(context, "Please enter email and password")
        return
    }

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        showToast(context, "Enter a valid email")
        return
    }

    if (password.length < 8) {
        showToast(context, "Password must contain at least 8 characters")
        return
    }
    if (!password.contains(Regex(CHECK_CAPITAL))) {
        showToast(context, "Password must contain at least one uppercase character")
        return
    }
//    if (!password.contains(Regex(CHECK_SYMBOL_DIGIT))) {
//        showToast(context, "Password must contain at least one symbol or digit")
//        return
//    }
}

@Preview
@Composable
fun Preview() {
    LoginScreen(onLoginClick = {})
}
