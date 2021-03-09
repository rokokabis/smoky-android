package com.rokokabis.domain.encoding

class EncodingUseCase(private val repository: EncodingRepository) {
    sealed class State {
        data class OnProgress(var progress: Int) : State()
        data class OnCompleted(var data: EncodingData) : State()
    }

    suspend fun getEncoding() = repository.getEncoding()
}