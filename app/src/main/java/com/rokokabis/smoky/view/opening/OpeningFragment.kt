package com.rokokabis.smoky.view.opening

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.rokokabis.smoky.R
import com.rokokabis.smoky.arch.BaseFragment
import com.rokokabis.smoky.utils.askPermission
import com.rokokabis.smoky.utils.getFilePath
import com.rokokabis.smoky.utils.toast
import com.rokokabis.smoky.view.MediaCodecViewModel
import timber.log.Timber


class OpeningFragment : BaseFragment() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(MediaCodecViewModel::class.java)
    }

    override fun initView(view: View) {
        val buttonSelectVideo = view.findViewById<MaterialButton>(R.id.button_select_video)

        buttonSelectVideo.setOnClickListener {
//            it.findNavController().navigate(R.id.action_openingFragment_to_encodingFragment)

            requireActivity().askPermission(
                onGranted = {
                    requireActivity().toast("Permission granted")

                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)

                    startActivityForResult(intent, BROWSE_VIDEO_REQUEST_CODE)
                }
            )
        }
    }

    override fun layoutRes() = R.layout.fragment_opening


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == BROWSE_VIDEO_REQUEST_CODE) {
            requireActivity().toast("File path: ${requireActivity().getFilePath(data?.data)}")
        }

    }
}