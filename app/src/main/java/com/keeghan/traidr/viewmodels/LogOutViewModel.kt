package com.keeghan.traidr.viewmodels

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
    private var _signOutState: MutableLiveData<Boolean> = MutableLiveData(false)
    var signOutState: LiveData<Boolean> = _signOutState

    private var _errorMsg: MutableLiveData<String> = MutableLiveData()
    var errorMsg: LiveData<String> = _errorMsg

    fun signOut(token: String, Id: Int) {
        _errorMsg.value = ""
        viewModelScope.launch(dispatcher) {
            try {
                val response = userRepository.signOut(token, Id)
                if (response.isSuccessful) {
                    _signOutState.postValue(true)
                } else {
                    _errorMsg.postValue(response.message())
                    _signOutState.postValue(false)
                }
            } catch (e: Exception) {
                val msg = e.message.toString()
                _errorMsg.postValue(msg)
                if (msg.contains("timeout")) {
                    _errorMsg.postValue("Server timeout, try Again")
                }
                _signOutState.postValue(false)
            }
        }
    }
}