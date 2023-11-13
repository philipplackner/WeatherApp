package com.plcoding.weatherapp.domain.util

import java.net.ProtocolException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

fun <T> checkError(throwable: Throwable): Resource.Error<T> {
    return when (throwable) {
        is UnknownHostException -> {
            Resource.Error("No internet Connection")
        }
        is SSLHandshakeException -> {
            Resource.Error("SSL handshake error")
        }
        is SocketTimeoutException -> {
            Resource.Error(throwable.localizedMessage ?: "Socket Timeout")
        }
        is ProtocolException -> {
            Resource.Error(throwable.localizedMessage ?: "Protocol Exception")
        }
        else -> {
            Resource.Error(throwable.localizedMessage ?: "Error")
        }
    }
}
