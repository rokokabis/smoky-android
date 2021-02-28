package com.rokokabis.smoky.domain

sealed class EncodingProgress {
    data class OnProgress(var progress: Int) : EncodingProgress()
    object OnCompleted : EncodingProgress()
}