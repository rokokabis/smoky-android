package com.rokokabis.smoky.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MediaCodecViewModel() : ViewModel() {
    val filePathLiveData: MutableLiveData<String?> = MutableLiveData()
}