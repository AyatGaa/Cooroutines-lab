package com.example.productmvvm

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.productmvvm.allfavourite.AllFavoriteActivity
import com.example.productmvvm.allproduct.AllProductActivity
import com.example.productmvvm.ui.theme.ProductMVVMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StartingScreen()
        }
    }
}


@Composable
fun StartingScreen() {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            contentDescription = null,
            painter = painterResource(id = R.drawable.ic_launcher_foreground)
        )

        Button(onClick = {
            val intent = Intent(context, AllProductActivity::class.java)
            context.startActivities(arrayOf(intent))
        }) {
            Text(text = "All Products")
        }
        Button(onClick = {
            val intent = Intent(context, AllFavoriteActivity::class.java)
            context.startActivities(arrayOf(intent))

        }) {
            Text(text = "Favorite Product")
        }
        Button(onClick = {}) {
            Text(text = "Exit")
        }
    }

}