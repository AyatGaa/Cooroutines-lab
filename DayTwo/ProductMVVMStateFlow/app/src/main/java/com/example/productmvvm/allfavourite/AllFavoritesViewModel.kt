package com.example.productmvvm.allfavourite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.productappmvvm.repositoty.ProductRepository
import com.example.productmvvm.allproduct.AllProductsViewModel
import com.example.productmvvm.data.local.ProductLocalDataSource
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


class AllFavoritesViewModel(private val repo: ProductRepository) : ViewModel() {
    private val _favList = MutableStateFlow<ResponseState>(ResponseState.Loading)
    val favList = _favList.asStateFlow()

    private val mutableMessage = MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()

    private val mutableMessageDone = MutableSharedFlow<String>()
    val messageDone = mutableMessage.asSharedFlow()

    fun deleteFromFavorite(product: Product?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (product != null) {
                try {

                    val result = repo.removeProductFromFavourite(product)
                    delay(500)
                    if (result > 0) {
                        mutableMessage.emit("Deleted From Fav!1")
                    } else {
                        mutableMessage.emit("Product already deleted")
                    }
                } catch (e: Exception) {
                    mutableMessage.emit("Can not add Product ${e.message}")
                }
            } else {
                mutableMessage.emit("Can not delete Product, no products to add")
            }
        }
    }

    fun undoDeleting(product: Product?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (product != null) {
                try {
                    repo.addProductToFavourite(product)
                    delay(500)

                } catch (e: Exception) {
                    mutableMessageDone.emit("Can not add Product ${e.message}")
                }
            } else {
                mutableMessageDone.emit("Can not delete Product, no products to add")
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(100)
                val result = repo.getAllProduct(false)
                if (result != null) {
                    result.catch { ex ->
                        _favList.value = ResponseState.Failure(ex)
                        mutableMessage.emit("Loading From Room Error")

                    }.collect {
                        _favList.value = ResponseState.Success(it)
                    }
                } else {
                    mutableMessage.emit("Empty List")
                }
            } catch (ex: Exception) {
                _favList.value = ResponseState.Failure(ex)
                mutableMessage.emit("Something went Wrong, ${ex.message}")

            }
        }
    }
}

class AllFavoriteFactory(private val repo: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllFavoritesViewModel(repo) as T
    }
}