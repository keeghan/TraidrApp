package com.keeghan.traidr.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keeghan.traidr.SignUpViewModel
import com.keeghan.traidr.utils.Constants.Companion.CHECK_CAPITAL
import kotlinx.coroutines.launch


const val TAG: String = "SIGNINTAG"

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpClick: (String) -> Unit,
) {
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordErrorState = remember { mutableStateOf(false) }
    val confirmPasswordState = remember { mutableStateOf(TextFieldValue("")) }
    val confirmPasswordErrorState = remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
//    val errorMsg = viewModel.errorMsg.observeAsState()
//    val isSignInSuccess = viewModel.isSignInSuccess.observeAsState()
//    val user = viewModel.user.observeAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
                .padding(bottom = 8.dp),
            isError = passwordErrorState.value
        )
        //Confirm Password
        OutlinedTextField(
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
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            isError = confirmPasswordErrorState.value
        )
        Button(
            //Input verification for both email and password
            onClick = {
                val email = emailState.value.text
                val password = passwordState.value.text
                val confirmPassword = confirmPasswordState.value.text

                formVerification(
                    context,
                    email,
                    password,
                    confirmPassword
                )  //Method might fail and return here
                focusManager.clearFocus()

                // Pass signUp values to server after form verification
                coroutineScope.launch {
                    viewModel.signUpWithEmail(email, password)
                    if (viewModel.isSignInSuccess.value == true) {
                        //signIn Successful
                        //todo: welcome message
                        val username =
                            viewModel.user.value?.data?.attributes!!.email.substringBefore("@")
                        showToast(context, "Welcome, $username")
                    } else {
                        viewModel.errorMsg.value.let {
                            if (it != null) {
                                showToast(context, it)
                            }
                        }
                    }
                }
            }
        ) {
            Text("SignUp")
        }
        Text(text = "Already have an Account",
            modifier = Modifier.clickable { })
    }
}


/*
* Method to verify input
* */
fun formVerification(context: Context, email: String, password: String, confirmPassword: String) {
    if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
        showToast(context, "Please enter email and password")
        return
    }
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        showToast(context, "Enter a valid email")
        return
    }
    if (confirmPassword != password) {
        showToast(context, "Password are not the same")
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


//
//@Preview
//@Composable
//fun SignUpPreview() {
//    SignUpScreen {}
//}

