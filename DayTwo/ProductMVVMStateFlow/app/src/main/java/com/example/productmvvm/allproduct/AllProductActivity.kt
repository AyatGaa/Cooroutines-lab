package com.example.productmvvm.allproduct

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.productappmvvm.remote.ProductRemoteDataSourceImpl
import com.example.productappmvvm.remote.RetrofitHelper
import com.example.productappmvvm.repositoty.ProductRepositoryImpl
import com.example.productmvvm.allproduct.ui.theme.ProductMVVMTheme
import com.example.productmvvm.data.local.ProductDataBase
import com.example.productmvvm.data.local.ProductLocalDataSourceImpl
import com.example.productmvvm.data.models.Product
import com.example.productmvvm.data.models.ResponseState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

class AllProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AllProductsScreen(
                viewModel = ViewModelProvider(
                    this, AllProductFactory(
                        ProductRepositoryImpl.getInstance(
                            ProductRemoteDataSourceImpl(RetrofitHelper.service),
                            ProductLocalDataSourceImpl(
                                ProductDataBase.getInstance(this@AllProductActivity).getProductDao()
                            )
                        )
                    )
                ).get(AllProductsViewModel::class.java)
            )
        }
    }
}

@Composable
fun AllProductsScreen(viewModel: AllProductsViewModel) {

    val uiState by viewModel.products.collectAsStateWithLifecycle()
    val snackBarState by viewModel.message.collectAsStateWithLifecycle("")

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

//    LaunchedEffect(Unit) {
//        Log.d("TAG", "AllProductsScreen: test in launched edd")
//        viewModel.getProducts()
//    }

    //trying to update snackBar here idk is true or not '_O
    LaunchedEffect(Unit) {
        viewModel.message.collect {
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = snackBarState,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    when (uiState) {

        is ResponseState.Loading -> {
            LodingIndicator()
        }

        is ResponseState.Success -> {
            val list: List<Product> = (uiState as ResponseState.Success).data
            Scaffold(
                snackbarHost = { SnackbarHost(snackBarHostState) },
                modifier = Modifier.fillMaxSize(),
            )
            { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    LazyColumn {
                        items(list.size) {
                            ProductRow(list[it], "Favorite") {
                                viewModel.addToFavourites(list[it])
                            }
                        }
                    }
                }
            }
        }

        is ResponseState.Failure -> {
            Text(
                text = "Sorry, something went wrong can not load data",
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
            )
        }
    }
}

@Composable
fun LodingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
    ) {
        CircularProgressIndicator()
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductRow(product: Product?, actionName: String, action: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    )
    {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
            GlideImage(
                model = product?.thumbnail,
                contentDescription = null,
                modifier = Modifier
                    .size(height = 70.dp, width = 70.dp)
                    .weight(0.1f)
                    .clip(CircleShape)
                    .background(shape = CircleShape, color = Color.LightGray)
                    .size(height = 90.dp, width = 90.dp)
            )
            Column(modifier = Modifier.weight(0.2f)) {
                Text(
                    text = product?.title.toString(),
                    modifier = Modifier.padding(14.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(text = product?.brand.toString(), modifier = Modifier.padding(14.dp))
            }
            Button(onClick = action) {
                Text(text = actionName)
            }
        }
    }
}