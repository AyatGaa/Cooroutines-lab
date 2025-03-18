package com.example.taskoneandtwo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import com.example.taskoneandtwo.ui.theme.TaskOneAndTwoTheme
import kotlinx.coroutines.flow.MutableSharedFlow

class MainActivity : ComponentActivity() {
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskOneAndTwoTheme {
                SearchScreen(viewModel = viewModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel) {
    val searchQuery = viewModel.sharedFlow.collectAsState(initial = "")
    val filteredNames = viewModel.filteredName.collectAsState(initial = emptyList())
    val isSearchBarActive = remember { mutableStateOf(false) }

    //the another method you can IGNORE IT
    val filteredNames2 = viewModel.filteredNames2.collectAsState(initial = emptyList())
    val currentText = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery.value,
            onQueryChange = { text ->
                viewModel.onSearch(text)
            },
            onSearch = { },
            active = isSearchBarActive.value,
            onActiveChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) { }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(filteredNames.value) {
                Log.i("TAG", "SearchScreen: $it in lazy")
                Text(
                    text = it,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
