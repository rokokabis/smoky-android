package com.rokokabis.smoky.domain

import android.net.Uri

data class SamplerClip(var uri: Uri, var startTime: Int, var endTime: Int, var duration: Int)