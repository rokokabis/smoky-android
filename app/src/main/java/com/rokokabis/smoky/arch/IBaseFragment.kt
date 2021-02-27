package com.rokokabis.smoky.arch

import android.view.View

interface IBaseFragment {
    fun initView(view: View)

    fun layoutRes(): Int
}