package com.rokokabis.smoky.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rokokabis.smoky.R

class MediaCodecActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        const val MAX_FRAME = 480 // 20 seconds of a 24fps video
    }
}