package com.rokokabis.smoky.view.encoding

import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.rokokabis.smoky.R
import com.rokokabis.smoky.arch.BaseFragment
import com.rokokabis.smoky.domain.EncodingProgress
import com.rokokabis.smoky.utils.observe
import com.rokokabis.smoky.view.MediaCodecViewModel
import timber.log.Timber
import java.io.File


class EncodingFragment : BaseFragment() {
    private val viewModel by activityViewModels<MediaCodecViewModel>()
    private var progressBar: LinearProgressIndicator? = null
    private var textProgress: TextView? = null

    override fun layoutRes() = R.layout.fragment_encoding

    override fun initView(view: View) {
        requireActivity().observe(viewModel.filePathLiveData, ::bindPath)
        requireActivity().observe(viewModel.encodingProgress, ::bindProgress)
        requireActivity().observe(viewModel.debugInfo, ::bindDebugInfo)

        progressBar = view.findViewById(R.id.linear_progress)
        textProgress = view.findViewById(R.id.text_progress)
    }

    private fun bindPath(path: String?) {
        Timber.d("codex > $path")
        var meta = ""
        path?.let {
            meta += viewModel.getInfo(File(it))
        }

        view?.findViewById<TextView>(R.id.text_view)?.text = meta

        view?.post {
            path?.let { viewModel.startEncoding(it) }
        }
    }

    private fun bindProgress(progress: EncodingProgress) {
        when (progress) {
            is EncodingProgress.OnCompleted -> {
                view?.post {
                    progressBar?.progress = 100
                    textProgress?.text = "Completed"
                }
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