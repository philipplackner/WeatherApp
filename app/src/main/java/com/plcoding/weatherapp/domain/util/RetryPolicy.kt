package com.plcoding.weatherapp.domain.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException


interface RetryPolicy {
    val numRetries: Long
    val delayMillis: Long
    val delayFactor: Long
}

data class DefaultRetryPolicy(
    override val numRetries: Long = 3,
    override val delayMillis: Long = 400,
    override val delayFactor: Long = 1
) : RetryPolicy

fun <T> Flow<T>.retryWithPolicy(
    retryPolicy: RetryPolicy
): Flow<T> {
    var currentDelay = retryPolicy.delayMillis
    val delayFactor = retryPolicy.delayFactor
    return retryWhen { cause, attempt ->
        if (cause is IOException && attempt < retryPolicy.numRetries) {
            delay(currentDelay)
            currentDelay *= delayFactor
            return@retryWhen true
        } else {
            return@retryWhen false
        }
    }
}
