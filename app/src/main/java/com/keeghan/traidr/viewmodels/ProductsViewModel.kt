package com.keeghan.traidr.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keeghan.traidr.models.product.Product
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

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _errorMsg: MutableLiveData<String> = MutableLiveData()
    var errorMsg: LiveData<String> = _errorMsg

    var allProductsRes: MutableLiveData<ProductsResponse> = MutableLiveData()

    // var allProducts: LiveData<List<Product>> = _allProducts.value?.data!!
    var allProducts: MutableLiveData<List<Product>> = MutableLiveData()


    fun getAllProduct() {
        viewModelScope.launch(dispatcher) {
            try {
                _isLoading.postValue(true)
                val response = repository.getAllProduct()
                if (response.isSuccessful) {
                    allProductsRes.postValue(response.body())
                    allProducts.postValue(allProductsRes.value?.data!!)
                    _isLoading.postValue(false)

                } else {
                    var msg = response.message()
                    var code = response.code()
                    _isLoading.postValue(false)
                }
            } catch (e: Exception) {
                _isLoading.postValue(false)
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