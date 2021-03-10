package com.rokokabis.domain.common

sealed class ResultError {
    object NetworkResultError : ResultError()
    object GenericResultError : ResultError()
    object ResponseResultError : ResultError()
    object PersistenceResultError : ResultError()
}