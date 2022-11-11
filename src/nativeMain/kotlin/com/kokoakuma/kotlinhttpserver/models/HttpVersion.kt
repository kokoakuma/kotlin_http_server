package com.kokoakuma.kotlinhttpserver.models

enum class HttpVersion {
    HTTP_0_9,
    HTTP_1_0,
    HTTP_1_1;

    companion object {
        fun from(version: String): HttpVersion {
            return when (version) {
                "" -> HTTP_0_9
                "HTTP/1.0" -> HTTP_1_0
                "HTTP/1.1" -> HTTP_1_1
                else -> HTTP_1_1
            }
        }
    }
}
