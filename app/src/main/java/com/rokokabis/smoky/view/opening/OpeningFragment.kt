package com.rokokabis.smoky.view.opening

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.rokokabis.common.arch.BaseFragment
import com.rokokabis.common.ext.askPermission
import com.rokokabis.smoky.R
import com.rokokabis.smoky.utils.getFilePath
import com.rokokabis.smoky.utils.toast
import com.rokokabis.smoky.view.MediaCodecViewModel


class OpeningFragment : BaseFragment() {
    private val viewModel by activityViewModels<MediaCodecViewModel>()

    override fun initView(view: View) {
        view.findViewById<MaterialButton>(R.id.button_select_video).setOnClickListener {
            askPermission(
                onDenied = {
                    requireActivity().toast("Permission denied")
                },
                onGranted = {
                    requireActivity().toast("Permission granted")

                    startVideoChooser()
                })

        }
    }

    override fun layoutRes() = R.layout.fragment_opening

    private fun startVideoChooser() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(intent, BROWSE_VIDEO_REQUEST_CODE)
    }

    private fun navigateToEncodingScreen() {
        view?.findNavController()?.navigate(R.id.action_openingFragment_to_encodingFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == BROWSE_VIDEO_REQUEST_CODE) {
            val path = requireActivity().getFilePath(data?.data)
            requireActivity().toast("File path: $path")
            viewModel.filePathLiveData.value = path

            navigateToEncodingScreen()
        }

    }
}