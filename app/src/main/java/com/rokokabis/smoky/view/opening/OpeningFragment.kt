package com.rokokabis.smoky.view.opening

import android.view.View
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.rokokabis.smoky.R
import com.rokokabis.smoky.arch.BaseFragment

class OpeningFragment : BaseFragment() {
    override fun initView(view: View) {
        val buttonSelectVideo = view.findViewById<MaterialButton>(R.id.button_select_video)

        buttonSelectVideo.setOnClickListener {
            it.findNavController().navigate(R.id.action_openingFragment_to_encodingFragment)
        }
    }

    override fun layoutRes() = R.layout.fragment_opening
}