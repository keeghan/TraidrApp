package com.keeghan.traidr.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keeghan.traidr.models.product.ProductResponse
import com.keeghan.traidr.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/*
* ViewModel handling operations for a single product
* */
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    @Named("ioDispatcher") private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private var _message: MutableLiveData<String> = MutableLiveData()
    var message: LiveData<String> = _message

    fun getProduct(productId: Int) {
        _message.value = ""
        viewModelScope.launch(dispatcher) {
            try {
                val response = repository.getProduct(productId)
                if (response.isSuccessful) {
                    val product: ProductResponse = response.body()!!
                } else {
                    var msg = response.message()
                    var code = response.code()
                }
            } catch (e: Exception) {
                val msg = e.message.toString()
                _message.value = msg
                if (e.message.toString().contains("timeout")) {
                    _message.postValue("Sever timeout, please try again")
                }
                if (msg.contains("Unable to resolve host")) {
                    _message.postValue("Check Internet Connection")
                }
            }
        }
    }

    fun deleteProduct(id: Int, token: String) {
        _message.value = ""
        viewModelScope.launch(dispatcher) {
            try {
                val response = repository.deleteProduct(token, id)
                if (response.isSuccessful && response.code() == 204) {
                    _message.postValue("success")
                } else {
                    _message.postValue(response.code().toString())
                }
            } catch (e: Exception) {
                val msg = e.message.toString()
                _message.postValue(msg)
                if (msg.contains("timeout")) {
                    _message.postValue("Server timeout, try Again")
                }
            }
        }
    }
}