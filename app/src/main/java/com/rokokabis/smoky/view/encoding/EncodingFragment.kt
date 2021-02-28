package com.rokokabis.smoky.view.encoding

import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Environment
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.rokokabis.smoky.R
import com.rokokabis.smoky.arch.BaseFragment
import com.rokokabis.smoky.utils.observe
import com.rokokabis.smoky.view.MediaCodecActivity
import com.rokokabis.smoky.view.MediaCodecViewModel
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.math.roundToInt


class EncodingFragment : BaseFragment() {
    private val viewModel by activityViewModels<MediaCodecViewModel>()
    private var progressBar: LinearProgressIndicator? = null
    private var textProgress: TextView? = null

    override fun layoutRes() = R.layout.fragment_encoding

    override fun initView(view: View) {
        requireActivity().observe(viewModel.filePathLiveData, ::bindPath)

        progressBar = view.findViewById(R.id.linear_progress)
        textProgress = view.findViewById(R.id.text_progress)
    }

    private fun bindPath(path: String?) {
        Timber.d("codex > $path")
        //requireActivity().toast("Observed path: $path")

        var meta = ""
        path?.let {
            meta += extract(File(it))
        }

        view?.findViewById<TextView>(R.id.text_view)?.text = meta

        view?.post {
            path?.let { startEncoding(it) }
        }

        //path?.let { startEncoding(it) }
    }

    private fun startEncoding(path: String) {
        val moviesDir: File = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MOVIES
        )

        val name = File(path).name.split(".")[0]
        var destination = File(moviesDir, "${name}_compressed.mp4")
        var fileNumber = 0
        while (destination.exists()) {
            fileNumber++
            destination = File(moviesDir, "${name}_compressed$fileNumber.mp4")
        }

        val complexCommand = arrayOf(
            "-y",
            "-i",
            path,
            "-t",
            "20",
            "-s",
            "960x540",
            "-r",
            "24",
            "-vcodec",
            "mpeg4",
            "-b:v",
            "150k",
            "-b:a",
            "44100",
            "-ac",
            "2",
            "-ar",
            "22050",
            destination.absolutePath
        )

        executeFfmpegBinary(complexCommand)
    }

    private fun executeFfmpegBinary(commands: Array<String>) {
        Config.enableLogCallback {
            Timber.d("codex_ffmpeg ${it.text}")
        }

        Config.enableStatisticsCallback {
            Timber.d("codex_ffmpeg $it")

            view?.post {
                val progress = (it.videoFrameNumber / MediaCodecActivity.MAX_FRAME.toDouble()) * 100
                progressBar?.progress = progress.roundToInt()

                textProgress?.text = "Encoding on progress ${progress.roundToInt()}%"
            }
        }

        FFmpeg.executeAsync(
            commands
        ) { executionId, returnCode ->
            if (returnCode == Config.RETURN_CODE_SUCCESS) {
                Timber.d("codex_ffmpeg SUCCESS")
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                Timber.d("codex_ffmpeg CANCELLED")
            }

        }
    }

    private fun extract(file: File): String {
        var info = ""
        val extractor = MediaExtractor()
        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(file.absolutePath)
            extractor.setDataSource(inputStream.fd)

            info += "> Path: ${file.absolutePath}\n"
            info += "> Tracks: ${extractor.trackCount}\n"
            for (i in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(i)
                info += ">> Track[$i]:\n"
                if (format.containsKey(MediaFormat.KEY_MIME)) {
                    info += ">>> ${format.getString(MediaFormat.KEY_MIME)}\n"
                }

                if (format.containsKey(MediaFormat.KEY_HEIGHT)
                    && format.containsKey(MediaFormat.KEY_WIDTH)
                ) {
                    info += ">>> ${format.getInteger(MediaFormat.KEY_HEIGHT)}x${
                        format.getInteger(
                            MediaFormat.KEY_WIDTH
                        )
                    } pixels\n"
                }

                if (format.containsKey(MediaFormat.KEY_FRAME_RATE)) {
                    info += ">>> ${format.getInteger(MediaFormat.KEY_FRAME_RATE)} Fps\n"
                }

                if (format.containsKey(MediaFormat.KEY_SAMPLE_RATE)) {
                    info += ">>> ${format.getInteger(MediaFormat.KEY_SAMPLE_RATE)} hz\n"
                }

                if (format.containsKey(MediaFormat.KEY_BIT_RATE)) {
                    info += ">>> ${format.getInteger(MediaFormat.KEY_BIT_RATE)} bits/sec\n"
                }

            }
        } catch (e: FileNotFoundException) {
            return e.toString()
        } catch (e: IOException) {
            return e.toString()
        } catch (e: RuntimeException) {
            extractor.release()
            inputStream?.close()
        }

        return info
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FFmpeg.cancel()
    }
}