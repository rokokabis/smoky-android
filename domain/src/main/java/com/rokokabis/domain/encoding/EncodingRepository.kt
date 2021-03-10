package com.rokokabis.domain.encoding

import com.rokokabis.domain.common.DefaultResult

interface EncodingRepository {
    suspend fun getEncoding(encoding: EncodingModel): DefaultResult
}