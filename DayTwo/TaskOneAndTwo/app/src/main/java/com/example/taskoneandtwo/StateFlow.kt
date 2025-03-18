package com.example.taskoneandtwo

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/*
* Don't forget Initial Value!
* replay most recent value
* does not buffer any values, keep last one only
* */


suspend fun main():Unit = coroutineScope{
    val stateFlow = MutableStateFlow("")



    launch {
        stateFlow.collect{
            println("State $it")
        }
    }

    stateFlow.emit("1")
    stateFlow.emit("2")
    delay(200)
    stateFlow.value = "3"
    delay(200)
    stateFlow.value = "4"
    delay(200)
    stateFlow.value = "5"

    //as it thread safe
 // Thread.sleep(1000)
}


