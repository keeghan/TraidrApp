package com.keeghan.traidr.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.keeghan.traidr.models.user.loginUser.UserCredentials
import com.keeghan.traidr.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @Named("mainDispatcher") private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private var _userCred: MutableLiveData<UserCredentials> = MutableLiveData()
    var userCred: LiveData<UserCredentials> = _userCred

    private val _isSignInSuccess = MutableLiveData(false)
    val isSignInSuccess: LiveData<Boolean> = _isSignInSuccess

    private var _errorMsg = MutableLiveData<String>()
    var errorMsg: LiveData<String> = _errorMsg

    fun logInWithEmail(email: String, password: String) {
        _errorMsg.postValue("")   //reset msg in case error appear in succession
        viewModelScope.launch(dispatcher) {
            try {
                val response = userRepository.loginWithEmail(email, password)
                if (response.isSuccessful) {
                    _userCred.value = response.body()
                    _isSignInSuccess.postValue(true)
                    Log.d("Auth-Token", _userCred.value!!.token)
                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "Check email or Password"
                        else -> response.code().toString()
                    }
                    _errorMsg.postValue(errorMsg)
                    _isSignInSuccess.postValue(false)
                }
            } catch (e: Exception) {
                val msg = when {
                    e.message!!.contains("timeout") -> "Server timeout, please try again"
                    e.message!!.contains("Unable to resolve host") -> "Check Internet Connection"
                    else -> e.message.toString()
                }
                _errorMsg.postValue(msg)
                _isSignInSuccess.postValue(false)

                Log.d("NetworkException", "Caught $e")
            }
        }
    }
}
