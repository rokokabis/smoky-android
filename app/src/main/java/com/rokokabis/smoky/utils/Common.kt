package com.rokokabis.smoky.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

fun Activity.askPermission(onGranted: () -> Unit, onDenied: (() -> Unit)? = null) {
    Dexter.withActivity(this)
        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }

            @Override
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                try {
                    if (report.areAllPermissionsGranted()) {
                        onGranted()
                    } else if (report.isAnyPermissionPermanentlyDenied) {
                        onDenied?.invoke()
                        toast("R.string.akses_storage")
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                } catch (ex: Exception) {
                    if (report.isAnyPermissionPermanentlyDenied) {
                        onDenied?.invoke()
                        toast("R.string.akses_storage")
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                }
            }
        }).check()
}

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
            contentResolver.query(contentUri!!, projection, null, null, null)
        val column: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        column?.let { cursor?.getString(it) }
    } catch (e: Exception) {
        cursor?.close()
        e.toString()
    } finally {
        cursor?.close()
    }
}
