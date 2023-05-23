package com.keeghan.traidr.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keeghan.traidr.models.product.ProductReq
import com.keeghan.traidr.models.product.ProductReqRes
import com.keeghan.traidr.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    @Named("ioDispatcher") private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _message: MutableLiveData<String> = MutableLiveData()
    var message: LiveData<String> = _message

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun createProduct(token: String, productReq: ProductReq) {
        _isLoading.value = true
        _message.value = ""
        viewModelScope.launch(dispatcher) {
            try {
                val response = repository.createProduct(token, productReq)
                if (response.isSuccessful) {
                    val productReqRes = response.body()!!
                    _message.postValue("success")
                } else {
                    if (response.code() == 500){
                        _message.postValue("Server Error")
                    }
                    _message.postValue(response.code().toString())
                }
            } catch (e: Exception) {
                e.message?.let { msg ->
                    _message.postValue(msg)
                    when {
                        msg.contains("timeout") -> _message.postValue("Server timeout, please reload")
                        msg.contains("Unable to resolve") -> _message.postValue("Check internet connection")
                    }
                }
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
