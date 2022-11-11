package com.kokoakuma.kotlinhttpserver.exceptions

abstract class HttpException(val status: Int, val reason: String): RuntimeException()

class BadRequestException: HttpException(400, "Bad Request")

class NotFoundException: HttpException(404, "Not Found")