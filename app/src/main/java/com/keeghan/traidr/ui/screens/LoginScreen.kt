package com.keeghan.traidr.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keeghan.traidr.R
import com.keeghan.traidr.utils.Auth
import com.keeghan.traidr.utils.Constants.Companion.CHECK_CAPITAL
import com.keeghan.traidr.utils.hashPassword
import com.keeghan.traidr.viewmodels.LoginViewModel


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginClick: (Boolean) -> Unit,
    onSignUpClick: () -> Unit,  //User without account
) {
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordErrorState = remember { mutableStateOf(false) }
    val passwordVisible = remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var loading by remember { mutableStateOf(false) }

    val isSuccess by viewModel.isSignInSuccess.observeAsState(false)
    val errorMsg by viewModel.errorMsg.observeAsState("")

    val auth = Auth(context)

    //On successful login, Store values in Preference using auth
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            loading = false
            val token = viewModel.userCred.value!!.token
            val userId = viewModel.userCred.value!!.id
            auth.logIn(token, userId)
            onLoginClick(true)
        } else {
            loading = false
        }
    }

    LaunchedEffect(errorMsg) {
        if (errorMsg.isNotEmpty()) {
            if (errorMsg != "") {
                showToast(context, errorMsg)
                loading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (loading) {
            CircularProgressIndicator()
        }
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
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            isError = passwordErrorState.value,
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisible.value = !passwordVisible.value
                }) {
                    Icon(
                        painter = if (passwordVisible.value) painterResource(id = R.drawable.baseline_visibility_off_24)
                        else painterResource(id = R.drawable.baseline_visibility_24),
                        contentDescription = if (passwordVisible.value) "Hide password" else "Show password"
                    )
                }
            }
        )
        Button(
            //Input verification for both email and password
            onClick = {
                val email = emailState.value.text
                val password = passwordState.value.text
                val isVerified = loginFormVerification(context, email, password)

                if (isVerified) {
                    loading = true
                    focusManager.clearFocus()

                    val hash = hashPassword(password)
                    viewModel.logInWithEmail(email, hash)
                }
            }
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Don't Have an Account?", Modifier.clickable {
            onSignUpClick()
        })
    }
}

//Toast Function
fun showToast(context: Context, msg: String) =
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

//Text Validation for email and Password
fun loginFormVerification(context: Context, email: String, password: String): Boolean {
    if (email.isBlank() || password.isBlank()) {
        showToast(context, "Please enter email and password")
        return false
    }
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        showToast(context, "Enter a valid email")
        return false
    }
    if (password.length < 8) {
        showToast(context, "Password must contain at least 8 characters")
        return false
    }
    if (!password.contains(Regex(CHECK_CAPITAL))) {
        showToast(context, "Password must contain at least one uppercase character")
        return false
    }
    return true
}

@Preview
@Composable
fun Preview() {
    LoginScreen(onLoginClick = {}) {}
}


