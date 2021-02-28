package com.rokokabis.smoky.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
    observeForever(it)
}

class TestObserver<T> : Observer<T> {
    val observedValue = MutableLiveData<T?>()
    override fun onChanged(value: T?) {
        observedValue.value = value
    }
}