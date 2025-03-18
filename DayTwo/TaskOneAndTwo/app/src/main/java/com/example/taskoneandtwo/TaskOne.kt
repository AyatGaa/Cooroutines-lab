package com.example.taskoneandtwo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

suspend fun main(): Unit = coroutineScope {
    //*
    // reply = num of events Cached
    // extraBufferCapacity = capcity of exr val on top
    // onBufferOverflow = what should do if buffer is full?
    // DROP_OLDEST  = delete first emit
    // DROP_LATEST = delete last emit
    // SUSPEND = save all data
    // */
    val sharedFlow = MutableSharedFlow<Int>(replay =3, extraBufferCapacity =1,  onBufferOverflow = BufferOverflow.DROP_LATEST)
/*
* if emits before collect,, to late the data emitted already
* */
    //sharedFlow.emit(5)

    launch {
        sharedFlow.collect {
            println("First : $it")
        }
    }
  /*if emits here it will not collect, WHY ???*/ //   was it in same time ?
    sharedFlow.emit(6)
    delay(200)
    sharedFlow.emit(7)



    /*if emits after collect , the subscribers ensure they can collect the data*/
//    sharedFlow.emit(1)
//    sharedFlow.emit(2)
//    sharedFlow.emit(3)
//    sharedFlow.emit(4)
//    sharedFlow.emit(8)
//    sharedFlow.emit(9)
//    sharedFlow.emit(10)
//    println("Done here")
//    sharedFlow.emit(12)
//    sharedFlow.emit(13)


/***/
/**
 * Lazily => when first subscriber come
 * eagerlu = > right now !
 * */
    val coro = CoroutineScope(Dispatchers.IO)
    val transformedFlow = flow<Int>{
        emit(99)
        emit(88)
        emit(77)
    }.shareIn(coro, SharingStarted.Eagerly)


    delay(200)
    launch {
        transformedFlow.collect{
            println("This is transformed Flow $it")
        }
    }

}