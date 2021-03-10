package com.rokokabis.domain.encoding

import com.rokokabis.domain.common.BaseUseCase
import com.rokokabis.domain.common.Result

class EncodingUseCase(private val repository: EncodingRepository) : BaseUseCase<EncodingModel>() {
    override suspend fun run(params: EncodingModel) {
        resultChannel.send(Result.State.Loading(0))

        resultChannel.send(repository.getEncoding(params))

        resultChannel.send(Result.State.Loaded)
    }
}