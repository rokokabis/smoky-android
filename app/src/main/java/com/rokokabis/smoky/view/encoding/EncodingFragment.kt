package com.rokokabis.smoky.view.encoding

import android.view.View
import android.widget.TextView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.rokokabis.common.arch.BaseFragment
import com.rokokabis.smoky.R
import com.rokokabis.smoky.view.MediaCodecViewModel
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


@ObsoleteCoroutinesApi
class EncodingFragment : BaseFragment() {
    private val viewModel: MediaCodecViewModel by viewModel()

    private var progressBar: LinearProgressIndicator? = null
    private var textProgress: TextView? = null

    override fun layoutRes() = R.layout.fragment_encoding

    override fun initView(view: View) {
//        requireActivity().observe(sharedViewModel.filePathLiveData, ::bindPath)
//        observe(viewModel.encodingProgress, ::bindProgress)
//        observe(viewModel.debugInfo, ::bindDebugInfo)


        val path = arguments?.getString("path")
        Timber.d("krokayy $path")
        path?.let { viewModel.startEncoding(it) }


        progressBar = view.findViewById(R.id.linear_progress)
        textProgress = view.findViewById(R.id.text_progress)
    }
//
//    private fun bindPath(path: String?) {
//        Timber.d("codex > $path")
//        view?.post {
//            path?.let {
//                viewModel.startEncoding(it)
//                setInfo(path = it)
//            }
//        }
//    }

//    private fun setInfo(path: String) {
//        var meta = ""
//        meta += viewModel.getInfo(File(path))
//
//        view?.post {
//            view?.findViewById<TextView>(R.id.text_view)?.text = meta
//            val thumbImageView = view?.findViewById<ImageView>(R.id.thumb)
//
//            Glide.with(requireActivity())
//                .load(path)
//                .centerCrop()
//                .into(thumbImageView!!)
//        }
//    }

//    private fun bindProgress(progress: EncodingProgress) {
//        when (progress) {
//            is EncodingProgress.OnCompleted -> {
//                view?.post {
//                    progressBar?.progress = 100
//                    textProgress?.text = "Completed"
//
//                    setInfo(path = progress.path)
//                }
//
//                // add bit of delay so user can see what the result is
//                view?.postDelayed({
//                    view?.findNavController()?.popBackStack()
//                }, 1000)
//            }
//            is EncodingProgress.OnProgress -> {
//                view?.post {
//                    with(progress.progress) {
//                        progressBar?.progress = this
//                        textProgress?.text = "Encoding in progress $this%"
//                    }
//
//                }
//            }
//        }
//    }

//    private fun bindDebugInfo(info: String) {
//        with(view) {
//            this?.post {
//                this.findViewById<TextView>(R.id.debug_text)?.text = info
//            }
//        }
//    }


//    override fun onDestroyView() {
//        super.onDestroyView()
//        viewModel.cancelEncodingProcess()
//    }
}