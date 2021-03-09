package com.rokokabis.smoky.view.encoding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.rokokabis.common.arch.BaseFragment
import com.rokokabis.smoky.R
import com.rokokabis.smoky.domain.EncodingProgress
import com.rokokabis.smoky.utils.observe
import com.rokokabis.smoky.view.MediaCodecViewModel
import timber.log.Timber
import java.io.File


class EncodingFragment : BaseFragment() {
    private val sharedViewModel by activityViewModels<MediaCodecViewModel>()
    private val viewModel by viewModels<MediaCodecViewModel>()
    private var progressBar: LinearProgressIndicator? = null
    private var textProgress: TextView? = null

    override fun layoutRes() = R.layout.fragment_encoding

    override fun initView(view: View) {
        requireActivity().observe(sharedViewModel.filePathLiveData, ::bindPath)
        observe(viewModel.encodingProgress, ::bindProgress)
        observe(viewModel.debugInfo, ::bindDebugInfo)

        progressBar = view.findViewById(R.id.linear_progress)
        textProgress = view.findViewById(R.id.text_progress)
    }

    private fun bindPath(path: String?) {
        Timber.d("codex > $path")
        view?.post {
            path?.let {
                viewModel.startEncoding(it)
                setInfo(path = it)
            }
        }
    }

    private fun setInfo(path: String) {
        var meta = ""
        meta += viewModel.getInfo(File(path))

        view?.post {
            view?.findViewById<TextView>(R.id.text_view)?.text = meta
            val thumbImageView = view?.findViewById<ImageView>(R.id.thumb)

            Glide.with(requireActivity())
                .load(path)
                .centerCrop()
                .into(thumbImageView!!)
        }
    }

    private fun bindProgress(progress: EncodingProgress) {
        when (progress) {
            is EncodingProgress.OnCompleted -> {
                view?.post {
                    progressBar?.progress = 100
                    textProgress?.text = "Completed"

                    setInfo(path = progress.path)
                }

                // add bit of delay so user can see what the result is
                view?.postDelayed({
                    view?.findNavController()?.popBackStack()
                }, 1000)
            }
            is EncodingProgress.OnProgress -> {
                view?.post {
                    with(progress.progress) {
                        progressBar?.progress = this
                        textProgress?.text = "Encoding in progress $this%"
                    }

                }
            }
        }
    }

    private fun bindDebugInfo(info: String) {
        with(view) {
            this?.post {
                this.findViewById<TextView>(R.id.debug_text)?.text = info
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cancelEncodingProcess()
    }
}