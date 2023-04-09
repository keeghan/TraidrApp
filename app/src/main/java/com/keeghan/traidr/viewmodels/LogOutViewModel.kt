package com.keeghan.traidr.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keeghan.traidr.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LogOutViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @Named("ioDispatcher") private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private var _isSignOutSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var isSignOutSuccess: LiveData<Boolean> = _isSignOutSuccess

    private var _errorMsg: MutableLiveData<String> = MutableLiveData()
    var errorMsg: LiveData<String> = _errorMsg

    private var _successMsg: MutableLiveData<String> = MutableLiveData()
    var successMsg: LiveData<String> = _successMsg


    fun signOut(token: String, Id: Int) {
        viewModelScope.launch(dispatcher) {
            try {
                val response = userRepository.signOut(token, Id)
                if (response.isSuccessful) {
                    val logoutResponse = response.body()
                    _successMsg.postValue(logoutResponse!!.message)
                    _isSignOutSuccess.postValue(true)
                    Log.d("Success", response.body().toString())
                } else {
                    var msg = response.message()
                    _errorMsg.postValue(msg)
                    _isSignOutSuccess.postValue(false)
                }
            } catch (e: Exception) {
                val msg = e.message.toString()
                _isSignOutSuccess.postValue(false)
                _errorMsg.postValue(msg)
                Log.d("NetworkException", "Caught $e")
            }
        }
    }
}