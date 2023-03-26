package com.keeghan.traidr

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keeghan.traidr.models.user.User
import com.keeghan.traidr.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    //  private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _isSignInSuccess = MutableLiveData(false)
    val isSignInSuccess: LiveData<Boolean> = _isSignInSuccess

    private var _errorMsg = MutableLiveData<String>()
    var errorMsg: LiveData<String> = _errorMsg

    suspend fun signUpWithEmail(email: String, password: String) {
        try {
            val signUpResponse = userRepository.createUserWithEmail(email, password)
            if (signUpResponse.isSuccessful) {
                //Assign New User
                _user.value = signUpResponse.body()
                Log.d("Success-Body", _user.value?.data?.attributes!!.email.substringBefore("@"))
                _isSignInSuccess.value = true
            } else {
                val jsonObj = JSONObject(signUpResponse.errorBody()!!.charStream().readText())
                val errorMessage = jsonObj.getJSONObject("errors")
                    .getJSONArray("email")
                    .getString(0)
                Log.d("Error-Body", errorMessage)
                Log.d("Error-Code", signUpResponse.code().toString())

                _errorMsg.value = errorMessage
                _isSignInSuccess.value = false
            }
        } catch (e: Exception) {
            _isSignInSuccess.value = false
            _errorMsg.value = e.message.toString()
            Log.d("NetworkException", "Caught $e")
        }
    }
}