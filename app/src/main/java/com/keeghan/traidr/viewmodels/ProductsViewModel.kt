package com.keeghan.traidr.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keeghan.traidr.models.product.ProductResponse
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

    private var _deleteTracker = MutableLiveData(false)
    val deleteTracker: LiveData<Boolean> = _deleteTracker

    private var _message: MutableLiveData<String> = MutableLiveData()
    var message: LiveData<String> = _message

    private var _allProductsRes: MutableLiveData<ProductsResponse> = MutableLiveData()
    var allProductsRes: LiveData<ProductsResponse> = _allProductsRes


    fun getAllProducts(url: String = "products") {
        _isLoading.value = true
        _message.value = ""
        viewModelScope.launch(dispatcher) {
            try {
                val response = repository.getAllProduct(url)
                if (response.isSuccessful) {
                    _allProductsRes.postValue(response.body())
                } else {
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
                //Cleanup deleteTracker because when all products is loading
                //deletion cannot be done, or at least tried
                _isLoading.postValue(false)
                _deleteTracker.postValue(false)
            }
        }
    }

    fun getProduct(productId: Int) {
        _message.value = ""
        viewModelScope.launch(dispatcher) {
            try {
                val response = repository.getProduct(productId)
                if (response.isSuccessful) {
                    val product: ProductResponse = response.body()!!
                } else {
                    _message.postValue(response.code().toString())
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
            } finally {
               _deleteTracker.postValue(true)  //trigger reload after product deletion
            }
        }
    }


}