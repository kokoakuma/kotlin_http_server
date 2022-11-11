package com.kokoakuma.kotlinhttpserver

import com.kokoakuma.kotlinhttpserver.exceptions.BadRequestException
import com.kokoakuma.kotlinhttpserver.models.HttpMethod
import com.kokoakuma.kotlinhttpserver.models.HttpVersion
import com.kokoakuma.kotlinhttpserver.models.RequestContext
import kotlinx.cinterop.toKString

/**
 * This class refers to this [format](https://www.rfc-editor.org/rfc/rfc7230#section-3)
 */
class RequestParser {
    fun parse(byteArray: ByteArray): RequestContext {
        val startLineElements = byteArray.toKString()
            .split("\r\n")[0]
            .split(" ")

        val httpMethod = HttpMethod.values()
            .firstOrNull { it.name == startLineElements[2] }
            ?: throw BadRequestException()

        val httpVersion = if (startLineElements.size <= 2) {
            HttpVersion.HTTP_0_9
        } else {
            HttpVersion.from(startLineElements[2])
        }


        return RequestContext(
            httpMethod,
            startLineElements[1],
            httpVersion
        )
    }
}
