package com.keeghan.traidr.viewmodels

import android.util.Log
import androidx.lifecycle.*
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
    private var _token: MutableLiveData<String> = MutableLiveData()
    var token: LiveData<String> = _token

    private var _userId: MutableLiveData<Int> = MutableLiveData()
    var userId: MutableLiveData<Int> = _userId

    private val _isSignInSuccess = MutableLiveData(false)
    val isSignInSuccess: LiveData<Boolean> = _isSignInSuccess

    private var _errorMsg = MutableLiveData<String>()
    var errorMsg: LiveData<String> = _errorMsg

    fun logInWithEmail(email: String, password: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val response = userRepository.loginWithEmail(email, password)
                if (response.isSuccessful) {
                    val userCred = response.body()
                    _token.value = userCred!!.token
                    _userId.value = userCred.id
                    _isSignInSuccess.postValue(true)
                    Log.d("Auth-Token", userCred.token)
                } else {
                    _errorMsg.postValue(response.code().toString())
                    if (response.code() == 401) {
                        _errorMsg.postValue("Check email or Password")
                    }
                    _isSignInSuccess.postValue(false)
                }
            } catch (e: Exception) {
                _isSignInSuccess.postValue(false)
                _errorMsg.postValue(e.message.toString())
                Log.d("NetworkException", "Caught $e")
            }
        }
    }
}

//fun logErrorMsg1(_userCred: Response<UserCredentials>): String? {
//    val jsonObj = JSONObject(_userCred.errorBody()!!.charStream().readText())
//    val errorMessage = jsonObj.getJSONObject("errors")
//        .getJSONArray("email")
//        .getString(0)
//    Log.d("Error-Body", errorMessage)
//    Log.d("Error-Code", _userCred.code().toString())
//    return errorMessage
//}
