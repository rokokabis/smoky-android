package com.rokokabis.smoky.domain

sealed class EncodingProgress {
    data class OnProgress(var progress: Int) : EncodingProgress()
    data class OnCompleted(var path: String) : EncodingProgress()
}