package com.rokokabis.smoky.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rokokabis.smoky.R

class MediaCodecActivity : AppCompatActivity() {
    private var haveStoragePermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}