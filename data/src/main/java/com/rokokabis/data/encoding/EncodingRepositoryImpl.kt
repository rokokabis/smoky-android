package com.rokokabis.data.encoding

import com.rokokabis.domain.common.DefaultResult
import com.rokokabis.domain.common.Result
import com.rokokabis.domain.common.ResultError
import com.rokokabis.domain.encoding.EncodingModel
import com.rokokabis.domain.encoding.EncodingRepository

class EncodingRepositoryImpl(private val encodingService: EncodingService) : EncodingRepository {
    override suspend fun getEncoding(encoding: EncodingModel): DefaultResult {
        return try {
            val result =
                encodingService.startEncoding(encoding.path)
            if (encodingService.isSuccess) {
                Result.Success(result)
            } else {
                Result.Failure(ResultError.ResponseResultError)
            }
        } catch (e: Exception) {
            Result.Failure(ResultError.GenericResultError)
        }
    }
}