package com.rokokabis.common.arch

import androidx.lifecycle.ViewModel
import com.rokokabis.domain.common.Result
import com.rokokabis.domain.common.ResultError
import kotlinx.coroutines.*

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext

@ObsoleteCoroutinesApi
abstract class BaseViewModel : ViewModel(), CoroutineScope {
    private val job = Job()
    protected abstract val receiveChannel: ReceiveChannel<Result<Any, ResultError>>

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    abstract fun resolve(value: Result<Any, ResultError>)

    init {
        processStream()
    }

    private fun processStream() {
        launch {
            receiveChannel.consumeEach {
                resolve(it)
            }
        }
    }

    override fun onCleared() {
        receiveChannel.cancel()
        coroutineContext.cancel()
        super.onCleared()
    }
}