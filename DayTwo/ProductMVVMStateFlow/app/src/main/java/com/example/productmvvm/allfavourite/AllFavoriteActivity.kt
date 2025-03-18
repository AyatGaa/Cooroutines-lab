package com.example.productmvvm.allfavourite

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.Font
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
import com.example.productmvvm.allfavourite.ui.theme.ProductMVVMTheme
import com.example.productmvvm.allproduct.AllProductsViewModel
import com.example.productmvvm.allproduct.LodingIndicator
import com.example.productmvvm.data.local.ProductDataBase
import com.example.productmvvm.data.local.ProductLocalDataSourceImpl
import com.example.productmvvm.data.models.Product
import com.example.productmvvm.data.models.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class AllFavoriteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AllFavoriteProductsScreen(
                viewModel =
                ViewModelProvider(
                    this,
                    AllFavoriteFactory(
                        ProductRepositoryImpl.getInstance(
                            ProductRemoteDataSourceImpl(RetrofitHelper.service),
                            ProductLocalDataSourceImpl(
                                ProductDataBase.getInstance(this@AllFavoriteActivity)
                                    .getProductDao()
                            )
                        )
                    )
                ).get(AllFavoritesViewModel::class.java)
            )

        }
    }
}

@Composable
fun AllFavoriteProductsScreen(viewModel: AllFavoritesViewModel) {
    val uiState by viewModel.favList.collectAsStateWithLifecycle()
   val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }
    val scope = rememberCoroutineScope()

  //  viewModel.getProducts()
    // to Undo hehe
    var lastDeleted by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(Unit) {
        viewModel.message.collect  { message ->
            scope.launch {

                val result = snackBarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short,
                    actionLabel = "Undo"
                )
                if (result == SnackbarResult.ActionPerformed) {
                    lastDeleted?.also { deletedItem ->
                        viewModel.undoDeleting(deletedItem)
                    }
                }
                lastDeleted = null
            }
        }
    }
   LaunchedEffect(Unit) {
        viewModel.messageDone.collect  { message ->
            scope.launch {
                     snackBarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short,
                )
            }
        }
    }


    when (uiState) {

        is ResponseState.Loading -> {
            LoadingIndicator()
        }

        is ResponseState.Success -> {
            val list: List<Product> = (uiState as ResponseState.Success).data

            Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) })
            { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    LazyColumn {

                        items(list.size ?: 0) {
                            ProductRow(list.get(it), "Delete") {
                                scope.launch {
                                    delay(500)
                                    lastDeleted = list[it]
                                    viewModel.deleteFromFavorite(list.get(it))
                                }
                            }
                        }
                    }
                }
            }
        }

        is ResponseState.Failure -> {
            Text(
                text = "Sorry, something went wrong can not load data",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
            )
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
                    .size(height = 50.dp, width = 50.dp)
                    .weight(0.1f)
                    .clip(CircleShape)
                    .background(shape = CircleShape, color = Color.LightGray)
                    .size(height = 70.dp, width = 70.dp)
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