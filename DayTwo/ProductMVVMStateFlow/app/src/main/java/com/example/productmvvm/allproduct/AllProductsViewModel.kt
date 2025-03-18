package com.example.productmvvm.allproduct

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.productappmvvm.repositoty.ProductRepository
import com.example.productmvvm.data.models.Product
import com.example.productmvvm.data.models.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllProductsViewModel(private val repo: ProductRepository) : ViewModel() {
    private var _productList = MutableStateFlow<ResponseState>(ResponseState.Loading)
    val products = _productList.asStateFlow()


    private val _mutableMessage = MutableSharedFlow<String>()
    val message = _mutableMessage.asSharedFlow()

    init {
        getProducts()
    }

    fun addToFavourites(product: Product?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (product != null) {
                try {
                    val result = repo.addProductToFavourite(product)
                    if (result > 0) {
                        _mutableMessage.emit("Added To Fav!2")
                        delay(500)
                    } else {
                        _mutableMessage.emit("Product already exist")
                        delay(500)
                    }
                } catch (e: Exception) {
                    _mutableMessage.emit("Can not add Product ${e.message}")
                }

            } else {
                _mutableMessage.emit("Can not add Product, no data to add")
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repo.getAllProduct(true)
                delay(1000)
                if (result != null) {
                    result.catch { ex ->
                        _productList.value = ResponseState.Failure(ex)
                        _mutableMessage.emit("API ERROR ${ex.message}")
                    }.collect {

                        _productList.value = ResponseState.Success(it)
                    }


                } else {
                    _mutableMessage.emit("Can not retrieve data, try again later!")
                }
            } catch (ex: Exception) {
                _productList.value = ResponseState.Failure(ex)
                _mutableMessage.emit("Something went worng ERROR ${ex.message}")
            }
        }
    }
}

class AllProductFactory(private val repo: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllProductsViewModel(repo) as T
    }
}