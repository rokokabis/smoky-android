package com.rokokabis.data.encoding

import android.os.Environment
import android.util.Log
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.rokokabis.domain.encoding.EncodingModel
import java.io.File

class EncodingService {
    var isSuccess = false
    fun startEncoding(path: String): EncodingModel {
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

        //output = destination.absolutePath
        executeFfmpegBinary(complexCommand)

        return EncodingModel(destination.absolutePath)
    }

    private fun executeFfmpegBinary(commands: Array<String>) {
        Config.enableLogCallback {
            Log.d("codex_ffmpeg", "> ${it.text}")
            //debugInfo.postValue(it.text)
        }

        Config.enableStatisticsCallback {
            Log.d("codex_ffmpeg", "> $it")
            //val progress = (it.videoFrameNumber / MediaCodecActivity.MAX_FRAME.toDouble()) * 100
            //encodingProgress.postValue(EncodingProgress.OnProgress(progress = progress.roundToInt()))
        }

        FFmpeg.executeAsync(
            commands
        ) { _, returnCode ->
            if (returnCode == Config.RETURN_CODE_SUCCESS) {
                //encodingProgress.postValue(EncodingProgress.OnCompleted(output))
                isSuccess = true
                Log.d("codex_ffmpeg", "> SUCCEED")
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                isSuccess = false
                Log.d("codex_ffmpeg", "> CANCELLED")
            }

        }
    }

    companion object {
        const val MAX_FRAME = 480 // 20 seconds of a 24fps video
    }
}