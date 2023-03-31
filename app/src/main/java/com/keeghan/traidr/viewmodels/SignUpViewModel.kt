package com.keeghan.traidr.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keeghan.traidr.models.user.User
import com.keeghan.traidr.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    //  private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _isSignUpSuccess = MutableLiveData(false)
    val isSignUpSuccess: LiveData<Boolean> = _isSignUpSuccess

    private var _errorMsg = MutableLiveData<String>()
    var errorMsg: LiveData<String> = _errorMsg

    suspend fun signUpWithEmail(email: String, password: String) {
        try {
            val response = userRepository.createUserWithEmail(email, password)
            if (response.isSuccessful) {
                //Assign New User
                _user.value = response.body()
                Log.d( "Auth-Token", response.headers()["Authorization"] ?: "null" )
                _isSignUpSuccess.value = true
            } else {
                _errorMsg.value = logErrorMsg(response)!!
                _isSignUpSuccess.value = false
            }
        } catch (e: Exception) {
            _isSignUpSuccess.value = false
            _errorMsg.value = e.message.toString()
            Log.d("NetworkException", "Caught $e")
        }
    }
}

fun logErrorMsg(response: Response<User>): String? {
    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
    val errorMessage = jsonObj.getJSONObject("errors")
        .getJSONArray("email")
        .getString(0)
    Log.d("Error-Body", errorMessage)
    Log.d("Error-Code", response.code().toString())
    return errorMessage
}