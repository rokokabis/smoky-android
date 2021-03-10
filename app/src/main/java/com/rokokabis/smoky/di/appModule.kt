package com.rokokabis.smoky.di

import com.rokokabis.data.encoding.EncodingRepositoryImpl
import com.rokokabis.data.encoding.EncodingService
import com.rokokabis.domain.encoding.EncodingRepository
import com.rokokabis.domain.encoding.EncodingUseCase
import com.rokokabis.smoky.view.MediaCodecViewModel
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModules = module {
    single { EncodingRepositoryImpl(get()) as EncodingRepository }
}

val networkModules = module {
    single{ EncodingService() }
}

val useCaseModules = module {
    single { EncodingUseCase(get()) }
}

@ObsoleteCoroutinesApi
val viewModels = module {
    viewModel {
        MediaCodecViewModel(get())
    }
}


private const val FFMPEG = "ffmpeg"
private const val REPO = "repository"
private const val GET_NEWS_USECASE = "encodingUseCase"