package com.rokokabis.domain.encoding

interface EncodingRepository {
    suspend fun getEncoding(): EncodingData
}