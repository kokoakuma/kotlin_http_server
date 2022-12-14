package com.kokoakuma.kotlinhttpserver

import kotlinx.cinterop.*
import platform.posix.*

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: echoserver.kexe <port>")
        return
    }

    val port = args[0].toShort()
    println("Waiting... <port>:$port")

    init_sockets()

    memScoped {

        val buffer = ByteArray(1024)
        val prefixBuffer = "echo: ".encodeToByteArray()
        val serverAddr = alloc<sockaddr_in>()

        val listenFd = socket(AF_INET, SOCK_STREAM, 0)
            .ensureUnixCallResult("socket") { !(it.isMinusOne()) }

        with(serverAddr) {
            memset(this.ptr, 0, sockaddr_in.size.convert())
            sin_family = AF_INET.convert()
            sin_port = posix_htons(port).convert()
        }

        bind(listenFd, serverAddr.ptr.reinterpret(), sockaddr_in.size.convert())
            .ensureUnixCallResult("bind") { it == 0 }

        listen(listenFd, 10)
            .ensureUnixCallResult("accept") { !it.isMinusOne() }

        val commFd = accept(listenFd, null, null)
            .ensureUnixCallResult("read") { !it.isMinusOne() }

        buffer.usePinned { pinned ->
            while (true) {
                val length = recv(commFd, pinned.addressOf(0), buffer.size.convert(), 0)
                    .ensureUnixCallResult("read") { it >= 0 }

                if (length == 0) {
                    break
                }

                println("Debug:")
                println(pinned.get().toKString())

                val contents = "HTTP/1.1 200 OK\r\nContent-Length: 33\r\n\r\n<html><h1>Hello World</h1></html>\r\n".cstr

                send(commFd, contents, contents.size, 0)
                    .ensureUnixCallResult("write") { it >= 0 }
            }
        }
    }

}

inline fun Int.ensureUnixCallResult(op: String, predicate: (Int) -> Boolean): Int {
    if (!predicate(this)) {
        throw Error("$op: ${strerror(posix_errno())!!.toKString()}")
    }
    return this
}

inline fun Long.ensureUnixCallResult(op: String, predicate: (Long) -> Boolean): Long {
    if (!predicate(this)) {
        throw Error("$op: ${strerror(posix_errno())!!.toKString()}")
    }
    return this
}

inline fun ULong.ensureUnixCallResult(op: String, predicate: (ULong) -> Boolean): ULong {
    if (!predicate(this)) {
        throw Error("$op: ${strerror(posix_errno())!!.toKString()}")
    }
    return this
}


private fun Int.isMinusOne() = (this == -1)
private fun Long.isMinusOne() = (this == -1L)
private fun ULong.isMinusOne() = (this == ULong.MAX_VALUE)