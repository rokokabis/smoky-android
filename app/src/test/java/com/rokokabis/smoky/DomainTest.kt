package com.rokokabis.smoky

import com.google.common.truth.Truth
import com.rokokabis.smoky.domain.EncodingProgress
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DomainTest {

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `verify EncodingProgress accept OnProgress`() {
        val expectedProgress = 69
        val onProgress = EncodingProgress.OnProgress(expectedProgress)

        Truth.assertThat(onProgress).apply {
            isNotNull()
            isNotEqualTo(EncodingProgress.OnCompleted("path"))
        }

        Truth.assertThat(onProgress.progress).apply {
            isEqualTo(expectedProgress)
        }
    }

    @Test
    fun `verify EncodingProgress accept OnComplete`() {
        val expectedPath = "path-to-mp4"
        val onCompleted = EncodingProgress.OnCompleted(expectedPath)

        Truth.assertThat(onCompleted).apply {
            isNotNull()
            isNotEqualTo(EncodingProgress.OnProgress(69))
        }

        Truth.assertThat(onCompleted.path).apply {
            isEqualTo(expectedPath)
        }
    }

}