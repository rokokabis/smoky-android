package com.rokokabis.smoky.view

import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.rokokabis.smoky.domain.EncodingProgress
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class MediaCodecViewModel : ViewModel() {
    val filePathLiveData: MutableLiveData<String?> = MutableLiveData()
    val encodingProgress: MutableLiveData<EncodingProgress> = MutableLiveData()
    val debugInfo: MutableLiveData<String> = MutableLiveData()

    private var output = ""

    fun getInfo(file: File): String {
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
                    info += ">>> ${format.getInteger(MediaFormat.KEY_SAMPLE_RATE) / 1000} kHz\n"
                }

                if (format.containsKey(MediaFormat.KEY_BIT_RATE)) {
                    info += ">>> ${format.getInteger(MediaFormat.KEY_BIT_RATE) / 1000} kbit/sec\n"
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

    fun startEncoding(path: String) {
        // legacy storage access
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
            //"-vf",
            //"crop=540:960:0:540",
            "540x960",
            "-r",
            "24",
            "-vcodec",
            "mpeg4",
            "-b:v",
            "1000k",
            "-b:a",
            "64k",
            "-ac",
            "1",
            "-ar",
            "44100",
            "-strict",
            "-2",
            destination.absolutePath
        )

        output = destination.absolutePath
        executeFfmpegBinary(complexCommand)
    }

    private fun executeFfmpegBinary(commands: Array<String>) {
        Config.enableLogCallback {
            Timber.d("codex_ffmpeg ${it.text}")
            debugInfo.postValue(it.text)
        }

        Config.enableStatisticsCallback {
            Timber.d("codex_ffmpeg $it")
            val progress = (it.videoFrameNumber / MediaCodecActivity.MAX_FRAME.toDouble()) * 100
            encodingProgress.postValue(EncodingProgress.OnProgress(progress = progress.roundToInt()))
        }

        FFmpeg.executeAsync(
            commands
        ) { _, returnCode ->
            if (returnCode == Config.RETURN_CODE_SUCCESS) {
                encodingProgress.postValue(EncodingProgress.OnCompleted(output))
                Timber.d("codex_ffmpeg SUCCESS")
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                Timber.d("codex_ffmpeg CANCELLED")
            }

        }
    }

    fun cancelEncodingProcess() {
        FFmpeg.cancel()
    }
}