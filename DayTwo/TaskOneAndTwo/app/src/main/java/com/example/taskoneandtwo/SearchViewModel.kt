package com.example.taskoneandtwo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.log

class SearchViewModel : ViewModel() {
    val names = listOf("Ayat", "Gamal", "mustafa", "MUHAMMED", "aHmed", "Mazen", "Omnia")

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow


    fun onSearch(text: String) {
        viewModelScope.launch {
            Log.i("TAG", "onSearch: $text")
            _sharedFlow.emit(text)
        }
    }

    val filteredName = sharedFlow
        .map { name ->
            Log.i("TAG", "fileted sheard flow: $name")
            names.filter { it.startsWith(name, ignoreCase = true) || it.contains(name, ignoreCase = true) }
        }


    //trying another method
    private val _filteredNames2 = MutableSharedFlow<List<String>>()
    val filteredNames2: SharedFlow<List<String>> = _filteredNames2.asSharedFlow()

    fun onSearch2(query: String) {
        viewModelScope.launch {
            val filtered = names.filter { it.startsWith(query, ignoreCase = true) || it.contains(it, ignoreCase = true)}
            _filteredNames2.emit(filtered)
        }
    }
}