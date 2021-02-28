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

class MediaCodecViewModel : ViewModel() {
    val filePathLiveData: MutableLiveData<String?> = MutableLiveData()
    val encodingProgress: MutableLiveData<EncodingProgress> = MutableLiveData()

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

    fun startEncoding(path: String) {
        // I'm using legacy storage access
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
            val progress = (it.videoFrameNumber / MediaCodecActivity.MAX_FRAME.toDouble()) * 100
            encodingProgress.postValue(EncodingProgress.OnProgress(progress = progress.roundToInt()))
        }

        FFmpeg.executeAsync(
            commands
        ) { executionId, returnCode ->
            if (returnCode == Config.RETURN_CODE_SUCCESS) {
                encodingProgress.postValue(EncodingProgress.OnCompleted)
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