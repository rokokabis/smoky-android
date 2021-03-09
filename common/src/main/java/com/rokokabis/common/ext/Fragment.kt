package com.rokokabis.common.ext

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

fun Fragment.askPermission(onGranted: () -> Unit, onDenied: (() -> Unit)? = null) {
    Dexter.withActivity(requireActivity())
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
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val uri = Uri.fromParts(
                            "package",
                            requireActivity().packageName, null
                        )
                        intent.data = uri
                        startActivity(intent)
                    }
                } catch (ex: Exception) {
                    if (report.isAnyPermissionPermanentlyDenied) {
                        onDenied?.invoke()
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val uri = Uri.fromParts(
                            "package",
                            requireActivity().packageName, null
                        )
                        intent.data = uri
                        startActivity(intent)
                    }
                }
            }
        }).check()
}