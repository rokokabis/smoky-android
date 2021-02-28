package com.rokokabis.smoky

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.rokokabis.smoky.domain.EncodingProgress
import com.rokokabis.smoky.utils.testObserver
import com.rokokabis.smoky.view.MediaCodecViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MediaCodecViewModelTest {
    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    private lateinit var objectUnderTest: MediaCodecViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        objectUnderTest = MediaCodecViewModel()
    }

    @Test
    fun `filePathLiveData should be updated with expected value`() {
        val expected = "path-to-movie.mp4"
        val testObserver = objectUnderTest.filePathLiveData.testObserver()

        objectUnderTest.filePathLiveData.postValue(expected)

        Truth.assertThat(testObserver.observedValue.value).apply {
            isNotNull()
            isEqualTo(expected)
        }
    }

    @Test
    fun `filePathLiveData should accept null`() {
        val nihil: String? = null
        val testObserver = objectUnderTest.filePathLiveData.testObserver()

        objectUnderTest.filePathLiveData.postValue(nihil)

        Truth.assertThat(testObserver.observedValue.value).apply {
            isNull()
        }
    }

    @Test
    fun `verify encodingProgress value = OnProgress`() {
        val progress = 69
        val testObserver = objectUnderTest.encodingProgress.testObserver()

        objectUnderTest.encodingProgress.postValue(EncodingProgress.OnProgress(progress))

        Truth.assertThat(testObserver.observedValue.value).apply {
            isNotEqualTo(EncodingProgress.OnCompleted("path"))
            isEqualTo(EncodingProgress.OnProgress(progress))
        }
    }

    @Test
    fun `verify encodingProgress value = OnCompleted`() {
        val path = "valid-path.mp4"
        val testObserver = objectUnderTest.encodingProgress.testObserver()

        objectUnderTest.encodingProgress.postValue(EncodingProgress.OnCompleted(path))

        Truth.assertThat(testObserver.observedValue.value).apply {
            isEqualTo(EncodingProgress.OnCompleted(path))
            isNotEqualTo(EncodingProgress.OnProgress(0))
        }
    }

    @Test
    fun `verify debugInfo`() {
        val message = "message of debug"
        val testObserver = objectUnderTest.debugInfo.testObserver()

        objectUnderTest.debugInfo.postValue(message)

        Truth.assertThat(testObserver.observedValue.value).apply {
            isNotEmpty()
            isEqualTo(message)
        }
    }
}