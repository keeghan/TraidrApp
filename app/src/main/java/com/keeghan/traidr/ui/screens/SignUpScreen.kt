package com.keeghan.traidr.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keeghan.traidr.R
import com.keeghan.traidr.utils.Constants.Companion.CHECK_CAPITAL
import com.keeghan.traidr.utils.hashPassword
import com.keeghan.traidr.viewmodels.SignUpViewModel

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpClick: (Boolean) -> Unit,
    onLoginClick: () -> Unit,    //User without account
) {
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordErrorState = remember { mutableStateOf(false) }
    val confirmPasswordState = remember { mutableStateOf(TextFieldValue("")) }
    val confirmPasswordErrorState = remember { mutableStateOf(false) }

    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordVisible = remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var loading by remember { mutableStateOf(false) }

    val errorMsg by viewModel.errorMsg.observeAsState("")
    val isSignUpSuccess by viewModel.isSignUpSuccess.observeAsState(false)



    LaunchedEffect(isSignUpSuccess) {
        if (isSignUpSuccess) {
            loading = false
            showToast(context, "Account Created")
            onSignUpClick(true)
        } else {
            loading = false
        }
    }

    LaunchedEffect(errorMsg) {
        if (errorMsg.isNotEmpty()) {
            if (errorMsg != "") {
                loading = false
                showToast(context, errorMsg)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        if (loading) {
            CircularProgressIndicator() //indicate progress
        }
        //email
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
        //Password
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
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
            },
            isError = passwordErrorState.value
        )
        //Confirm Password
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            value = confirmPasswordState.value,
            onValueChange = {
                confirmPasswordState.value = it
                confirmPasswordErrorState.value = it.text.length < 8 ||
                        !it.text.contains(Regex(CHECK_CAPITAL)) ||
                        it.text != passwordState.value.text
            },
            label = { Text("Confirm Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (confirmPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    confirmPasswordVisible.value = !confirmPasswordVisible.value
                }) {
                    Icon(
                        painter = if (confirmPasswordVisible.value) painterResource(id = R.drawable.baseline_visibility_off_24)
                        else painterResource(id = R.drawable.baseline_visibility_24),
                        contentDescription = if (confirmPasswordVisible.value) "Hide password" else "Show password"
                    )
                }
            },
            isError = confirmPasswordErrorState.value
        )
        Button(
            //Input verification for both email and password
            onClick = {
                val email = emailState.value.text
                val password = passwordState.value.text
                val confirmPassword = confirmPasswordState.value.text

                val isVerified = formVerification(
                    context = context,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword
                )

                if (isVerified) {
                    loading = true   //Spinning Icon state
                    focusManager.clearFocus()
                    val hash = hashPassword(password)
                    viewModel.signUpWithEmail(email, hash)
                } //end of ifVerified
            }
        ) {
            Text("SignUp")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Already have an Account",
            modifier = Modifier.clickable {
                onLoginClick()
            })
    }
}


/*
* Method to verify input
* */
fun formVerification(
    context: Context,
    email: String,
    password: String,
    confirmPassword: String,
): Boolean {
    if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
        showToast(context, "Please enter email and password")
        return false
    }
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        showToast(context, "Enter a valid email")
        return false
    }
    if (confirmPassword != password) {
        showToast(context, "Password are not the same")
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


//
//@Preview
//@Composable
//fun SignUpPreview() {
//    SignUpScreen {}
//}

