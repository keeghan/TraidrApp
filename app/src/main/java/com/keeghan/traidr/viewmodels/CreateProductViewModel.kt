package com.keeghan.traidr.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keeghan.traidr.models.product.ProductReqRes
import com.keeghan.traidr.models.product.ProductReq
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

    private var _errorMsg: MutableLiveData<String> = MutableLiveData()
    var errorMsg: LiveData<String> = _errorMsg

    private var _productRes: MutableLiveData<ProductReqRes> = MutableLiveData()
    var productRes: LiveData<ProductReqRes> = _productRes

    fun createProduct(token: String, productReq: ProductReq) {
        viewModelScope.launch(dispatcher) {
            try {
                val response = repository.createProduct(token, productReq)
                if (response.isSuccessful) {
                    val productReqRes = response.body()!!
                } else {
                    var msg = response.message()
                    var code = response.code()
                }
            } catch (e: Exception) {
                val msg = e.message.toString()
                _errorMsg.postValue(msg)
                if (e.message.toString().contains("timeout")) {
                    _errorMsg.postValue("Sever timeout, please try again")
                }
                if (msg.contains("Unable to resolve host")) {
                    _errorMsg.postValue("Check Internet Connection")
                }
            }
        }
    }
}