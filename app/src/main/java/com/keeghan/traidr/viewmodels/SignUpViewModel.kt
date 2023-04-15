package com.keeghan.traidr.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keeghan.traidr.models.user.UserResponse
import com.keeghan.traidr.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @Named("mainDispatcher") private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _user = MutableLiveData<UserResponse>()
    //val user: LiveData<UserResponse> = _user

    private val _isSignUpSuccess = MutableLiveData(false)
    val isSignUpSuccess: LiveData<Boolean> = _isSignUpSuccess

    private var _errorMsg = MutableLiveData("")
    var errorMsg: LiveData<String> = _errorMsg


    fun signUpWithEmail(email: String, password: String) {
        _errorMsg.postValue("")
        viewModelScope.launch(dispatcher) {
            try {
                val response = userRepository.createUserWithEmail(email, password)
                if (response.isSuccessful) {
                    //Assign New User
                    _user.value = response.body()
                    _isSignUpSuccess.postValue(true)
                } else {
                    var msg = logErrorMsg(response)!!
                    if (msg.contains("has already")) {
                        msg = "Account already Exists"
                    }
                    _errorMsg.postValue(msg)
                    _isSignUpSuccess.postValue(false)
                }
            } catch (e: Exception) {
                val msg = when {
                    e.message!!.contains("timeout") -> "Server timeout, please try again"
                    e.message!!.contains("Unable to resolve host") -> "Check Internet Connection"
                    else -> e.message.toString()
                }
                _isSignUpSuccess.postValue(false)
                _errorMsg.postValue(msg)
                Log.d("NetworkException", "Caught $e")
            }
        }
    }
}

fun logErrorMsg(response: Response<UserResponse>): String? {
    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
    val errorMessage = jsonObj.getJSONObject("errors")
        .getJSONArray("email")
        .getString(0)
    Log.d("Error-Body", errorMessage)
    Log.d("Error-Code", response.code().toString())
    return errorMessage
}