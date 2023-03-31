package com.keeghan.traidr.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch


const val TAG: String = "SIGNINTAG"

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpClick: (String) -> Unit,
    onLoginClick: () -> Unit,
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
    val coroutineScope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(false) }

//    val errorMsg = viewModel.errorMsg.observeAsState()
//    val isSignUpSuccess = viewModel.isSignUpSuccess.observeAsState()
//    val user = viewModel.user.observeAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (loading) {
            CircularProgressIndicator()
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
                    loading = true
                    focusManager.clearFocus()
                    coroutineScope.launch {
                        val hash = hashPassword(password)
                        viewModel.signUpWithEmail(email, hash)
                        if (viewModel.isSignUpSuccess.value == true) {
                            //signIn Successful
                            loading = false
                            val username =
                                viewModel.user.value?.data?.attributes!!.email.substringBefore("@")
                            showToast(context, "Welcome, $username")
                        } else {
                            loading = false
                            viewModel.errorMsg.value.let {
                                if (it != null) {
                                    if (it.contains("has already")) {
                                        showToast(context, "Account already Exits")
                                    }
                                    if (it.contains("timeout")) {
                                        showToast(context, "Sever timeout, please try again")
                                    }
                                }
                            }
                        }
                    }
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

