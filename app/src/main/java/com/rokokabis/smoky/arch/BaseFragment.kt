package com.rokokabis.smoky.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.rokokabis.smoky.view.opening.OpeningFragment

abstract class BaseFragment : Fragment(), IBaseFragment, LifecycleOwner {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded) initView(view)
    }

    companion object {
        const val BROWSE_VIDEO_REQUEST_CODE = 420
    }
}