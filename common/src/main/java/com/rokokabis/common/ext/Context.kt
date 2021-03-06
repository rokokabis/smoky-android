package com.rokokabis.common.ext

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast

fun Context.toast(message: String): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }

fun Context.getFilePath(contentUri: Uri?): String? {
    var cursor: Cursor? = null
    return try {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        cursor =
            contentUri?.let {
                contentResolver.query(
                    it, projection,
                    null, null, null
                )
            }
        val column: Int? =
            cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        column?.let { cursor?.getString(it) }
    } catch (e: Exception) {
        cursor?.close()
        e.toString()
    } finally {
        cursor?.close()
    }
}