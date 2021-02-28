package com.rokokabis.smoky.view.encoding

import android.media.MediaCodec.BufferInfo
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.rokokabis.smoky.R
import com.rokokabis.smoky.arch.BaseFragment
import com.rokokabis.smoky.utils.observe
import com.rokokabis.smoky.view.MediaCodecViewModel
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.ByteBuffer


class EncodingFragment : BaseFragment() {
    private val viewModel by activityViewModels<MediaCodecViewModel>()

    override fun initView(view: View) {
        requireActivity().observe(viewModel.filePathLiveData, ::bindPath)
    }

    private fun bindPath(path: String?) {
        Timber.d("codex > $path")
        //requireActivity().toast("Observed path: $path")

        var meta = ""
        path?.let {
            meta += extract(File(it))
        }

        view?.findViewById<TextView>(R.id.text_view)?.text = meta

        //path?.let { startEncoding(it) }
    }

    private fun startEncoding(path: String) {
        val muxer = MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        val videoFormat = MediaFormat()
        val audioFormat = MediaFormat()

        val extractor = MediaExtractor()

        val videoTrack = muxer.addTrack(videoFormat)
        val audioTrack = muxer.addTrack(audioFormat)

        val inputBuffer: ByteBuffer = ByteBuffer.allocate(2048)
        val finished = false
        val bufferInfo = BufferInfo()
    }

    override fun layoutRes() = R.layout.fragment_encoding

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
}