package com.kokoakuma.kotlinhttpserver.models

import com.kokoakuma.kotlinhttpserver.models.HttpMethod
import com.kokoakuma.kotlinhttpserver.models.HttpVersion

data class RequestContext (
    val method: HttpMethod,
    val requestTarget: String,
    val httpVersion: HttpVersion,
)
