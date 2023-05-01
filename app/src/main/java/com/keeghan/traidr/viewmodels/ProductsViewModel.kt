package com.keeghan.traidr.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keeghan.traidr.models.product.ProductsResponse
import com.keeghan.traidr.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductRepository,
    @Named("ioDispatcher") private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _errorMsg: MutableLiveData<String> = MutableLiveData()
    var errorMsg: LiveData<String> = _errorMsg

    private var _allProductsRes: MutableLiveData<ProductsResponse> = MutableLiveData()
    var allProductsRes: LiveData<ProductsResponse> = _allProductsRes


    fun getAllProducts(url: String = "products") {
        _isLoading.postValue(true)
        _errorMsg.postValue("")
        viewModelScope.launch(dispatcher) {
            try {
                val response = repository.getAllProduct(url)
                if (response.isSuccessful) {
                    _allProductsRes.postValue(response.body())
                } else {
                    _errorMsg.postValue(response.code().toString())
                }
            } catch (e: Exception) {
                e.message?.let { msg ->
                    _errorMsg.postValue(msg)
                    when {
                        msg.contains("timeout") -> _errorMsg.postValue("Server timeout, please reload")
                        msg.contains("Unable to resolve") -> _errorMsg.postValue("Check internet connection")
                    }
                }
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

}